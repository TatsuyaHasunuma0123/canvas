package com.example.y3033906.datecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class DateDiffActivity extends AppCompatActivity implements View.OnClickListener {

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
        String diff = null; //日数差を格納する変数weekday
        cal.setLenient(false);
        try {
            cal.getTime();
        } catch (Exception ex) {
            diff = Setting.shared.errMsg; //正しくない場合
        }
        //確認はここまで。

        //nullの場合、日付は正しいため、日付との差を返す関数を呼び出す
        if (diff == null) {
            diff = dateDiff(cal);
        }

        screen(diff);

    }

    //引数の日付との差を日単位で返す
    private static String dateDiff(Calendar cal){
        Calendar calendarNow = Calendar.getInstance();
        //ミリ秒単位での差分計算　//(例)入力された日数(cal)の方が進んでいる場合、diffTime < 0。
        long diffTime = calendarNow.getTimeInMillis() - cal.getTimeInMillis();

        //日単位に変換　//不具合修正のため、if文を使用。
        long diffDays = 0;
        //diffDays = diffTime / (1000*60*60*24);

        if(diffTime < 0) //diffTime < 0　の時、「〇日後」の表示が一日ずれる不具合を修正
            diffDays = (diffTime - 1000) / (1000*60*60*24) ;
        else
            diffDays = diffTime / (1000*60*60*24); //「現在，〇日前」の表示は問題なし。

        //String型に変換してreturn
        return String.valueOf(diffDays);
    }

    //画面に、「〇日前，〇日後，現在」のいずれかを表示する
    public void screen(String diff){
        TextView tv = findViewById(R.id.textView1);

        if(diff.equals(Setting.shared.errMsg)); //weekdayが文字列(errMsg)の時、エラーになるのを防ぐための処理
        else
        if(Integer.parseInt(diff) > 0 )
            diff = diff + "日前です";

        else if(Integer.parseInt(diff) < 0){
            //「－〇日後」と表示されるため、「－」を消去
            diff = String.valueOf(-1 * Integer.parseInt(diff));
            diff = diff + "日後です" ;
        }

        else
            diff = "現在です";
        tv.setText(diff);
    }

    //「戻る」ボタンが押された時の画面遷移
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}