package com.wei.appwidget05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {

    private static final String TAG = ConfigActivity.class.getSimpleName();

    // 五種可供選擇的文字顏色
    private static String[] TEXT_COLORS = null;

    // 文字文色 Preference 名稱
    public static final String TEXT_COLOR_KEY = "TEXT_COLOR_KEY";
    // 背景資源 Preference 名稱
    public static final String BACKGROUND_KEY = "BACKGROUND_KEY";

    // 文字顏色的按鈕編號
    private int colorIdx = 0;
    // 背景預設編號
    private int backgroundId = R.drawable.appwidget_drawable;
    // 畫面元件 時間文字
    private TextView txtConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);

        processViews();
    }

    private void processViews() {
        TEXT_COLORS = getResources().getStringArray(R.array.txt_colors);
        Log.d(TAG, "processViews: " + TEXT_COLORS[0] + TEXT_COLORS[1]);
        txtConfig = findViewById(R.id.txtConfig);
    }

    // <summary> Select what color set text </summary>
    public void btnColor(View view) {
        // 取得顏色按鈕的 id
        int id = view.getId();

        switch (id) {
            case R.id.btnColor1:
                colorIdx = 0;
                break;
            case R.id.btnColor2:
                colorIdx = 1;
                break;
            case R.id.btnColor3:
                colorIdx = 2;
                break;
            case R.id.btnColor4:
                colorIdx = 3;
                break;
            case R.id.btnColor5:
                colorIdx = 4;
                break;
        }
       // 設定預覽文字顏色
        txtConfig.setTextColor(Color.parseColor(TEXT_COLORS[colorIdx]));
    }

    // <summary> Set appWidget background color </summary>
    public void btnStroke(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnStroke1:
                backgroundId = R.drawable.appwidget_drawable;
                break;
            case R.id.btnStroke2:
                backgroundId = R.drawable.appwidget_drawable_stroke;
                break;
        }

        // 設定預覽背景
        txtConfig.setBackgroundResource(backgroundId);
    }

    // <summary> button ok function </summary>
    public void btnOk(View view) {
        // 儲存小工具編號
        int appWidgetId = 0;

        // 取得由小工具傳來的 intent 物件
        Intent intent = getIntent();

        // 取得包裝所有資料的 bundle 物件
        Bundle extras = intent.getExtras();

        if (extras != null) {
            // 取得小工具編號
            // 小工具編號資料的名稱，讀取失敗的預設值
            // 使用在 AppWidgetManager 中宣告好的變數
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // 取得 SharedPreferences 物件
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        // 取得編輯 SharedPreferences 的物件
        SharedPreferences.Editor editor = preferences.edit();

        // 存放小工具需要的資料
        // 每個小工具用自已的編號識別自已的設定值，設定時 key 值加上自己的 id

        // 存放文字編號，KEY 值加上自己的 ID
        editor.putString(TEXT_COLOR_KEY + appWidgetId, TEXT_COLORS[colorIdx]);
        // 存放背景資訊，KEY 值加上自己的 ID
        editor.putInt(BACKGROUND_KEY + appWidgetId, backgroundId);
        // 存入資料
        editor.apply();

        // 取得更新元件的 Manager 物件
        AppWidgetManager manager = AppWidgetManager.getInstance(this);

        // 建立 AppWidget05 物件並執行更新小工具方法
        new MyAppWidget().onUpdate(this, manager,
                new int[] {appWidgetId});

        // 存入小工具編號
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // 設定回傳結果為 OK，小工具樣式設定完成
        setResult(RESULT_OK, intent);
        // 結束設定
        finish();
    }

    // <summary> 取得指定小工具的文字設定顏色 </summary>
    // <param name='context'> </param>
    // <param name='appWidgetId'> 小工具編號 </param>
    public static int getTextColorPreferences(Context context, int appWidgetId) {
        // 取得讀取資料的 preferences 物件
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        // 取得設定文字顏色
        // 如無，預設黑色
        String colorStr = preferences.getString(TEXT_COLOR_KEY + appWidgetId,
                "#000000");

        return Color.parseColor(colorStr);
    }

    // <summary> 取得指定小工具的背景 </summary>
    // <param name='context'> </param>
    // <param name='appWidgetId'> 小工具編號 </param>
    public static int getBackgroundPreferences(Context context, int appWidgetId) {

        // 取得讀取資料用的 preferences 物件
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getInt(BACKGROUND_KEY + appWidgetId,
                R.drawable.appwidget_drawable);
    }
}
