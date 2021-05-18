package com.example.y3033906.canvas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
    public Integer first = 0;

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

        //FloatingActionButtonの設定
        FloatingActionButton fab_DeleteAll = findViewById(R.id.fab);
        fab_DeleteAll.setOnClickListener(this);

        //TextViewの設定
        TextView textView_ShowPenWidth = findViewById(R.id.textWidth);

        /*-----------------------------------SeekBarの設定-----------------------------------------*/
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
                        textView_ShowPenWidth.setText(str);
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

    //menuを表示するメソッド
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //ボタンが押された時に呼び出されるメソッド
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


    //「menu」内のボタンが押された時に呼び出されるメソッド
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PopupWindow menu_PopupWindow = new PopupWindow(MainActivity.this);
        //ボタンによって処理を変更
        switch (item.getItemId()) {
            //「save」が押された時
            case R.id.action_save:
                //PopupWindowに設定するViewをinflateしてsetContentView
                View popupView = getLayoutInflater().inflate(R.layout.popup_layout_save, null);
                //PopupWindowのボタンを設定
                popupView.findViewById(R.id.button_popup_save).setOnClickListener(new View.OnClickListener() {

                    @Override
                    //「保存」ボタンが押された時の処理
                    public void onClick(View v) {
                        if (menu_PopupWindow.isShowing()) {

                            //保存するファイルの名前を取得するet_FileNameとssb_FileName
                            EditText et_FileName = popupView.findViewById(R.id.editText_popup_save_name);
                            SpannableStringBuilder ssb_FileName = (SpannableStringBuilder) et_FileName.getText();

                            //bitmap_Canvasに出力する前に背景を白色に変更
                            View view = findViewById(R.id.myView);
                            view.setBackgroundColor(Color.WHITE);

                            Bitmap bitmap_Canvas = getViewCapture(findViewById(R.id.myView));
                            saveToFile(bitmap_Canvas,ssb_FileName);
                            showToast("保存しました");
                            menu_PopupWindow.dismiss();
                        }
                    }

                });
                menu_PopupWindow.setContentView(popupView);
                screenPopupWindow(menu_PopupWindow);
                break;

            //「browse」が押された時
            case R.id.action_read:
                //PopupWindowに設定するViewをinflateしてsetContentView
                popupView = getLayoutInflater().inflate(R.layout.popup_layout_read, null);
                //PopupWindowのボタンを設定
                popupView.findViewById(R.id.button_popup_read).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (menu_PopupWindow.isShowing()) {
                            EditText et_FileName = popupView.findViewById(R.id.editText_popup_read_name);
                            SpannableStringBuilder ssb_FileName = (SpannableStringBuilder) et_FileName.getText();
                            readToFile(ssb_FileName);
                            showToast("読み出しました");
                            menu_PopupWindow.dismiss();
                        }
                    }
                });
                menu_PopupWindow.setContentView(popupView);
                screenPopupWindow(menu_PopupWindow);
                break;
        }
        return true;
    }

    //activity_mainのMyViewの範囲のみを画像として返す
    public Bitmap getViewCapture(View view) {
        view.setDrawingCacheEnabled(true);
        //Viewのキャッシュを取得
        Bitmap cache = view.getDrawingCache();
        //キャッシュを画像として取得
        Bitmap screenshot_canvas = Bitmap.createBitmap(cache);

        //imageViewを画像として取得
        if(first != 0) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            screenshot_canvas = blendBitmap(screenshot_canvas,image);
        }
        view.setDrawingCacheEnabled(false);

        first++;

        return screenshot_canvas;
    }

    //currentBitmapとblendBitmapを合成
    public Bitmap blendBitmap(Bitmap currentBitmap, Bitmap blendBitmap){
        int width = currentBitmap.getWidth();
        int height = currentBitmap.getHeight();
        Bitmap new_bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(new_bitmap);
        canvas.drawBitmap(currentBitmap,0,0,null);
        int disWidth = (width - blendBitmap.getWidth());
        int disHeight = (height - blendBitmap.getHeight());
        canvas.drawBitmap(blendBitmap,disWidth,disHeight,null);
        return new_bitmap;
    }

    //描画した画像のデータbitmap_Canvasを、ssb_FileNameのファイルに保存する
    private void saveToFile(Bitmap bitmap_Canvas, SpannableStringBuilder ssb_FileName) {
        Context context = getApplicationContext();
        //拡張子を設定
        String fileName = ssb_FileName + ".jpg";
        //「/Pictures/"(ssb_FileNeme).jpg"」のパスを取得
        File save_to_file = new File (context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        try {
            //書き込み
            FileOutputStream outputStream = new FileOutputStream(save_to_file);
            bitmap_Canvas.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //「ssb_FileName」をもとに、ファイルを取得
    public void readToFile(SpannableStringBuilder ssb_FileName){
        Context context = getApplicationContext();
        //拡張子を設定
        String fileName = ssb_FileName + ".jpg";
        //「/Pictures/"(ssb_FileName).jpg」のパスを取得
        File read_to_file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),fileName);
        try{
            //読み込み
            FileInputStream inputStream = new FileInputStream(read_to_file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ImageView iv = findViewById(R.id.imageView);
            iv.setImageBitmap(bitmap);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    //mPopupWindowを用いて画面にポップアップウィンドウを表示(xmlには記述せず、javaで記述する必要がある。)
    public void screenPopupWindow(PopupWindow mPopupWindow){
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
    }

    public void showToast(String string){
        Toast t = Toast.makeText(
                this, string, Toast.LENGTH_SHORT);
        t.show();
    }

}