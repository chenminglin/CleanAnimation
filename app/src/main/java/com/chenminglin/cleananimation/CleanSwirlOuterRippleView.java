package com.chenminglin.cleananimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CleanSwirlOuterRippleView extends View {
    final String TAG = getClass().getSimpleName();
    float centerX;
    float centerY;
    Paint mPaint;

    int mOuterCircleColor = Color.parseColor("#40FFFFFF");

    float mCenterCircleRadius;

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
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CleanCircleRippleView, defStyle, 0);

        a.recycle();

        // Set up a default TextPaint object
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mOuterCircleColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;
        mCenterCircleRadius = w / 2 * (2.8f / 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);
        canvas.drawCircle(0, 0, mCenterCircleRadius, mPaint);
    }
}
