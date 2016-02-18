package com.app.feng.sendfestivalsmsdemo.element;

/**本来历史短信类和收藏短信类都应该继承自Msg类，但是写历史短信类时没有考虑清楚，
 * 继承自Msg类不一定方便
 *
 * Created by feng on 2015/11/2.
 */
public class CollectMessage {

    public CollectMessage(String content, String festivalName, int id) {
        this.content = content;
        this.festivalName = festivalName;
        this.id = id;
    }

    private int id;
    private String festivalName;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public static String TABLE_COLLECT ="collect_message";
    public static String COL_CONTENT="content";
    public static String COL_FESTIVAL_NAME="festival_name";
    public static String COL_ID = "_id";

}
