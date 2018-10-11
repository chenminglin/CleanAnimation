package com.chenminglin.cleananimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 奖杯外圆圈的浮动效果，刚开始的策略时显示五个，每面发现去掉里面几个就可以。
 */
public class CleanCircleRippleView extends View {

    final String TAG = getClass().getSimpleName();
    //中心x坐标
    float centerX;
    //中心y坐标
    float centerY;
    //扩散最大半径
    float mRippleMaxRadius;
    //每个圆圈的间距
    float mRippleInterval;
    //圆圈向外扩散递增幅度
    float mRippleRadiusDecrement;
    final int RIPPLES_SIZE = 5;

    Paint mPaint;

    List<CircleRipple> ripples = new ArrayList<>();

    float duration = 1000f;

    long maxprogress = 100;

    ValueAnimator animator;

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


        // Set up a default TextPaint object
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
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
        //里面几个不需要显示
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
        animator = ValueAnimator.ofInt(1, (int) maxprogress);
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

    public void cancelAnimation() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    public class CircleRipple {
        public int radius;
        public int initRadius;
    }
}
