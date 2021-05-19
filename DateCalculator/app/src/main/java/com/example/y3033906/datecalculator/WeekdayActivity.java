package com.example.y3033906.datecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class WeekdayActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //「戻る」ボタンを設定
        Button b = findViewById(R.id.button_back);
        b.setOnClickListener(this);

        //SettingクラスからCalendar calを取得
        Calendar cal = Setting.shared.cal;

        //日付が正しいか確認
        String weekday = null; //曜日を格納する変数weekday
        cal.setLenient(false);
        try {
            cal.getTime();
        } catch (Exception ex) {
            weekday = Setting.shared.errMsg; //正しくない場合
        }
        //確認はここまで。

        //nullの場合、日付は正しいため、曜日を返す関数を呼び出す
        if (weekday == null) {
            weekday = getWeekDay(cal);
        }

        screen(weekday);

    }

    //calの日付の曜日を返す
    private static String getWeekDay(Calendar cal){
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY: //日曜日
                return "日曜日";
            case Calendar.MONDAY: //月曜日
                return "月曜日";
            case Calendar.TUESDAY: //火曜日
                return "火曜日";
            case Calendar.WEDNESDAY: //水曜日
                return "水曜日";
            case Calendar.THURSDAY: //木曜日
                return "木曜日";
            case Calendar.FRIDAY: //金曜日
                return "金曜日";
            case Calendar.SATURDAY: //土曜日
                return "土曜日";
        }
        return null;
    }

    //画面に、weekdayに格納されている文字列を表示
    public void screen(String weekday){
        TextView tv = findViewById(R.id.textView1);
        tv.setText(weekday);
    }

    //「戻る」ボタンが押された時の画面遷移
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}