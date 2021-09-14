package io.dcloud.uniplugin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirePush {
    final static String TAG = "FirePush";
    final static int NOTIFY_ID = 2233;
    final static int REQUEST_NO = 1001;
    public static String Token = "";

    public static void getToken(Context iContext){
        //获取推送Token
        try {
            Toast.makeText(iContext, "开始获取Token", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Start");
            FirebaseMessaging fire = FirebaseMessaging.getInstance();
            fire.getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }
                // Get new FCM registration token
                String token = task.getResult();
                Token = token;
                // Log and toast
                Log.e(TAG, token);
                Toast.makeText(iContext,"获取Token：" + token, Toast.LENGTH_SHORT).show();
            });
        }catch (Exception ex){
            Log.e(TAG, "Fetching FCM registration token failed", ex);
        }
    }

    public static void sendNotification(Context iContext,String messageTitle, String messageBody) {
        // 接收到通知后，点击通知，启动 MessageActivity
        Intent intent = new Intent();
        intent.setData(Uri.parse("rising://com.app.kok/app"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(iContext.getApplicationContext(), REQUEST_NO, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = null;
        NotificationManager manager = (NotificationManager)iContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String id = "channelId";
            String name = "channelName";
            NotificationChannel channel = new NotificationChannel(id,name,NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            notification = new Notification.Builder(iContext,iContext.getPackageName())
                    .setChannelId(id)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(io.dcloud.uniplugin.R.drawable.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(iContext.getResources(), io.dcloud.uniplugin.R.drawable.icon))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        }else{
            notification = new NotificationCompat.Builder(iContext,iContext.getPackageName())
                    .setContentTitle(messageTitle)
                    .setContentText(messageTitle)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(io.dcloud.uniplugin.R.drawable.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(iContext.getResources(), io.dcloud.uniplugin.R.drawable.icon))
                    .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                    .setLights(Color.BLUE, 1, 1)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        }
//                builder.setContentIntent(pendingIntent);
//                builder.setFullScreenIntent(pendingIntent, true);//将一个Notification变成悬挂式Notification
        manager.notify(NOTIFY_ID,notification);
    }
}
