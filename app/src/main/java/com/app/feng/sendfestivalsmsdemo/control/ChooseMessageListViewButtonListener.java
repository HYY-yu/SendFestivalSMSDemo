package com.app.feng.sendfestivalsmsdemo.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.feng.sendfestivalsmsdemo.R;
import com.app.feng.sendfestivalsmsdemo.element.CollectMessage;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * Created by feng on 2015/10/29.
 */
public class ChooseMessageListViewButtonListener implements View.OnClickListener {

    Tencent mTencent;
    IWXAPI iwxapi;
    Activity chooseMessageActivity;

    LayoutInflater inflater;

    private ShareButtonListener shareButtonListener = new ShareButtonListener();


    public ChooseMessageListViewButtonListener(Activity activity, Tencent t, IWXAPI iwxapi) {
        chooseMessageActivity = activity;
        mTencent = t;
        inflater = LayoutInflater.from(chooseMessageActivity);
        this.iwxapi = iwxapi;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_share:
                ClickButtonShare(v);
                break;
            case R.id.button_star:
                ClickButtonStar(v);
                break;
        }
    }

    private void ClickButtonStar(View v) {
        //先强制转换成ToggleButton
        ToggleButton temp = (ToggleButton) v;
        if (temp.isChecked()) {
            //读取button中的信息
            Bundle bundle = (Bundle) temp.getTag();
            ContentValues values = new ContentValues();
            values.put(CollectMessage.COL_FESTIVAL_NAME, bundle.getString(CollectMessage.COL_FESTIVAL_NAME));
            values.put(CollectMessage.COL_CONTENT, bundle.getString(CollectMessage.COL_CONTENT));
            //加入数据库前应先判断数据库中有没有改短信
            Cursor cursor1 = chooseMessageActivity.getContentResolver().query(SmsCollectProvider.CONTENT_URI, null, CollectMessage.COL_CONTENT
                    + " like '" + bundle.getString(CollectMessage.COL_CONTENT).substring(0, 8) + "%'", null, null);
            if (cursor1.getCount() != 0) {
                //有这条短信
                Toast.makeText(chooseMessageActivity, "这条短信已存在", Toast.LENGTH_SHORT);
                temp.setChecked(true);
                return;
            }
            chooseMessageActivity.getContentResolver().insert(SmsCollectProvider.CONTENT_URI, values);
            Toast.makeText(chooseMessageActivity, "已加入收藏", Toast.LENGTH_SHORT).show();
            temp.setChecked(true);
        } else {
            //删除
            Toast.makeText(chooseMessageActivity, "已取消收藏", Toast.LENGTH_SHORT).show();
            Bundle bundle = (Bundle) temp.getTag();
            String someMsg = bundle.getString(CollectMessage.COL_CONTENT).substring(0, 8);
            chooseMessageActivity.getContentResolver().delete(SmsCollectProvider.CONTENT_URI,
                    CollectMessage.COL_CONTENT + " like '" + someMsg + "%'", null);
            temp.setChecked(false);
        }
    }

    private void ClickButtonShare(View v) {
        String content = (String) v.getTag();
        //打开分享对话框
       /* Log.i("Button Tag", content);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "节日祝福");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chooseMessageActivity.startActivity(Intent.createChooser(intent,"发送到："));*/

        AlertDialog.Builder builder = new AlertDialog.Builder(chooseMessageActivity);
        builder.setTitle("发送到");
        View share = inflater.inflate(R.layout.share_dialog, null);
        Button share_qq = (Button) share.findViewById(R.id.button_qq);
        Button share_weixin = (Button) share.findViewById(R.id.button_weixin);
        Button share_qzone = (Button) share.findViewById(R.id.button_qzone);
        Button share_pengyou = (Button) share.findViewById(R.id.button_pengyou);
        share_qq.setOnClickListener(shareButtonListener);
        share_qzone.setOnClickListener(shareButtonListener);
        share_weixin.setOnClickListener(shareButtonListener);
        share_pengyou.setOnClickListener(shareButtonListener);
        //设置要发送的文本
        shareButtonListener.setText(content);

        builder.setView(share);
        builder.show();
    }

    class ShareButtonListener implements View.OnClickListener {
        private String text;

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_qq:
                    final Bundle params = new Bundle();
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                    params.putString(QQShare.SHARE_TO_QQ_TITLE, "节日祝福");
                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, text.substring(0, 40));
                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.aizhufu.com/");
                    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
                    mTencent.shareToQQ(chooseMessageActivity, params, new IUiListener() {
                        @Override
                        public void onComplete(Object o) {

                        }

                        @Override
                        public void onError(UiError uiError) {
                            Toast.makeText(chooseMessageActivity, uiError.errorMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                case R.id.button_qzone:
                    final Bundle params1 = new Bundle();
                    params1.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                    params1.putString(QzoneShare.SHARE_TO_QQ_TITLE, "节日祝福");//必填
                    params1.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, text);//选填
                    params1.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.aizhufu.cn");//必填
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("http://img.ptcms.csdn.net/article/201510/29/56318ad09564f_middle.jpg?_=36631");//必填
                    params1.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, arrayList);
                    mTencent.shareToQzone(chooseMessageActivity, params1, new IUiListener() {
                        @Override
                        public void onComplete(Object o) {

                        }

                        @Override
                        public void onError(UiError uiError) {
                            Toast.makeText(chooseMessageActivity, uiError.errorMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    break;

                case R.id.button_weixin:
                    if (TextUtils.isEmpty(text)) {
                        return;
                    }
                    WXTextObject wxTextObject = new WXTextObject();
                    wxTextObject.text = text;
                    WXMediaMessage msg = new WXMediaMessage();
                    msg.title = "节日祝福";
                    msg.mediaObject = wxTextObject;
                    msg.description = text.substring(0, 20).concat("...");

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSession;

                    Log.i("isArgs", req.checkArgs() + "");
                    iwxapi.sendReq(req);
                    break;

                case R.id.button_pengyou:
                    if (TextUtils.isEmpty(text)) {
                        return;
                    }
                    WXTextObject wxTextObject2 = new WXTextObject();
                    wxTextObject2.text = text;
                    WXMediaMessage msg1 = new WXMediaMessage();
                    msg1.mediaObject = wxTextObject2;
                    msg1.description = text.substring(0, 20).concat("...");

                    SendMessageToWX.Req req1 = new SendMessageToWX.Req();
                    req1.transaction = String.valueOf(System.currentTimeMillis());
                    req1.message = msg1;
                    req1.scene = SendMessageToWX.Req.WXSceneTimeline;

                    Log.i("isArgs", req1.checkArgs() + "");
                    iwxapi.sendReq(req1);
                    break;
            }
        }
    }
}
