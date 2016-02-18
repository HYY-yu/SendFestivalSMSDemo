package com.app.feng.sendfestivalsmsdemo.element;

/**
 * Created by feng on 2015/10/22.
 */
public class Festival {

    private String name;
    private int id;

    //创建节日表
    public static String TABLE_FESTIVAL = "festival";
    public static String FESTIVAL_ID = "_id";
    public static String FESTIVAL_NAME = "name";
    public static String FESTIVAL_CODE = "code";

    public Festival(Festival f) {
        if (f != null) {
            name = f.getName();
            id = f.getId();
        }
    }

    public Festival(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
