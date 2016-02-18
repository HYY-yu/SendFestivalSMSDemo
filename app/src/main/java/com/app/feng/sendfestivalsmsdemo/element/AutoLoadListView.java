package com.app.feng.sendfestivalsmsdemo.element;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.feng.sendfestivalsmsdemo.R;

/**
 * 上部也可以下拉查看（下拉刷新也可以实现）
 * 此listview下拉到底部能自动加载数据
 * Created by feng on 2015/10/27.
 */
public class AutoLoadListView extends ListView implements AbsListView.OnScrollListener {

    //底部的View
    private View bottomView;
    //顶部的View
    private View topView;

    //顶部View的高度
    private int topViewHeight;

    //共有多少个Item
    private int totalItem;
    //最后一个Item的编号
    private int lastItem;
    //第一个可视Item 用于判断是否到了列表最顶端
    private int firstItem;

    //如果正在加载，就不能在调用OnLoad
    private boolean isLoading;
    //是否是在listview的最顶端
    private boolean isTop;


    //用于加载顶部和底部的View
    private LayoutInflater inflater;
    //接口引用
    private OnLoadListener onLoadListener;

    /**
     * 以下的变量用于ListView实现下拉刷新是使用
     */
    private final int NONE = 0;//正常
    private final int PULL = 1;//下拉
    private final int MAX = 2;//拉到最大值
    private int state = NONE;//记录当前状态

    private int startY;//记录Y的偏移

    //OnLoadListener接口
    public interface OnLoadListener {
        /**
         * 异步加载
         *
         * @return 如果加载失败，返回False(当数据加载完成时，不看做加载失败)
         */
        boolean OnLoading();
    }

    public AutoLoadListView(Context context) {
        this(context, null);
    }

    public AutoLoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        bottomView = inflater.inflate(R.layout.item_listview_loading, null);
        topView = inflater.inflate(R.layout.item_listview_refreshing, null);

        bottomView.setVisibility(View.GONE);

        measureView(topView);
        topViewHeight = topView.getMeasuredHeight();
        setTopViewTopPadding(-topViewHeight);

        this.addHeaderView(topView);
        this.addFooterView(bottomView);
        this.setOnScrollListener(this);
    }

    /**
     * 通知父布局 view的尺寸
     *
     * @param view
     */
    public void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height = 0;
        if (params.height > 0) {
            height = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        view.measure(width, height);

    }

    /**
     * 设置TopView的上边距
     *
     * @param topPadding
     */
    public void setTopViewTopPadding(int topPadding) {
        topView.setPadding(topView.getPaddingLeft(), topPadding,
                topView.getPaddingRight(), topView.getPaddingBottom());
        topView.invalidate();
    }

    public void loadComplete() {
        bottomView.setVisibility(GONE);
        isLoading = false;
    }

    /**
     * 已经加载完所有的数据调用此方法
     */
    public void loadAllData() {
        ProgressBar progressBar = (ProgressBar) bottomView.findViewById(R.id.progressBar_loading);
        TextView textView = (TextView) bottomView.findViewById(R.id.textView_loading);
        progressBar.setVisibility(View.GONE);
        textView.setText("已加载完所有数据");
        isLoading = false;
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        onLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totalItem == lastItem && scrollState == SCROLL_STATE_IDLE) {
            Log.i("listview", "Loading");
            if (!isLoading) {
                isLoading = true;
                bottomView.setVisibility(View.VISIBLE);
                onLoadListener.OnLoading();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        totalItem = totalItemCount;
        lastItem = firstVisibleItem + visibleItemCount;
        firstItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstItem == 0) {
                    //当前确实是在列表的最顶端
                    isTop = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMoveing(ev);
                break;
            case MotionEvent.ACTION_UP:
                isTop = false;
                state = NONE;
                setTopViewTopPadding(-topViewHeight);
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void onMoveing(MotionEvent event) {
        if (isTop) {

            int tempY = (int) event.getY();
            int space = tempY - startY;
            int topPadding = space - topViewHeight;

            switch (state) {
                case NONE:
                    if (space > 0) {
                        state = PULL;
                    }

                    break;
                case PULL:
                    setTopViewTopPadding(topPadding);
                    if (space >= topViewHeight) {
                        state = MAX;
                    } else if (space <= 0) {
                        state = NONE;
                    }
                    break;
                case MAX:
                    //setTopViewTopPadding(topViewHeight);
                    if (space < topViewHeight) {
                        state = PULL;
                    }
                    break;

                default:

            }


        } else

        {
            return;
        }
    }
}
