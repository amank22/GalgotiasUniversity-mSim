package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceFetchingEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceProccesedEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.ConnectionDetector;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SubjectParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.TodayAttParcel;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 19-02-2015.
 * Modified 6 July 2017
 */
public class AttendanceJob extends Job {

    private String startDate = "", endDate = "";
    private String type;
    private DbSimHelper dbhelper;
    private boolean inService;

    public AttendanceJob(boolean inService, String type, String startDate, String endDate) {
        super(new Params(AppConstants.PRIORITY1).groupBy(AppConstants.GroupAttendance).requireNetwork());
        this.startDate = startDate == null ? "" : startDate;
        this.endDate = endDate == null ? "" : endDate;
        this.inService = inService;
        this.type = type;
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {
        if (!inService)
            EventBus.getDefault().post(new AttendanceFetchingEvent(true));

    }

    @Override
    public void onRun() throws Throwable {
        if (!ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            throw new Exception(AppConstants.ERROR_NETWORK);
        }

        String attInitialData = IonMethods.getString(AppConstants.AttendanceString);
        List<Pair<String, String>> attTypes = parseAttendanceTypes(attInitialData);
        for (Pair<String, String> attType : attTypes) {
            if (type.equals(attType.second)) {
                String tableStringAtt = postToServer(attType.first, attType.second);
                addToDatabase(attType.second, tableStringAtt);
            }
        }
        if (!inService)
            EventBus.getDefault().post(new AttendanceProccesedEvent("FetchedFromNetwork", null));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if (!inService) {
            EventBus.getDefault().post(new AttendanceErrorEvent(AppConstants.ERROR_NO_CONTENT));
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (runCount == maxRunCount)
            return RetryConstraint.CANCEL;
        if (throwable.getMessage().equals(AppConstants.ERROR_NETWORK)) {
            EventBus.getDefault().post(new AttendanceErrorEvent(AppConstants.ERROR_NETWORK));
            return RetryConstraint.CANCEL;
        } else if (throwable.getMessage().equals(AppConstants.ERROR_SESSION_EXPIRED)) {
            EventBus.getDefault().post(new SessionExpiredEvent());
            return RetryConstraint.CANCEL;
        }
        if (!inService)
            EventBus.getDefault().post(new AttendanceErrorEvent("Retrying for " + runCount + " times"));
        return RetryConstraint.RETRY;
    }

    private void addToDatabase(String value, String pageData) throws Exception {
        //MCPH1_SCPH_GVSubject MCPH1_SCPH_gvDailyAttendence1
        if (!value.equals("Today Attendance")) {
            HashMap<String, String> colorCodes = parseColorCode(pageData);
            // TODO: 26/09/17 All Color codes and corrosponding texts here. 
            Elements rows = parseDoc(pageData, "MCPH1_SCPH_GVSubject");
            dbhelper.deletesubj();
            for (Element row : rows) {
                Elements values = row.select("td span");
                String semester = values.get(3).text();
                String subject = values.get(4).text();
                // TODO: 26/09/17 Here in gcet only status is there so need to removed these 4 values and add a status
                String status = row.select("td").last().attr("bgcolor");
                String statusText = colorCodes.get(status);
                // TODO: 26/09/17 Here is the text which can be shown to student 
                // TODO: 26/09/17 Can add new field to database to store this text and color 
                // TODO: 26/09/17 After storing get this color and text and show in recyclerview of @AttendanceContentFragment 
                int present = 0;
                int absent = 0;
                int total = 0;
                float percentage = 0;
                SubjectParcel subjectParcel = new SubjectParcel(semester, subject, present, absent, total, percentage);
                dbhelper.addnewsubj(subjectParcel, String.format("%s-%s", startDate, endDate));
            }
        } else {
            Elements rows = parseDoc(pageData, "MCPH1_SCPH_gvDailyAttendence1");
            dbhelper.deletetoday();
            for (Element row : rows) {
                Elements values = row.select("td > span");
                String session = values.get(1).text();
                String program = values.get(2).text();
                String semester = values.get(3).text();
                String subject = values.get(4).text();
                String timeSlot = values.get(5).text();
                String attendanceType = values.get(6).text();
                String status = values.get(7).text();
                TodayAttParcel todayAttParcel = new TodayAttParcel(session, program, semester, subject, timeSlot, attendanceType, status);
                dbhelper.addnewtoday(todayAttParcel);
            }
        }
    }

    private List<Pair<String, String>> parseAttendanceTypes(String attInitialData) throws Exception {
        List<Pair<String, String>> types = new ArrayList<>();
        Document doc = Jsoup.parse(attInitialData);
        Elements rows = doc.select("input[type=submit]");
        for (Element row : rows) {
            final String value = row.attr("value");
            final String name = row.attr("name");
            if (!value.equalsIgnoreCase("show")) {
                types.add(new Pair<>(name, value));
            }
        }
        if (types.isEmpty()) {
            throw new Exception(AppConstants.ERROR_NO_CONTENT);
        }
        return types;
    }

    private String postToServer(String type, String typevalue) throws Exception {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            return IonMethods.post(AppConstants.AttendanceString, getnvp(type, typevalue, false));
        } else {
            String initCall = IonMethods.post(AppConstants.AttendanceString, getnvp(type, typevalue, false));
            IonMethods.setvsev(initCall);
            return IonMethods.post(AppConstants.AttendanceString, getnvp(type, typevalue, true));
        }
    }

    private ContentValues getnvp(String type, String typevalue, boolean showDates) {
        ContentValues nameValuePair = new ContentValues();
        nameValuePair.put("__EVENTTARGET", "");
        nameValuePair.put("__EVENTARGUMENT", "");
        nameValuePair.put("__VIEWSTATE", AppConstants.viewstate);
        nameValuePair.put("__VIEWSTATEGENERATOR", "5A6B7435");
        nameValuePair.put("ctl00$ctl00$txtCaseCSS", "textDefault");
        nameValuePair.put("__EVENTVALIDATION", AppConstants.eventvalidate);
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$hdnStudentId", "1241");
        if (!showDates) {
            nameValuePair.put(type, typevalue);
        }
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$txtDFrom", !showDates ? "" : startDate);
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$txtDTo", !showDates ? "" : endDate);
        if (showDates) {
            nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$btnShowSubject", "Show");
        }
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$txtFrom", "");
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$txtTo", "");
        return nameValuePair;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }

    private HashMap<String, String> parseColorCode(String s) throws Exception {
        HashMap<String, String> colorsMap = new HashMap<>();
        String tableid = "table.font-size11";
        Document doc = Jsoup.parse(s);
        Elements table = doc.select("table." + tableid + " > tbody > tr");
        if (table.isEmpty()) {
            throw new Exception(AppConstants.ERROR_NO_CONTENT);
        }
        Element row = table.first();
        Elements colors = row.select("td > span");
        Elements values = row.select("td > strong");
        if (values.size() != colors.size()) {
            throw new Exception(AppConstants.ERROR_NO_CONTENT);
        }
        for (int i = 0; i < values.size(); i++) {
            String colorRaw = colors.get(i).select("*[style*='background-color']").toString();
            String[] split = colorRaw.split("#");
            if (split.length <= 1) {
                throw new Exception(AppConstants.ERROR_NO_CONTENT);
            }
            String color = "#" + split[1].substring(0, 6);
            String value = values.get(i).html();
            colorsMap.put(color, value);
        }
        return colorsMap;
    }

    private Elements parseDoc(String s, String tableid) throws Exception {
        Document doc = Jsoup.parse(s);
        Elements table = doc.select("table#" + tableid + " > tbody > tr").not(".top-heading");
        if (table.isEmpty()) {
            throw new Exception(AppConstants.ERROR_NO_CONTENT);
        }
        return table;
    }


}