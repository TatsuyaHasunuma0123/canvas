package com.example.y3033906.datecalculator;

import java.util.Calendar;

//Activity間での共通のデータを有するクラス
public class Setting {
    public static Setting shared = new Setting(); //シングルトンを作成
    public Calendar cal;  //TextEditから取得した年月日が格納される変数
    public String errMsg = "そのような日付は存在しませんでした。"; //エラーメッセージ
}
