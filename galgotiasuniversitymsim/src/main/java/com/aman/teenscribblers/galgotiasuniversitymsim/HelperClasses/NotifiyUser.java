package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class NotifiyUser {

    static final int Notify_TAG = 2821994;
    static NotificationManager notifier = null;

    public static void notify(Context c, String title, String content) {
        if (c != null) {
            notifier = (NotificationManager) c
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent cintent = PendingIntent.getActivity(c, 28021994, new Intent(), 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c)
                    .setSmallIcon(R.drawable.ic_stat_notification)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(cintent)
                    .setAutoCancel(true)
                    .setColor(Color.BLACK);
            notifier.notify(Notify_TAG, mBuilder.build());
        }
    }

    public static void notify(Context c, String title, String content,Intent i) {
        if (c != null) {
            notifier = (NotificationManager) c
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent cintent = PendingIntent.getActivity(c, 28021994,i, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c)
                    .setSmallIcon(R.drawable.ic_stat_notification)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(cintent)
                    .setAutoCancel(true)
                    .setColor(Color.BLACK);
            notifier.notify(Notify_TAG, mBuilder.build());
        }
    }

    public static void RemoveNotification(Context c) {
        if (c != null) {
            notifier = (NotificationManager) c
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notifier.cancel(Notify_TAG);
        }
    }
}
