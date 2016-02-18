package com.app.feng.sendfestivalsmsdemo.control;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import com.app.feng.sendfestivalsmsdemo.element.HistoryMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 使用该类发送短信息
 * Created by feng on 2015/10/23.
 */
public class MessageSend {

    private Context context;

    public MessageSend(Context context) {
        this.context = context;
    }

    public int sendMessage(String phoneNumber, String content, PendingIntent sending, PendingIntent deliverySuccess) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> msgs = smsManager.divideMessage(content);
        Log.i("msgs count", " " + msgs.size());

        for (String msg : msgs
                ) {
            if (msg != null)
                Log.i("msg in msgs", msg);
            smsManager.sendTextMessage(phoneNumber, null, msg, sending, deliverySuccess);
        }
        return msgs.size();
    }

    public int sendMoreMessage(List<String> phoneNumbers, HistoryMessage historyMessage, PendingIntent sending, PendingIntent deliverySuccess) {

        //保存此信息
        save(historyMessage);
        //发送此信息
        int all = 0;
        for (String num : phoneNumbers
                ) {
            int count = sendMessage(num, historyMessage.getMsg(), sending, deliverySuccess);
            all += count;
        }
        return all;
    }

    /**
     * 此方法沟通Provider 操作其进行增删改查，本应该写在新一个类中，
     * 本不应该写在MessageSend类中，此处简化处理，因为只涉及插入操作.
     * 后期可扩展。
     *
     * @param historyMessage 需保存的历史信息
     */
    public void save(HistoryMessage historyMessage) {
        historyMessage.setDate(new Date());
        ContentValues values = new ContentValues();
        values.put(HistoryMessage.COLUMN_MSG, historyMessage.getMsg());
        values.put(HistoryMessage.COLUMN_DATE, historyMessage.getDateStr());
        values.put(HistoryMessage.COLUMN_NAME, historyMessage.getName());
        values.put(HistoryMessage.COLUMN_NUMBER, historyMessage.getNumber());
        values.put(HistoryMessage.COLUMN_FESTIVAL_NAME, historyMessage.getFestivalName());

        context.getContentResolver().insert(SmsHistoryProvider.CONTENT_URI, values);
    }


}
