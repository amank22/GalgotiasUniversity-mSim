package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.ResultParcel;
import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 10/6/16.
 */
public class ResultJob extends Job {

    private DbSimHelper dbhelper;

    public ResultJob() {
        super(new Params(AppConstants.PRIORITY4).requireNetwork());
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if (!Connection_detect.isConnectingToInternet(getApplicationContext())) {
            throw new Exception(AppConstants.ERROR_NETWORK);
        }
        String resultInitialData = IonMethods.getString(AppConstants.ResultString);
        List<String> semestersList = parseStringToFindSemesters(resultInitialData);
        dbhelper.deleteResult();
        for (String semester : semestersList) {
            String semResult = GetResutForSemester(semester);
            parseResultForSpecificSemester(semester, semResult);
        }
//        FileUtil.createFile(getApplicationContext(), AppConstants.FILE_NAME_PERSONAL, parsedInfo);
        EventBus.getDefault().post(new InfoEvent(false, null, false));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if (cancelReason == CancelReason.REACHED_RETRY_LIMIT)
            EventBus.getDefault().post(new InfoEvent(false, AppConstants.ERROR_CONTENT_FETCH, true));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable.getMessage().equals(AppConstants.ERROR_NETWORK)) {
            EventBus.getDefault().post(new InfoEvent(false, throwable.getMessage(), true));
            return RetryConstraint.CANCEL;
        } else if (throwable.getMessage().equals(AppConstants.ERROR_NO_CONTENT)) {
            EventBus.getDefault().post(new InfoEvent(false, throwable.getMessage(), true));
            return RetryConstraint.CANCEL;
        } else if (throwable.getMessage().equals(AppConstants.ERROR_SESSION_EXPIRED)) {
            EventBus.getDefault().post(new SessionExpiredEvent());
            return RetryConstraint.CANCEL;
        }
        return RetryConstraint.RETRY;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }

    private void parseResultForSpecificSemester(String semster, String semesterResult) {
        Document doc = Jsoup.parse(semesterResult);
        Elements resultRows = doc.select("table").get(1).select("tbody > tr");
        for (Element resultRow : resultRows) {
            Elements columns = resultRow.select("td > span").not(".top-heading border-radius");
            if (columns.size() > 1) {
                String subject = columns.get(1).text();
                String grade = columns.get(3).text();
                dbhelper.addNewResult(subject, grade, semster);
            }
        }

    }

    private List<String> parseStringToFindSemesters(String initialData) throws Exception {
        List<String> semesters = new ArrayList<>();
        Document doc = Jsoup.parse(initialData);
        Elements rows = doc.select("select > option");
        for (Element row : rows) {
            if (!row.text().equalsIgnoreCase("--Select--")) {
                semesters.add(row.attr("value"));
            }
        }
        if (semesters.isEmpty()) {
            throw new Exception(AppConstants.ERROR_NO_CONTENT);
        }
        return semesters;
    }

    private String GetResutForSemester(String semester) throws Exception {
        return IonMethods.post(AppConstants.ResultString,
                getnvp(semester));
    }

    private ContentValues getnvp(String semester) {
        ContentValues nameValuePair = new ContentValues();
        nameValuePair.put("__EVENTTARGET", "");
        nameValuePair.put("__EVENTARGUMENT", "");
        nameValuePair.put("__VIEWSTATE", AppConstants.viewstate);
        nameValuePair.put("ctl00$ctl00$txtCaseCSS", "textDefault");
        nameValuePair.put("__EVENTVALIDATION", AppConstants.eventvalidate);
        nameValuePair.put("__VIEWSTATEGENERATOR", "F79DB338");
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$hdnStudentId", "1241");
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$ddldrp", semester);
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$btnRun", "Show");
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$txtRemark", "");

        return nameValuePair;
    }
}
