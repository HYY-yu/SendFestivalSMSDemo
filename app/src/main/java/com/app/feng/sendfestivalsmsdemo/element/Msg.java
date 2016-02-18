package com.app.feng.sendfestivalsmsdemo.element;

/**
 * Created by feng on 2015/10/22.
 */
public class Msg {
    private int id;

    private String festivalName;

    //创建信息表
    public static String TABLE_MSG = "msg_table";
    public static String MSG_ID = "_id";
    public static String MSG_FESTIVAL_NAME = "festival_name";
    public static String MSG_CONTENT = "content";

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    private String content;


    public Msg(Msg msg){
        this.id = msg.id;
        this.content = msg.content;
        this.festivalName = msg.festivalName;
    }

    public Msg( int id, String content, String festivalName) {
        this.id = id;
        this.content = content;
        this.festivalName = festivalName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
