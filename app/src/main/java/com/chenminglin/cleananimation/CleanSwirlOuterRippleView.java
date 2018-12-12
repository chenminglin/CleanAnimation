package com.chenminglin.cleananimation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * 旋转动画外部圆圈，此类本身没有动画，只是画了一个圆圈，动画由外部view控制
 */
public class CleanSwirlOuterRippleView extends View {
    final String TAG = getClass().getSimpleName();
    float centerX;
    float centerY;
    Paint mPaint;

    int mOuterCircleColor = Color.parseColor("#40FFFFFF");

    float mCenterCircleRadius;

    Paint mCenterCirclePaint;
    float mCenterTransCircleRadius;

    public CleanSwirlOuterRippleView(Context context) {
        super(context);
        init(null, 0);
    }

    public CleanSwirlOuterRippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CleanSwirlOuterRippleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes


        // Set up a default TextPaint object
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mOuterCircleColor);
        mPaint.setAntiAlias(true);

        mCenterCirclePaint = new Paint();
        mCenterCirclePaint.setAntiAlias(true);
        mCenterCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;
        mCenterCircleRadius = w / 2 * (2.8f / 5);

        mCenterTransCircleRadius = w / 2 * (2.4f / 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);
        canvas.drawCircle(0, 0, mCenterCircleRadius, mPaint);
        canvas.drawCircle(0, 0, mCenterTransCircleRadius, mCenterCirclePaint);
    }
}
