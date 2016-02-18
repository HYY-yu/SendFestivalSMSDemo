package com.app.feng.changecoloriconview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by feng on 2015/10/31.
 */
public class ChangeColorIconView extends View {

    //自定义的属性
    private String text;
    private Bitmap icon;
    private int color;
    private int textSize;

    //用于绘制View的工具
    private Canvas canvas;
    //用于绘制纯色
    private Bitmap bitmap;

    private Paint textPaint;
    private Paint paint;

    private float alpha;

    //icon text 位置
    private Rect iconRect;
    private Rect textRect;


    public ChangeColorIconView(Context context) {
        this(context, null);
    }

    public ChangeColorIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIconView);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.ChangeColorIconView_mainicon) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) array.getDrawable(attr);
                icon = bitmapDrawable.getBitmap();

            } else if (attr == R.styleable.ChangeColorIconView_bgcolor) {
                color = array.getColor(attr, Color.BLUE);

            } else if (attr == R.styleable.ChangeColorIconView_text) {
                text = array.getString(attr);
            } else if (attr == R.styleable.ChangeColorIconView_textSize) {
                textSize = (int) array.getDimension(attr, TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()
                ));
            }
        }
        array.recycle();

        //设置绘制
        textRect = new Rect();
        iconRect = new Rect();
        textPaint = new Paint();

        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.parseColor("#8E9196"));
        textPaint.setAntiAlias(true);
        textPaint.getTextBounds(text, 0, text.length(), textRect);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - textRect.height());

        int iconLeft = getMeasuredWidth() / 2 - iconWidth / 2;
        int iconTop = getMeasuredHeight() / 2 - (textRect.height() + iconWidth) / 2;
        //实测图标太大，因此减10
        iconWidth -= 10;

        iconRect.set(iconLeft + 10, iconTop + 10, iconLeft + iconWidth, iconTop + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(icon, null, iconRect, null);
        //绘制纯色Bitmap setAlpha,纯色,Xfermode,图标
        int alp = (int) Math.ceil(255 * alpha);
        setupBitmap(alp);

        //1、不变色文本
        textPaint.setColor(Color.parseColor("#8E9196"));
        textPaint.setAlpha(255 - alp);
        int x = getMeasuredWidth() / 2 - textRect.width() / 2;
        int y = iconRect.bottom + textRect.height();
        canvas.drawText(text, x, y, textPaint);
        //2、变色文本
        textPaint.setColor(color);
        textPaint.setAlpha(alp);
        canvas.drawText(text, x, y, textPaint);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    public void setIconAlpha(float alpha1) {
        this.alpha = alpha1;
        //设置完透明度，重绘View，这时可能在UI线程也可能在非UI线程
        invalidateView();
    }

    private void invalidateView() {
        //是否是UI线程
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private void setupBitmap(int alp) {

        bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(alp);

        canvas.drawRect(iconRect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setAlpha(255);
        canvas.drawBitmap(icon, null, iconRect, paint);
    }

    /**
     * 在我们自定义View的时候，可能需要记录一些View的状态信息，可以记录在这个方法中
     *
     * @return
     */
    //这是防止我们重写Save方法把系统的Save方法覆盖掉了，用这个变量记录一些系统的东西
    private static final String PARENT_STATUS = "parent_status";
    //记录自己需要的变量
    private static final String STATUS_ALPHA = "status_alpha";


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARENT_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA, alpha);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(PARENT_STATUS));
            alpha = bundle.getFloat(STATUS_ALPHA);
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
