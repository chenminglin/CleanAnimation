package com.chenminglin.cleananimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class CleanCircleRippleView extends View {

    final String TAG = getClass().getSimpleName();

    float centerX;
    float centerY;

    float mRippleMaxRadius;
    float mRippleInterval;
    float mRippleRadiusDecrement;
    final int RIPPLES_SIZE = 5;

    Paint mPaint;

    List<CircleRipple> ripples = new ArrayList<>();

    float duration = 1000f;

    long maxprogress = 100;


    public CleanCircleRippleView(Context context) {
        super(context);
        init(null, 0);
    }

    public CleanCircleRippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CleanCircleRippleView(Context context, AttributeSet attrs, int defStyle) {
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
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setAntiAlias(true);
    }

    private void initRipple() {
        ripples.clear();
        for (int n = 0; n <= RIPPLES_SIZE - 1; n++) {
            CircleRipple ripple = new CircleRipple();
            ripple.radius = n * (int) mRippleInterval;
            ripple.initRadius = n * (int) mRippleInterval;
            ripples.add(ripple);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;

        mRippleMaxRadius = w / 2f * (4 / 5f);
        mRippleInterval = mRippleMaxRadius / RIPPLES_SIZE + 1;
        mRippleRadiusDecrement = mRippleInterval / maxprogress;

        initRipple();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);
        drawRipple(canvas);
    }

    private void drawRipple(Canvas canvas) {
        for (int n = 3; n < ripples.size(); n++) {
            CircleRipple ripple = ripples.get(n);
            float alpha = ripple.radius / mRippleMaxRadius;
            int alphaInt = (int) (255 * 0.3f * (1 - alpha));
//            Log.d(TAG, "ripple.radius = " + ripple.radius + "alphaInt = " + alphaInt);
            if (alphaInt < 0) {
                mPaint.setAlpha(0);
            } else {
                mPaint.setAlpha(alphaInt);
            }
            canvas.drawCircle(0, 0, ripple.radius, mPaint);
        }
    }

    public void setProgress(int progress) {
        for (CircleRipple ripple : ripples) {
            ripple.radius = (int) (ripple.initRadius + (progress * mRippleRadiusDecrement));
        }
        postInvalidate();
    }


    public void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(1, (int) maxprogress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
//                Log.d(TAG, "value =  " + value);
                setProgress(value);
            }
        });
        animator.setDuration((long) duration);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }


    class CircleRipple {
        public int radius;
        public int initRadius;
    }
}
