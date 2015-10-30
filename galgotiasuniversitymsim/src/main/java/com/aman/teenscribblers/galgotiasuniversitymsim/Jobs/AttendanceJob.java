package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import android.content.ContentValues;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceFetchingEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceProccesedEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 19-02-2015.
 */
public class AttendanceJob extends Job {

    String type, value;
    private DbSimHelper dbhelper;
    int count = 0;
    String[][] data;
    boolean inService;
    String error = "NULL";

    public AttendanceJob(String type, String value, boolean inService) {
        super(new Params(AppConstants.PRIORITY1).groupBy(AppConstants.GroupAttendance).requireNetwork());
        this.type = type;
        this.value = value;
        this.inService = inService;
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {
        if (!inService)
            EventBus.getDefault().post(new AttendanceFetchingEvent(true));

    }

    @Override
    public void onRun() throws Throwable {
        String s = Attendance(type, value);
        if (s.equals("error")) {
            EventBus.getDefault().post(new AttendanceErrorEvent("Error Fetching Attendance"));
            if (getCurrentRunCount() > 3) onCancel();
        }
        if (s.equals("expired")) {
            EventBus.getDefault().post(new AttendanceErrorEvent("Session Expired"));
            if (getCurrentRunCount() > 3)
                onCancel();
        }
        int i;
        switch (value) {
            case "Subject+Wise+Attendance":
                parseDoc(s, "MCPH1_SCPH_GVSubject");
                dbhelper.deletesubj();
                for (i = 0; i < data.length; i++) {
                    if (data[i][4] == null)
                        continue;
                    dbhelper.addnewsubj(data[i][4], Integer.parseInt(data[i][5]),
                            Integer.parseInt(data[i][6]),
                            Integer.parseInt(data[i][7]),
                            Float.parseFloat(data[i][8]));
                }
                break;
            case "Monthly+Attendance":
                parseDoc(s, "MCPH1_SCPH_gvMonthly");
                dbhelper.deleteMonthly();
                for (i = 0; i < data.length; i++) {
                    if (data[i][4] == null)
                        continue;
                    dbhelper.addnewmonthly(data[i][4],
                            Integer.parseInt(data[i][5]),
                            Integer.parseInt(data[i][6]),
                            Integer.parseInt(data[i][7]),
                            Float.parseFloat(data[i][8]));
                }
                break;
            case "Semester+Attendance":
                parseDoc(s, "MCPH1_SCPH_gvAttendanceDetail");
                dbhelper.deleteSem();
                for (i = 0; i < data.length; i++) {
                    if (data[i][4] == null)
                        continue;
                    dbhelper.addnewsem(data[i][3], Integer.parseInt(data[i][4]),
                            Integer.parseInt(data[i][5]),
                            Integer.parseInt(data[i][6]),
                            Float.parseFloat(data[i][7]));
                }
                break;
            case "Today+Attendance":
                parseDoc(s, "MCPH1_SCPH_gvDailyAttendence1");
                dbhelper.deletetoday();
                for (i = 0; i < data.length; i++) {
                    if (data[i][4] == null)
                        continue;
                    dbhelper.addnewtoday(data[i][4], data[i][5], data[i][6],
                            data[i][7]);
                }
                break;
        }
        if (!inService)
            EventBus.getDefault().post(new AttendanceProccesedEvent("FetchedFromNetwork", null));
    }

    @Override
    protected void onCancel() {
        if (!inService) {
            EventBus.getDefault().post(new AttendanceErrorEvent("No Record Found on WebSim"));
        }
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        error = throwable.getLocalizedMessage();
        if (!inService)
            EventBus.getDefault().post(new AttendanceErrorEvent("Retrying for " + super.getCurrentRunCount() + " times"));
        return true;
    }

    String Attendance(String type, String typevalue) {
        String s = "error";
        try {
            if (IonMethods.get(AppConstants.AttendanceString))
                s = IonMethods.post(AppConstants.AttendanceString,
                        getnvp(type, typevalue));
        } catch (Exception e) {
            s = e.getMessage();
            e.printStackTrace();
        }
        return s;
    }

    private ContentValues getnvp(String type, String typevalue) {
        ContentValues nameValuePair = new ContentValues();
        nameValuePair.put("__EVENTTARGET", "");
        nameValuePair.put("__EVENTARGUMENT", "");
        nameValuePair.put("__VIEWSTATE", AppConstants.viewstate);
        nameValuePair.put("ctl00$ctl00$txtCaseCSS", "textDefault");
        nameValuePair.put("__EVENTVALIDATION", AppConstants.eventvalidate);
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$hdnStudentId", "1241");
        nameValuePair.put(type, typevalue);
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$txtFrom", "");
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$txtTo", "");
        return nameValuePair;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }

    void parseDoc(String s, String tableid) {
        Document doc = Jsoup.parse(s);
        //   EventBus.getDefault().post(new AttendanceErrorEvent(doc.toString()));
        Elements table = doc.select("table#" + tableid);
        for (Element row : table.select("tr")) {
            count++;
        }
//        for (Element row : table.select("tr")) {
//            for (Element heading : row.select("th")) {
//                //    Log.d("heading", heading.text());
//            }
//        }
        data = new String[count][9];
        int i = 0;
        for (Element row : table.select("tr")) {
            int j = 0;
            for (Element column : row.select("td")) {
                if (!column.text().equals("")) {
                    data[i][j] = column.text();
                    Log.d("New", data[i][j]);
                }
                j++;
            }
            i++;
        }
    }


}
