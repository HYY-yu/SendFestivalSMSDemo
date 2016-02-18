package com.app.feng.sendfestivalsmsdemo.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.feng.sendfestivalsmsdemo.R;
import com.app.feng.sendfestivalsmsdemo.control.MessageSend;
import com.app.feng.sendfestivalsmsdemo.element.FlowLayout;
import com.app.feng.sendfestivalsmsdemo.element.HistoryMessage;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActivity extends ActionBarActivity {

    private static final int SEND_CONTENT_REQUEST = 101;
    private String festivalName;
    private String message;

    private EditText editText;
    private Button btn_addContext;
    private FloatingActionButton btn_send;
    private FlowLayout flowLayout;
    private View mLayoutLoading;

    //所有需要发送的联系人
    private List<String> contactNames = new ArrayList<>();
    //所有需要发送的号码集
    private List<String> contactNumbers = new ArrayList<>();

    //用户选择的联系人
    private String contactName;
    //该联系人下的号码
    private String contactNumber;

    private LayoutInflater inflater;

    //用于发送短信
    private MessageSend messageSend;

    //监听短信发送状态参数
    public static final String SENT = "SMS_SENT";
    public static final String DELIVERY = "SMS_DELIVERED";
    private PendingIntent sendPI, deliveryPI;
    private BroadcastReceiver smsSendReceiver, smsDeliveryReceiver;

    //短信数
    private int totalCount;
    private int nowSendCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        initStatusBar();

        initView();
        initData();
        initEvent();
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置状态栏颜色
            Log.i("SDK_VERSION", "当前版本大于等于19");
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main_color);
        }
    }

    private void initEvent() {
        btn_addContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开系统的联系人
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, SEND_CONTENT_REQUEST);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用SendMessage
                if (contactNumbers.size() == 0) {
                    Toast.makeText(getBaseContext(), "请选择联系人", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = editText.getText().toString().trim();
                Log.i("msg", msg);
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(getBaseContext(), "短信内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //暂时中断界面
                mLayoutLoading.setVisibility(View.VISIBLE);
                //测试
                /*for (String n : contactNumbers
                        ) {
                    Log.i("PhoneNumbers", n);
                }
                */
                totalCount = messageSend.sendMoreMessage(contactNumbers, buildHistroyMessage(msg), sendPI, deliveryPI);
                nowSendCount = 0;
            }

        });
    }

    private HistoryMessage buildHistroyMessage(String msg) {
        HistoryMessage message = new HistoryMessage();
        message.setMsg(msg);
        message.setFestivalName(festivalName);
        StringBuilder names = new StringBuilder();
        for (String name : contactNames
                ) {
            names.append(name + ":");
        }

        message.setName(names.substring(0, names.length() - 1));
        StringBuilder numbers = new StringBuilder();
        for (String num : contactNames
                ) {
            numbers.append(num + ":");
        }

        message.setName(numbers.substring(0, numbers.length() - 1));
        message.setNumber(names.substring(0, names.length() - 1));

        return message;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //当联系人界面退出（返回选择的联系人数据）
        if (requestCode == SEND_CONTENT_REQUEST && resultCode == RESULT_OK) {
            //获取联系人姓名
            Uri contacturi = data.getData();
            contactNumber = null;
            //获取游标
            CursorLoader cursorLoader = new CursorLoader(this, contacturi, null, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            cursor.moveToFirst();
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contactNames.add(contactName);
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            //获取电话号码
            int hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (hasPhone > 0) {
                cursorLoader = new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        , null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                final Cursor phoneCursor = cursorLoader.loadInBackground();
                phoneCursor.moveToFirst();
                if (phoneCursor.getCount() > 1) {
                    //多个号码提示用户选择一个

                    //打开一个Dialog提示用户选择
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("选择号码 " + contactName);
                    phoneCursor.moveToPrevious();
                    //第三个参数说明的是要显示游标的第几列 不能省略
                    builder.setSingleChoiceItems(phoneCursor, -1, ContactsContract.CommonDataKinds.Phone.NUMBER, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            phoneCursor.moveToFirst();
                            switch (which) {
                                case 0:
                                    contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    break;
                                case 1:
                                    phoneCursor.moveToNext();
                                    contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    break;
                                case 2:
                                    phoneCursor.moveToNext();
                                    phoneCursor.moveToNext();
                                    contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    break;
                            }
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            contactNumbers.add(contactNumber);
                            setOnFlowLayout();
                            phoneCursor.close();
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();

                } else {
                    //只有一个号码 直接发送
                    contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    phoneCursor.close();
                }
                cursor.close();
            }

            //根据联系人在FlowLayout中显示
            if (!TextUtils.isEmpty(contactNumber)) {
                contactNumbers.add(contactNumber);
                setOnFlowLayout();
            }

        }
    }

    private void setOnFlowLayout() {
        TextView textView = (TextView) inflater.inflate(R.layout.item_flowlayout, flowLayout, false);
        textView.setText(contactName);
        Log.i("add", contactNumber);
        //测试
        /*if (contactNumbers.size() > 0) {
            for (String n : contactNumbers
                    ) {
                Log.i("PhoneNumbers", n);
            }
        }*/
        textView.setTag(contactNumber);
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_delete2);
        drawable.setBounds(3, 3, 30, 30);
        textView.setCompoundDrawables(null, null, drawable, null);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("remove", v.getTag().toString());
                //测试
                /*if (contactNumbers.size() > 0) {
                    for (String n : contactNumbers
                            ) {
                        Log.i("PhoneNumbers", n);
                    }
                }*/
                contactNames.remove(((TextView) v).getText().toString());
                Log.i("removeSuccess", "" + contactNumbers.remove(v.getTag().toString()));
                flowLayout.removeView(v);
            }
        });
        ObjectAnimator.ofFloat(textView, "translationY", 100F, 0F).setDuration(1000).start();
        ObjectAnimator.ofFloat(textView, "alpha", 0F, 1F).setDuration(1000).start();
        flowLayout.addView(textView);

    }

    private void initData() {
        festivalName = getIntent().getStringExtra("festivalName");
        message = getIntent().getStringExtra("message");

        //在此设置界面中控件显示的文本信息
        editText.setText("    " + message);

        setTitle(festivalName);
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.id_send_edittext);
        btn_addContext = (Button) findViewById(R.id.id_send_addperson);
        btn_send = (FloatingActionButton) findViewById(R.id.id_acbtn_send);
        flowLayout = (FlowLayout) findViewById(R.id.id_send_flowlayout);
        mLayoutLoading = findViewById(R.id.id_send_layout_loading);

        mLayoutLoading.setVisibility(View.GONE);

        inflater = LayoutInflater.from(this);

        //设置发送短信的PendingIntent
        sendPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveryPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERY), 0);

        messageSend = new MessageSend(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //创建短信监听器
        smsSendReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                nowSendCount++;
                if (getResultCode() == RESULT_OK) {
                    Toast.makeText(getBaseContext(), nowSendCount + "/" + totalCount + "短信发送成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getBaseContext(), nowSendCount + "/" + totalCount + "短信发送失败", Toast.LENGTH_SHORT).show();
                }

                if (nowSendCount == totalCount) {
                    finish();
                }
            }
        };
        smsDeliveryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == RESULT_OK) {
                    Log.i("短信发送确认", nowSendCount + "/" + totalCount + "对方已接收");
                } else {
                    Log.i("短信发送确认", nowSendCount + "/" + totalCount + "对方未接收");
                }
            }
        };

        //注册监听器
        registerReceiver(smsSendReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveryReceiver, new IntentFilter(DELIVERY));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //注销监听器
        unregisterReceiver(smsSendReceiver);
        unregisterReceiver(smsDeliveryReceiver);
    }

    //使用此方法能快速的创建SendMessageActivity
    public static void toSendMessageActivity(Context context, String festivalName, String message) {
        Intent intent = new Intent(context, SendMessageActivity.class);
        intent.putExtra("festivalName", festivalName);
        intent.putExtra("message", message);
        context.startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
