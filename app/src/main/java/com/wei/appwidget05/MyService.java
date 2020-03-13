package com.wei.appwidget05;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

// <SUMMARY> 背景服務類別：更新時間 </SUMMARY>
public class MyService extends Service {

    // 更新時間的廣播接收物件
    private MyTimeTick tick;

    // 通知管理物件
    private NotificationManager manager;

    // <SUMMARY> 服務建立 </SUMMARY>
    @Override
    public void onCreate() {

        // 建立廣播接收器
        if (tick == null)
            tick = new MyTimeTick();

        // 產生通知管理物件
        manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        // 建立一個通知用的 channel ID
        String channelID = "com.wei.config_notify.channel";
        // 建立並設定 channel notification
        createChannel(channelID);

        // 建立通知物件
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channelID);
        Notification notification = builder.build();

        // 啟動通知服務
        startForeground(1, notification);
    }

    private void createChannel(String channelID) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        // 建立通知 channel 物件
        NotificationChannel channel = new NotificationChannel(channelID, "config",
                NotificationManager.IMPORTANCE_DEFAULT);
        // 設定 channel 說明
        channel.setDescription("Config notify channel");

        // 加入通知 channel
        manager.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 過濾每分鐘發送一次的廣播事件
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        // 廣播註冊
        registerReceiver(tick, filter);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //
        unregisterReceiver(tick);
    }
}
