package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import android.content.ContentValues;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableStartEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 19-02-2015.
 */
public class TimeTableJob extends Job {

    String day;
    private DbSimHelper dbhelper;
    int count = 0;
    String[][] data;
    String error = "NULL";
    private static List<String> days = null;
    private int current_day = 0;


    public TimeTableJob(String day) {
        super(new Params(AppConstants.PRIORITY2).groupBy(AppConstants.GroupTimeTable).requireNetwork());
        this.day = day;
        dbhelper = DbSimHelper.getInstance();
        days = new ArrayList<>();
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new TimeTableStartEvent("Fetching From Network"));
    }

    @Override
    public void onRun() throws Throwable {
        String s = GetTTData();
        if (s.equals("error")) {
            EventBus.getDefault().post(new TimeTableErrorEvent("Error"));
        }
        if (s.equals("expired")) {
            EventBus.getDefault().post(new TimeTableErrorEvent("Expired"));
        }
        Document doc = Jsoup.parse(s);
        Elements table = doc.select("table");
        for (Element row : table.select("tr")) {
            count++;
        }
        data = new String[count][9];
        int i = 0;
        int j;
        for (Element row : table.select("tr")) {
            j = 0;
            for (Element column : row.select("td")) {
                if (j == 0 && days.contains(column.text())) {
                    current_day = days.indexOf(column.text());
                    j--;
                } else {
                    //  Log.d("DATA", "does not contains");
                    if (data[i - 1][0] == null || data[i - 1][0].equals("")) {
                        data[i - 1][0] = days.get(current_day);
                    }
                    if (column.text() != null) {
                        data[i - 1][j + 1] = column.text();
                    }
                }
                j++;
            }
            i++;
        }
        dbhelper.deleteTimeTable();
        for (i = 0; i < data.length; i++) {
            dbhelper.addnewtimetable(data[i][2], data[i][0], data[i][4], data[i][3], data[i][5], data[i][6]);
        }
        EventBus.getDefault().post(new TimeTableSuccessEvent("Stored", null));
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new TimeTableErrorEvent("Error"));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        error = throwable.getLocalizedMessage();
        EventBus.getDefault().post(new TimeTableErrorEvent("Retrying for " + super.getCurrentRunCount() + " time"));
        return true;
    }

    private String GetTTData() {
        String s = "error";
        try {
            if (IonMethods.get(AppConstants.TimeTableString))
                s = IonMethods.post(AppConstants.TimeTableString,
                        getnvp());
        } catch (Exception e) {
            s = "error";
            e.printStackTrace();
        }
        return s;
    }

    private ContentValues getnvp() {
        ContentValues nameValuePair = new ContentValues();
        nameValuePair.put("__EVENTTARGET", "");
        nameValuePair.put("__EVENTARGUMENT", "");
        nameValuePair.put("__VIEWSTATE",
                AppConstants.viewstate);
        nameValuePair.put("ctl00$ctl00$txtCaseCSS",
                "textDefault");
        nameValuePair.put("__EVENTVALIDATION",
                AppConstants.eventvalidate);
        nameValuePair.put(
                "ctl00$ctl00$MCPH1$SCPH$hdnStudentId", "1241");
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$Button1", "Weekly");

        return nameValuePair;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }


}
