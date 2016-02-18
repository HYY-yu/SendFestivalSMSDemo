package com.app.feng.sendfestivalsmsdemo.element;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {


    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content
        int width = 0;
        int height = 0;

        //得到每行的宽高
        int lineWidth = 0;
        int lineHeight = 0;

        //得到子控件个数
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //测量子View的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到LayoutParams,这个Params就是generateLayoutParams方法中设置的
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

            //得到子控件宽高
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                //换行
                width = Math.max(width, lineWidth);
                height += lineHeight;

                //重置此行
                lineWidth = childWidth;
                lineHeight = childHeight;

            } else {

                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == childCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;

            }
        }

        Log.i("Log", "sizeWidth " + sizeWidth);
        Log.i("Log", "sizeHeight " + sizeHeight);

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : (width + getPaddingLeft() + getPaddingRight())
                , modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        MarginLayoutParams lp = new MarginLayoutParams(getContext(), attrs);
        lp.setMargins(10, 10, 10, 10);
        return lp;
    }

    //所有子View
    private List<List<View>> mAllView = new ArrayList<>();
    //记录每行的高度
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //防止多次调用使用过时数据
        mAllView.clear();
        mLineHeight.clear();

        //当前FlowLayout的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        //拿到子View的个数
        List<View> lineView = new ArrayList<>();

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);

            MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();

            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            //如果宽度不够
            if (lineWidth + childWidth >
                    width - getPaddingLeft() - getPaddingRight()) {
                //记录LineHeight
                mLineHeight.add(childHeight);
                //记录当前行的View
                mAllView.add(lineView);
                lineWidth = 0;
                lineHeight = childHeight + layoutParams.topMargin + layoutParams.bottomMargin;

                lineView = new ArrayList<>();
            }

            lineWidth += childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight
                    + layoutParams.topMargin + layoutParams.bottomMargin);

            lineView.add(view);
        }

        //最后一行
        mLineHeight.add(lineHeight);
        mAllView.add(lineView);

        //设置子View位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNum = mAllView.size();

        for (int i = 0; i < lineNum; i++) {
            lineView = mAllView.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineView.size(); j++) {
                View view = lineView.get(j);
                if (view.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();

                int lc = left + layoutParams.leftMargin;
                int rc = lc + view.getMeasuredWidth();
                int tc = top + layoutParams.topMargin;
                int bc = tc + view.getMeasuredHeight();
                view.layout(lc, tc, rc, bc);

                left += view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
    }
}
