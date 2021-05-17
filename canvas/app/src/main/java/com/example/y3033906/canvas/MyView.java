package com.example.y3033906.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MyView extends View {
    static Paint pen = new Paint();
    static Integer width = 0;

    //イベント発生時のx座標、y座標を保存するための動的配列
    private ArrayList array_x, array_y; //タッチパネルが押された時の座標を格納する配列
    private ArrayList array_color; //座標と同時に色のデータを格納する配列
    private ArrayList array_width; //座標と同時にペンの太さのデータを格納する配列
    private ArrayList array_status;

    /*-------------------------------------コンストラクタ-------------------------------------------*/
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        array_x = new ArrayList();
        array_y = new ArrayList();
        array_color = new ArrayList();
        array_width = new ArrayList();
        array_status = new ArrayList();
    }
    /*--------------------------------------------------------------------------------------------*/

    /*----------------------------タッチパネルを操作した時に呼ばれるメソッド---------------------------*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //座標を取得
        int x = (int) event.getX();
        int y = (int) event.getY();

        //イベントに応じて動作を変更
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //タッチパネルが押された時
            case MotionEvent.ACTION_POINTER_DOWN:
                array_x.add(new Integer(x)); //座標を配列に保存
                array_y.add(new Integer(y)); //線の描画はしない(false)
                array_color.add(pen.getColor()); //色のデータを保存
                array_width.add(width); //太さのデータを保存
                array_status.add(new Boolean(false));
                invalidate(); //画面を強制的に再描画
                break;
            case MotionEvent.ACTION_MOVE:
                array_x.add(new Integer(x)); // 座標を配列に保存
                array_y.add(new Integer(y)); // 線の描画をする(true)
                array_color.add(pen.getColor()); //色のデータを保存
                array_width.add(width); //太さのデータを保存
                array_status.add(new Boolean(true));
                invalidate(); // 画面を強制的に再描画
                break;
            case MotionEvent.ACTION_UP: //タッチパネルから離れた時
            case MotionEvent.ACTION_POINTER_UP:
                array_x.add(new Integer(x)); //座標を配列に保存
                array_y.add(new Integer(y)); //線の描画をする(true)
                array_color.add(pen.getColor()); //色のデータを保存
                array_width.add(width); //太さのデータを保存
                array_status.add(new Boolean(true));
                invalidate(); //画面を強制的に再描画
                break;
        }
        return true;
    }
    /*--------------------------------------------------------------------------------------------*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //背景を白で塗りつぶす
        /*Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0,0,canvas.getWidth(),canvas.getHeight()),p);*/

        //描画用のPaintオブジェクトを用意
        pen.setStyle(Paint.Style.STROKE);

        //配列内の座標を読み出して線（軌跡）を描画
        for (int i = 1; i < array_status.size(); i++) {
            pen.setColor((Integer) array_color.get(i));
            pen.setStrokeWidth((Integer)array_width.get(i));
            if((Boolean) array_status.get(i)) {
                int x1 = (Integer) array_x.get(i-1);
                int x2 = (Integer) array_x.get(i);
                int y1 = (Integer) array_y.get(i-1);
                int y2 = (Integer) array_y.get(i);
                canvas.drawLine(x1,y1,x2,y2,pen);
            }
        }
    }

    //ペンの色を設定するメソッド
    public static void setPen(int color) {
        pen.setColor(color);
    }

    //ペンの色をRGBで設定するメソッド
    public static void setPenRGB(int red,int green, int blue) {
        int color = Color.rgb(red, green, blue);
        pen.setColor(color);
    }
}


