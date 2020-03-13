package com.wei.appwidget05;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

// <SUMMARY> 廣播接收類別：接收時間更新廣播 </SUMMARY>
public class MyTimeTick extends BroadcastReceiver {

    // <SUMMARY> 收到廣播後執行 </SUMMARY>
    @Override
    public void onReceive(Context context, Intent intent) {
        // 建立更新小工具元件的管理物件
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        // 建立小工具畫面的包裝元件
        ComponentName cn = new ComponentName(context, MyAppWidget.class);

        // 取得所有安裝在桌面的小工具編號
        int[] ids = manager.getAppWidgetIds(cn);

        // 進行時間更新
        new MyAppWidget().onUpdate(context, manager, ids);
    }
}
