package com.resourcefulparenting.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.resourcefulparenting.R;
import com.resourcefulparenting.activity.MainActivity;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String MEESAGE = "";

    private NotificationManager mNotificationManager;
    //private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private Context context;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        H.L("fcmtoken"+refreshToken);
        Prefs.setRegistrationTokenID(MyFirebaseMessagingService.this, s);
        ////Log.e("TAG", "Token" + Prefs.getRegistrationTokenID(this));
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //   //H.L("From" +remoteMessage);
         String response = remoteMessage.getData().get("body");
        context = MyFirebaseMessagingService.this;
      //  String response = remoteMessage.getData().get("message");
        H.L("fcmtoken1"+response);

        //String text = response.replace("[", "").replace("]", "");
       // String text1 = text.replace("\"", "").replace("\"", "");


      //  if (Prefs.getNewMessage(context).equals("select")) {
          //  sendNotification(text1);
     //   }

        if (response !=null)
        {
            if (response.equalsIgnoreCase(""))
            {

            }
            else
            {
                sendNotification(response);
            }

        }


    }


    private void sendNotification(String body) {

        Intent intent = new Intent(this, MainActivity.class);
   /*     if(body.contains("has send you message")) {
            intent.putExtra("from", "Message");
    }
        else
        {
            intent.putExtra("from", "MyFirebaseMessage");
        }*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        String channelId = getString(R.string.default_notification_channel_id);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(notificationSound)
                        .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
               mNotificationManager.notify(0 /* ID of notification */, mBuilder.build());
    }
}
