package com.app.feng.sendfestivalsmsdemo.element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 该类用于SmsSQLOpenHelper类的建表
 * Created by feng on 2015/10/24.
 */
public class HistoryMessage{

    private int id;
    private String msg;
    //发送给哪个号码 给了谁
    private String name;
    private String number;
    //对应那个节日的短信
    private String festivalName;
    //发送时间
    private Date date;
    //格式化时间方便显示
    private String dateStr;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //建立HistoryMessage表所用到的列名
    public static final String TABLE_NAME = "histroy_message";
    public static final String COLUMN_MSG = "msg";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_FESTIVAL_NAME = "festivalName";
    public static final String COLUMN_DATE = "date";

    public String getDateStr() {
        dateStr = dateFormat.format(date);
        return dateStr;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
