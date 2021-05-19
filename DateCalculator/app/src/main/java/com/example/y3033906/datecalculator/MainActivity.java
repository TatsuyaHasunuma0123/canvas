package com.example.y3033906.datecalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableStringBuilder;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //「曜日検索」ボタンの設定
        Button b = findViewById(R.id.button_weekday);
        b.setOnClickListener(this);

        //「経過日数」ボタンの設定
        Button b2 = findViewById(R.id.button_days);
        b2.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        //EditTextから文字列を取得
        EditText et_year = findViewById(R.id.editText_year); //年数を入力するEditTextから取得
        EditText et_month = findViewById(R.id.editText_month); //月数を入力するEditTextから取得
        EditText et_day = findViewById(R.id.editText_day); //日数を入力するEditTextから取得

        //int型で日付を取得
        int year = getText(et_year);
        int month = getText(et_month) - 1; //月数には-1が必要
        int day = getText(et_day);

        //取得したデータを元にsetCalendarを呼び出す
        Setting.shared.cal = setCalendar(year, month, day);

        //入力が無い場合のエラー処理(monthは-2)
        if ((year == -1) || (month == -2) || (day == -1)) {
            showToast("入力がありません。");
        }

        //入力がある時
        else {
            Intent intent = null;
            //カレンダーを初期化

            switch (view.getId()) {
                case R.id.button_weekday: //「曜日検索」が押された場合
                    intent = new Intent(this, WeekdayActivity.class);
                    break;
                case R.id.button_days: //「経過日数」が押された場合
                    intent = new Intent(this, DateDiffActivity.class);
                    break;
            }

            startActivity(intent);
        }
    }

    //EditTextの文字列を取得
    private int getText(EditText et){
        SpannableStringBuilder ssb = (SpannableStringBuilder)et.getText();
        if(ssb.toString().equals(""))
            return -1; //取得に失敗したら-1を返す
        return Integer.parseInt(ssb.toString());
    }

    //カレンダーのインスタンスを取得、日付を格納
    private static Calendar setCalendar(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal;
    }

    public void showToast(String string){
        Toast t = Toast.makeText(
                this, string, Toast.LENGTH_SHORT);
        t.show();
    }


}