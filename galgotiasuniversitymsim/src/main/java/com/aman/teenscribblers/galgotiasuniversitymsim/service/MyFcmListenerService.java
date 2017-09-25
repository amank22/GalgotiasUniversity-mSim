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
import android.support.v4.app.NotificationCompat;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.NewsActivity;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsTopicsParcel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;

public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyFcmListenerService";
    private Target pictureTarget;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();

        final String id = data.containsKey("id") ? data.get("id").toString() : null;
        String topicId = data.containsKey("topicid") ? data.get("userid").toString() : null;
        final String message = data.containsKey("message") ? data.get("message").toString() : null;
        final String imageUrl = data.containsKey("image") ? data.get("image").toString() : null;
        String systemMsg = data.containsKey("systemmsg") ? data.get("systemmsg").toString() : null;
        final String author = from.replace("/topics/", "");
        if (systemMsg != null && !systemMsg.equals("true")) {
            if (id == null) {
                return;
            }
            String admNo = PrefUtils.getFromPrefs(this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, null);
            if (admNo == null) {
                return;
            }
            String gcmId = FirebaseInstanceId.getInstance().getToken();
            IonMethods.getNewsSpecificTopic(topicId, admNo, gcmId, new FutureCallback<Response<NewsTopicsParcel>>() {
                @Override
                public void onCompleted(Exception e, Response<NewsTopicsParcel> result) {
                    try {
                        DbSimHelper.getInstance().addnewnews(id, message, imageUrl, author);
                        sendNotification(message, imageUrl, author);
                    } catch (Exception ignore) {
                    }
                }
            });
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
                .setSmallIcon(R.drawable.ic_launcher)
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
            pictureTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    final NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    bigPictureStyle.setSummaryText(message).setBigContentTitle("News from " + from).bigPicture(bitmap);
                    notificationBuilder.setStyle(bigPictureStyle);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    sendNotification(message, null, from);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(MyFcmListenerService.this).load(imageUrl).resize(256, 256).into(pictureTarget);
                }
            });
        }

    }
}