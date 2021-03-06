package com.example.y3033906.canvas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Bitmap bmp = null;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*-------------------------------ボタンの設定----------------------------------------------*/
        //黒
        Button black = findViewById(R.id.button_black);
        black.setOnClickListener(this);
        //橙
        Button orange = findViewById(R.id.button_orange);
        orange.setOnClickListener(this);
        //赤
        Button red = findViewById(R.id.button_red);
        red.setOnClickListener(this);
        //青
        Button blue = findViewById(R.id.button_blue);
        blue.setOnClickListener(this);
        //水色
        Button sky = findViewById(R.id.button_sky);
        sky.setOnClickListener(this);
        //緑
        Button green = findViewById(R.id.button_green);
        green.setOnClickListener(this);
        //黄緑
        Button w_green = findViewById(R.id.button_w_green);
        w_green.setOnClickListener(this);
        //黄
        Button yellow = findViewById(R.id.button_yellow);
        yellow.setOnClickListener(this);
        //白（消しゴムとして使用)
        Button erase = findViewById(R.id.button_erase);
        erase.setOnClickListener(this);
        /*----------------------------------------------------------------------------------------*/

        /*----------------------------FloatingActionButtonの設定-----------------------------------*/
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        /*----------------------------------------------------------------------------------------*/

        //TextViewの設定
        TextView textView = findViewById(R.id.textWidth);


        /*-----------------------------SeekBarの設定-----------------------------------------------*/
        SeekBar seekBar = findViewById(R.id.seekBar);
        //初期値
        seekBar.setProgress(0);
        //最大値
        seekBar.setMax(100);
        //onClickListenerと同様の記述seekBar.setOnSeekBarChangeListener
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //つまみがドラッグされると呼ばれる
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //0の時も描画されるため、感覚的に分かるように「0%」と表示しないための処理
                        if (progress == 0)
                            progress++;
                        String str = String.format(Locale.US, "%d %%", progress);
                        textView.setText(str);
                        MyView.width = progress;
                    }

                    //つまみがタッチされた時に呼ばれる
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) { //特に処理は無し
                    }

                    //つまみがリリースされた時に呼ばれる
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { //特に処理は無し
                    }
                });
        /*----------------------------------------------------------------------------------------*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View view) {
        //ボタンによって処理を変更
        switch (view.getId()) {
            case R.id.button_black: //黒色をセット
                MyView.setPen(Color.BLACK);
                break;
            case R.id.button_orange: //橙色をセット
                MyView.setPenRGB(244, 67, 54);
                break;
            case R.id.button_red://赤色をセット
                MyView.setPen(Color.RED);
                break;
            case R.id.button_blue://青色をセット
                MyView.setPen(Color.BLUE);
                break;
            case R.id.button_sky://水色をセット
                MyView.setPen(Color.CYAN);
                break;
            case R.id.button_green://緑色をセット
                MyView.setPenRGB(75, 175, 80);
                break;
            case R.id.button_w_green://黄緑色をセット
                MyView.setPenRGB(139, 195, 74);
                break;
            case R.id.button_yellow://黄色をセット
                MyView.setPen(Color.YELLOW);
                break;
            case R.id.button_erase://白色(消しゴム)をセット
                MyView.setPen(Color.WHITE);
                break;
            case R.id.fab: //書いた絵を全て消す
                //MainActivityを呼び出すことで疑似的に消去
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    /*-------------------------「menu」のボタンが押された時の処理-------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //ボタンによって処理を変更
        switch (item.getItemId()) {

            //「save]が押された時
            case R.id.action_menu1:
                PopupWindow mPopupWindow = new PopupWindow(MainActivity.this);

                /*---------------------------レイアウト設定-----------------------------------------*/
                //PopupWindowに設定するViewをinflateしてsetContentView
                View popupView = getLayoutInflater().inflate(R.layout.popup_layout_save, null);
                popupView.findViewById(R.id.button_popup_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    //「保存」ボタンが押された時の処理
                    public void onClick(View v) {
                        if (mPopupWindow.isShowing()) {
                            //保存するファイルの名前を取得するtext
                            EditText text = popupView.findViewById(R.id.editText_popup_save_name);
                            SpannableStringBuilder ssb = (SpannableStringBuilder) text.getText();
                            String filename = ssb + ".jpeg";
                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + filename);
                            String path = String.valueOf(file);
                            //指定したファイルが無ければ作成する。
                            file.getParentFile().mkdir();
                            Bitmap bitmap = getViewCapture(findViewById(R.id.myView));
                            saveToFile(bitmap,ssb);
                            showToast("保存しました");
                            mPopupWindow.dismiss();
                        }
                    }
                });
                mPopupWindow.setContentView(popupView);
                /*--------------------------------------------------------------------------------*/

                // 背景設定
                mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

                // タップ時に他のViewでキャッチされないための設定(この設定をしないと、ポップアップウィンドウに貫通してタッチパネルが反応する)
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);

                // 表示サイズの設定 今回は幅300dp
                float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setWidth((int) width);
                mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

                // 画面中央に表示
                mPopupWindow.showAtLocation(findViewById(R.id.button_black), Gravity.CENTER, 0, 0);
                break;

            //「browse」が押された時
            case R.id.action_menu2:
                //処理を記述
                mPopupWindow = new PopupWindow(MainActivity.this);

                // レイアウト設定
                popupView = getLayoutInflater().inflate(R.layout.popup_layout_read, null);
                popupView.findViewById(R.id.button_popup_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPopupWindow.isShowing()) {
                            //ここに処理を記述
                            EditText text = popupView.findViewById(R.id.editText_popup_save_name);
                            SpannableStringBuilder ssb = (SpannableStringBuilder) text.getText();
                            readFile(ssb);
                            showToast("読み出しました");
                            mPopupWindow.dismiss();
                        }
                    }
                });
                mPopupWindow.setContentView(popupView);

                // 背景設定
                mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

                // タップ時に他のViewでキャッチされないための設定
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);

                // 表示サイズの設定 今回は幅300dp
                width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setWidth((int) width);
                mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

                // 画面中央に表示
                mPopupWindow.showAtLocation(findViewById(R.id.button_black), Gravity.CENTER, 0, 0);
                break;
        }
        return true;
    }
    /*---------------------------------------------------------------------------------------------*/

    public Bitmap getViewCapture(View view) {
        view.setDrawingCacheEnabled(true);
        //Viewのキャッシュを取得
        Bitmap cache = view.getDrawingCache();
        Bitmap screenshot = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        return screenshot;
    }

    private void saveToFile(Bitmap bitmap, SpannableStringBuilder ssb) {
        Context context = getApplicationContext();
        String fileName = ssb + ".jpg";
        File file = new File (context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void readFile(SpannableStringBuilder ssb){
        Context context = getApplicationContext();
        String fileName = ssb + ".jpg";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),fileName);
        try{
            FileInputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ImageView iv = findViewById(R.id.imageView);
            iv.setImageBitmap(bitmap);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void showToast(String string){
        Toast t = Toast.makeText(
                this, string, Toast.LENGTH_SHORT);
        t.show();
    }

}