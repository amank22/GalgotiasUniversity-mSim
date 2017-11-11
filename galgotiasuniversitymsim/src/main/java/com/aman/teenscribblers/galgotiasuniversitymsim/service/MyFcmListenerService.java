package com.aman.teenscribblers.galgotiasuniversitymsim.service;

/**
 * Created by aman on 23-10-2015 in Galgotias University(mSim).
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.NewsActivity;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.GlideApp;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyFcmListenerService";
    private SimpleTarget<Bitmap> target;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();

        String id = data.containsKey("id") ? data.get("id").toString() : null;
        String message = data.containsKey("message") ? data.get("message").toString() : null;
        String imageUrl = data.containsKey("image") ? data.get("image").toString() : null;
        String systemMsg = data.containsKey("systemmsg") ? data.get("systemmsg").toString() : null;
        String aEmail = data.containsKey("a_email") ? data.get("a_email").toString() : null;
        String aPic = data.containsKey("a_pic") ? data.get("a_pic").toString() : null;
        String author = from.replace("/topics/", "");
        if (systemMsg != null && !systemMsg.equals("true")) {
            if (id == null) {
                return;
            }
            try {
                DbSimHelper.getInstance().addnewnews(id, message, imageUrl, author, aEmail, aPic);
                sendNotification(message, imageUrl, author);
            } catch (Exception ignore) {
            }
        } else if (systemMsg != null) {
            assert message != null;
            String[] newTopics = message.split(",");
            for (String ntop : newTopics) {
                RegistrationIntentService.subscribeTopics(this, ntop);
            }
        }
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     * @param from    FROM topic
     */
    private void sendNotification(final String message, final String imageUrl, final String from) {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, message.hashCode(), intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("News from " + from)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (imageUrl == null || imageUrl.isEmpty()) {
            final NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle("News from " + from).bigText(message);
            notificationBuilder.setStyle(bigTextStyle);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    final NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    bigPictureStyle.setSummaryText(message).setBigContentTitle("News from " + from).bigPicture(bitmap);
                    notificationBuilder.setStyle(bigPictureStyle);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    sendNotification(message, null, from);
                }
            };
            handler.post(new Runnable() {
                @Override
                public void run() {
                    GlideApp.with(MyFcmListenerService.this).asBitmap().load(imageUrl).into(target);
                }
            });
        }

    }
}