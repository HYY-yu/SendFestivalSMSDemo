package com.app.feng.sendfestivalsmsdemo.activity;


import android.animation.ObjectAnimator;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.feng.sendfestivalsmsdemo.R;
import com.app.feng.sendfestivalsmsdemo.control.ChooseMessageListViewButtonListener;
import com.app.feng.sendfestivalsmsdemo.control.HttpRequest;
import com.app.feng.sendfestivalsmsdemo.control.MessageProvider;
import com.app.feng.sendfestivalsmsdemo.control.SmsCollectProvider;
import com.app.feng.sendfestivalsmsdemo.element.AutoLoadListView;
import com.app.feng.sendfestivalsmsdemo.element.CollectMessage;
import com.app.feng.sendfestivalsmsdemo.element.Msg;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

public class ChooseMessageActivity extends ActionBarActivity implements AutoLoadListView.OnLoadListener {
    private static int LOADER_ID = 2;
    private AutoLoadListView listView;
    private FloatingActionButton fabToSend;

    public static final String PREFERENCES = "com.app.feng.sendfestivalsmsdemo.UsingPreferences_preferences";

    //这里是腾讯qq和微信使用的变量
    private Tencent tencent;
    private static final String TENCENT_APP_ID = "1104857139";
    public IWXAPI iwxapi;
    private static final String WX_APP_ID = "wxfcb347556fddedfc";

    //将两个按钮的监听事件写到外面去
    private ChooseMessageListViewButtonListener listener;

    private CursorAdapter cursorAdapter;

    private String festivalName;

    private LayoutInflater layoutInflater;

    private HttpRequest httpRequest;

    //记录ListView当前已经访问到第几页了
    private static int index = 1;
    //记录listView上的收藏按钮的索引
    private int btn_index;

    //Loader机制

    private class Myhander extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpRequest.STATUS_ERROR:
                    Toast.makeText(ChooseMessageActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case HttpRequest.INFORMATION:
                    //TODO 在此HttpRequest加载数据完成后会返回 arg1 加载数据的条数
                    if (msg.arg1 == 10) {
                        listView.loadComplete();
                    } else {
                        listView.loadAllData();
                    }
                    break;
            }
        }
    }

    private Handler myHandler = new Myhander();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_message);

        initStatucBar();

        //打开qq分享功能API
        tencent = Tencent.createInstance(TENCENT_APP_ID, this.getApplicationContext());
        //打开微信分享功能API
        iwxapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        Log.i("注册微信", " " + iwxapi.registerApp(WX_APP_ID));
        //发送到监听器上
        listener = new ChooseMessageListViewButtonListener(this, tencent, iwxapi);

        //重置收藏按钮
        btn_index = 0;

        initView();
        initEvent();

        initIntenet();

        initLoader();

        setupAdapter();
    }

    private void initStatucBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置状态栏颜色
            Log.i("SDK_VERSION", "当前版本大于等于19");
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main_color);
        }
    }

    private void setupAdapter() {
        cursorAdapter = new CursorAdapter(this, null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View convertView = layoutInflater.inflate(R.layout.item_message_listview, parent, false);
                return convertView;
            }

            @Override
            public void bindView(View view, Context context, final Cursor cursor) {
                final TextView textView = (TextView) view.findViewById(R.id.listview_textView);
                Button button = (Button) view.findViewById(R.id.listview_button);
                //得到短信内容
                final StringBuilder smsbuilder = new StringBuilder();
                smsbuilder.append(cursor.getString(cursor.getColumnIndex(Msg.MSG_CONTENT)));

                textView.setText("    " + smsbuilder.toString());

                Button btn_share = (Button) view.findViewById(R.id.button_share);
                final ToggleButton btn_star = (ToggleButton) view.findViewById(R.id.button_star);

                btn_share.setOnClickListener(listener);
                btn_share.setTag(smsbuilder.toString());
                btn_star.setOnClickListener(listener);
                // 获得star的点击态
                Bundle bundle = new Bundle();
               /* bundle.putInt("btn_star_id", index);
                Log.i("btn_star_id", index + festivalName + textView.getText().toString());*/
                bundle.putString(CollectMessage.COL_FESTIVAL_NAME, festivalName);
                bundle.putString(CollectMessage.COL_CONTENT, smsbuilder.toString());
                btn_star.setTag(bundle);
                btn_star.post(new Runnable() {
                    @Override
                    public void run() {
                        Cursor cursor1 = getContentResolver().query(SmsCollectProvider.CONTENT_URI, null, CollectMessage.COL_CONTENT
                                + " like '" + smsbuilder.toString().substring(0, 8) + "%'", null, null);
                        if (cursor1.getCount() != 0) {
                            btn_star.setChecked(true);
                        }
                        cursor1.close();
                    }
                });
                /*SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                String isCheck = preferences.getString("btn_ischeck" + index++, String.valueOf(-1));
                if (!isCheck.equals("-1")) {
                    btn_star.setChecked(true);
                }
*/
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendMessageActivity.toSendMessageActivity(ChooseMessageActivity.this, festivalName, textView.getText().toString());
                    }
                });

            }
        };
        listView.setAdapter(cursorAdapter);

        listView.setOnLoadListener(this);
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursorLoader = new CursorLoader(getBaseContext(), MessageProvider.CONTENT_URI_MSG, null, Msg.MSG_FESTIVAL_NAME + " = '" + festivalName + "'", null, null);

                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (loader.getId() == LOADER_ID) {
                    cursorAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                //当Cursor变化
                cursorAdapter.swapCursor(null);
            }
        });
    }

    private void initIntenet() {
        httpRequest = new HttpRequest(this, festivalName, index++, myHandler);
        httpRequest.start();
    }

    private void initEvent() {
        fabToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageActivity.toSendMessageActivity(ChooseMessageActivity.this, festivalName, " ");
            }
        });
    }

    private void initView() {
        listView = (AutoLoadListView) findViewById(R.id.id_atuoloadlv_msg);
        fabToSend = (FloatingActionButton) findViewById(R.id.id_acbtn_msg);

        festivalName = getIntent().getStringExtra("festivalName");

        layoutInflater = LayoutInflater.from(this);

        setTitle(festivalName);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_message, menu);
        final ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.menu_item, null);
        imageView.setImageResource(R.mipmap.icon_menu_refresh);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator.ofFloat(imageView, "rotation", 0F, 360F * 2F).setDuration(800).start();
                //清除数据库中该节日对应的所有数据
                ChooseMessageActivity.this.getContentResolver().delete(MessageProvider.CONTENT_URI_MSG,
                        Msg.MSG_FESTIVAL_NAME + "= ?", new String[]{festivalName});
                //启动HttpRequest重新下载数据
                index = 1;
                initIntenet();
            }
        });
        menu.add("刷新").setActionView(imageView).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean OnLoading() {
        //在此启动HttpRequest来获取数据
        HttpRequest httpRequest = new HttpRequest(this, festivalName, index++, myHandler);
        httpRequest.start();

        return true;
    }

}
