package com.aman.teenscribblers.galgotiasuniversitymsim.BroadCastReciever;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class SimScheduleReceiver extends BroadcastReceiver {

    private static final int _REFRESH_INTERVAL = 60 * 60 * 6; // 5 hours
    // Alarm id
    private static final int ALARM_ID = 105; // This can be any random integer.

    PendingIntent pi = null;
    AlarmManager am = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SimStartServiceReceiver.class);
        pi = PendingIntent.getBroadcast(context, ALARM_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            am.cancel(pi);
        } catch (Exception ignored) {
        }
        am.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 1000 * _REFRESH_INTERVAL, pi);
    }
}
