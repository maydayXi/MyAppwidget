package com.wei.appwidget05;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import java.util.Date;

public class MyAppWidget extends AppWidgetProvider {

    // <summary> 如果小工具建立就啟動服務 </summary>
    // <param name='context'> 要啟動服務的元件 </param>
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        // 啟動服務
        startService(context);
    }

    // <summary> 啟動服務元件 </summary>
    private void startService(Context context) {

        // 建立啟動服務的 intent 物件
        Intent intent = new Intent(context, MyService.class);

        // 版本不同，啟動服務方式不同
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intent);
        else
            context.startService(intent);
    }

    // <summary> 執行更新小工具畫面的工作 </summary>
    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            // 建立包裝所有小工具畫面的物件：用以存取小工具元件的畫面
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.appwidget_layout);
            // 取得現在時間
            String nowStr = String.format("%tR", new Date());
            // 設定現在時間
            views.setTextViewText(R.id.txtNow, nowStr);

            // 取得指定小工具編號的文字顏色設定
            int txtColor = ConfigActivity.getTextColorPreferences(context,
                    appWidgetId);
            // 設定指定的文字顏色
            views.setTextColor(R.id.txtNow, txtColor);

            // 取得指定小工具編號的背景樣式設定
            int txtBackground = ConfigActivity.getBackgroundPreferences(
                    context, appWidgetId);
            views.setInt(R.id.txtNow, "setBackgroundResource",
                    txtBackground);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    // <summary> 如果小工具被移除，就停止更新時間的服務 </summary>
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        stopService(context);
    }

    // <summary> 停止服務元件 </summary>
    private void stopService(Context context) {
        // 建立停止服務的 intent 物件
        Intent intent = new Intent(context, MyService.class);
        // 停止服務
        context.stopService(intent);
    }
}
