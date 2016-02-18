package com.app.feng.sendfestivalsmsdemo.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.app.feng.sendfestivalsmsdemo.element.Festival;
import com.app.feng.sendfestivalsmsdemo.element.Msg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2015/10/26.
 */
public class HttpRequest extends Thread {

    //web短信抓取地址
    private static final String AIZHUFU_URL = "http://www.aizhufu.cn/duanxinku/column/";

    //哪个节日
    private String festivalName;
    //节日编号
    private Integer festivalNum;

    //默认获得第一页数据，可以更改获取后面页的数据
    private int indexNum = 1;

  /*  //封装在该类中的数据结构存储了节日名和节日标号的映射关系
    private FestivalDataHelper dateHelper = new FestivalDataHelper();*/

    private Context context;
    //更新UI
    private Handler handler;
    //用来配合Handler更新UI
    private Message message;

    //解析HTML 得到的短信列表
    private List<Msg> tempMsgs = new ArrayList<>();

    //解析HTML用的字符串
    private String htmlString = "<span class=\"w2 readContent\" original-title='";

    //这些常量赋值给arg1表示该信息的类型
    public static final int STATUS_ERROR = -1;
    public static final int INFORMATION = 0;

    public HttpRequest(Context context, String festivalName, int indexNum, Handler handler) {
        this.context = context;
        this.festivalName = festivalName;
        this.indexNum = indexNum;
        this.handler = handler;
        message = handler.obtainMessage();
        Cursor c = context.getContentResolver().query(MessageProvider.CONTENT_URI_FESTIVAL, null,
                Festival.FESTIVAL_NAME + "= '" + festivalName + "'", null, null);
        c.moveToFirst();
        this.festivalNum = Integer.parseInt(c.getString(c.getColumnIndex(Festival.FESTIVAL_CODE)).trim());
        c.close();

    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }

    @Override
    public void run() {
        //开始拼装网址数据
        String u = AIZHUFU_URL + festivalNum + "/" + indexNum + ".html";
        URL url;
        try {
            url = new URL(u);
            Log.i("URL", u);
            //打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            //解析HTML文件
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str;
            while ((str = reader.readLine()) != null) {
                Log.i("WEB_PAGE", str);

                if (str.trim().startsWith(htmlString)) {

                    String content = str.substring(htmlString.length() + 2,
                            str.lastIndexOf("'>"));
                    Log.i("找到了", content);
                    Msg msg = new Msg(1, content, festivalName);
                    tempMsgs.add(msg);
                }
            }
            //添加到数据库中
            for (Msg m :
                    tempMsgs) {
                ContentValues values = new ContentValues();
                values.put(Msg.MSG_CONTENT, m.getContent());
                values.put(Msg.MSG_FESTIVAL_NAME, m.getFestivalName());
                //如果数据库中没有
                Cursor c = context.getContentResolver().query(MessageProvider.CONTENT_URI_MSG, null,
                        Msg.MSG_CONTENT + " like '" + m.getContent().substring(0, 5) + "%'", null, null);
                if (c.getCount() <= 0)
                    context.getContentResolver().insert(MessageProvider.CONTENT_URI_MSG, values);
                c.close();
            }

            message.what = INFORMATION;
            message.obj = "成功刷新";
            message.arg1 = tempMsgs.size();
            handler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
            //通知Handler弹出错误提示窗口
            message.what = STATUS_ERROR;
            message.obj = "服务器未响应";
            handler.sendMessage(message);
        }
    }
}
