package com.chenminglin.cleananimation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class CleanAnimationView extends View {

    final String TAG = getClass().getSimpleName();

    final int DEFAULT_COLOR = Color.parseColor("#32BD7B");
    final float OVAL1_DEFAULT_DEGREES = -15F;
    final float OVAL2_DEFAULT_DEGREES = -60F;
    final float OVAL3_DEFAULT_DEGREES = -135F;

    final int BUBBLE_DEFAULT_NUM = 50;

    float mBubbleMaxRadius = 0;
    float mBubbleMinRadius = 0;
    float mBubbleMinX = 0;
    float mBubbleMaxX = 0;
    float mBubbleMinY = 0;
    float mBubbleMaxY = 0;

    float mBubbleMinCenterDistance;
    float mBubbleMaxCenterDistance;
    int mBubbleNum;

    List<CleanBubble> mBubbles = new ArrayList<>();

    ValueAnimator mAnimator;


    Paint mPaint;

    int mCenterW;
    int mCenterY;

    int mOuterCircleColor = Color.parseColor("#30FFFFFF");
    float mOuterCircleRadius;

    int mOval1Color = Color.parseColor("#43FFFFFF");
    RectF mOval1 = new RectF();
    float mOval1Degrees = OVAL1_DEFAULT_DEGREES;

    int mOval2Color = Color.parseColor("#F2FFFFFF");
    RectF mOval2 = new RectF();
    float mOval2Degrees = OVAL2_DEFAULT_DEGREES;

    RectF mOval3 = new RectF();
    float mOval3Degrees = OVAL3_DEFAULT_DEGREES;

    float mCenterCircleRadius;
    int mCenterCircleColor;

    float mBubbleCanvasDegrees;

    int mMaxDecrement;
    int mMinDecrement;



    public CleanAnimationView(Context context) {
        super(context);
        init(null, 0);


    }

    public CleanAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

    }

    public CleanAnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CleanAnimationView, defStyle, 0);


        mCenterCircleColor = a.getColor(R.styleable.CleanAnimationView_themeColor, DEFAULT_COLOR);
        setBackgroundColor(mCenterCircleColor);

        mBubbleNum = a.getInt(R.styleable.CleanAnimationView_bubbleNum, BUBBLE_DEFAULT_NUM);

        if (mBubbleNum > BUBBLE_DEFAULT_NUM) {
            mBubbleNum = BUBBLE_DEFAULT_NUM;
        }

        a.recycle();

        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterW = w / 2;
        mCenterY = h / 2;

        mOuterCircleRadius = w / 2 * (3.8f / 5);

        float oval1Left = w / 2 * (3f / 5);
        float oval1Top = w / 2 * (2.5f / 5);
        mOval1.set(-oval1Left, -oval1Top, oval1Left, oval1Top);

        float oval2Left = w / 2 * (2.8f / 5);
        float oval2Top = w / 2 * (2.5f / 5);
        mOval2.set(-oval2Left, -oval2Top, oval2Left, oval2Top);

        float oval3Left = w / 2 * (2.6f / 5);
        float oval3Top = w / 2 * (2.5f / 5);
        mOval3.set(-oval3Left, -oval3Top, oval3Left, oval3Top);

        mCenterCircleRadius = w / 2 * (2.4f / 5);

        mBubbleMaxRadius = w * 1.1f / 100f;
        mBubbleMinRadius = w * 0.4f / 100f;
        mBubbleMinX = -(w / 2 * (4.5f / 5));
        mBubbleMaxX = w / 2 * (4.5f / 5);
        mBubbleMinY = mBubbleMinX;
        mBubbleMaxY = mBubbleMaxX;

        mBubbleMinCenterDistance = w / 2 * (3.9f / 5);
        mBubbleMaxCenterDistance = w / 2 * (4.3f / 5);


        mMaxDecrement = (int) (w / 100f);
        mMinDecrement = (int) (w * 0.5f / 100f);

        initBubble();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mCenterW, mCenterY);

        mPaint.setColor(mOuterCircleColor);
        canvas.drawCircle(0, 0, mOuterCircleRadius, mPaint);


        canvas.save();
        canvas.rotate(mOval1Degrees);
        mPaint.setColor(mOval1Color);
        canvas.drawOval(mOval1, mPaint);
        canvas.restore();


        canvas.save();
        canvas.rotate(mOval2Degrees);
        mPaint.setColor(mOval2Color);
        canvas.drawOval(mOval2, mPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(mOval3Degrees);
        canvas.drawOval(mOval3, mPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(mBubbleCanvasDegrees);
        mPaint.setColor(mOval2Color);
        drawBubbles(canvas, mPaint);

        canvas.restore();

        canvas.save();
        mPaint.setColor(mCenterCircleColor);
        canvas.drawCircle(0, 0, mCenterCircleRadius, mPaint);
        canvas.restore();

//        canvas.rotate(mCanvasDegrees);

    }

    private void resetDegrees() {
        mOval1Degrees = OVAL1_DEFAULT_DEGREES;
        mOval2Degrees = OVAL2_DEFAULT_DEGREES;
        mOval3Degrees = OVAL3_DEFAULT_DEGREES;
        mBubbleCanvasDegrees = 0;
    }


    public void setProgress(int progress) {
        if (progress == 1) {
            resetDegrees();
        }
        Log.d(TAG, "progress = " + progress);
        mOval1Degrees = OVAL1_DEFAULT_DEGREES + progress * 1.5f;
        mOval2Degrees = OVAL2_DEFAULT_DEGREES + progress * 1.3f;
        mOval3Degrees = OVAL3_DEFAULT_DEGREES + progress * 1.8f;
        mBubbleCanvasDegrees = progress;
        Log.d(TAG, "mOval1Degrees = " + mOval1Degrees);
        Log.d(TAG, "mOval2Degrees = " + mOval2Degrees);
        Log.d(TAG, "mOval3Degrees = " + mOval3Degrees);


        postInvalidate();
    }

    public void startAnimation(int duration) {
        mAnimator = ValueAnimator.ofInt(1, 1000);
        mAnimator.setDuration(duration);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                setProgress(value);
            }
        });


        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mAnimator.start();
    }

    public ValueAnimator getAnimator() {
        return mAnimator;
    }

    private void initBubble() {
        if (mBubbles.size() > mBubbleNum) {
            return;
        }

        for (int n = mBubbles.size(); n < mBubbleNum; n++) {
            mBubbles.add(provideBubble());
        }
    }

    private CleanBubble provideBubble() {
        CleanBubble bubble = new CleanBubble();
        bubble.cx = randomBubbleCenterX();
        bubble.cy = provideBubbleCenterY(bubble);
        bubble.radius = randomBubbleRadius();
        bubble.decrement = randomBubbleDecrement();
        return bubble;
    }

    Random random = new Random(System.currentTimeMillis());
//    private float randomBubbleCenter() {
//        int coordinate = random.nextInt((int) mBubbleMaxX * 2);
//        return coordinate + mBubbleMinX;
//    }

    private float randomBubbleCenterX() {
        int coordinate = random.nextInt((int) mBubbleMaxX * 2);
        return coordinate + mBubbleMinX;
    }

    private float provideBubbleCenterY(CleanBubble bubble) {
        float distance = randomBubbleCenterDistance();
        double absY = Math.sqrt(Math.pow(distance, 2) - Math.pow(Math.abs(bubble.cx), 2));

        bubble.distance = distance;
        int i = random.nextInt(2);
        if (i == 0) {
            return (float) absY;
        } else {
            return (float) -absY;
        }

    }

    private float randomBubbleCenterDistance() {
        int distance = random.nextInt((int) mBubbleMaxX);
        if (distance < mBubbleMinCenterDistance) {
            return randomBubbleCenterDistance();
        }
        return distance;
    }

    private float randomBubbleRadius() {
        int radius = random.nextInt((int) mBubbleMaxRadius);
        if (radius < mBubbleMinRadius) {
            return randomBubbleRadius();
        }
        return radius;
    }

    private int randomBubbleDecrement() {
        int decrement = random.nextInt(mMaxDecrement);
        if (decrement < mMinDecrement) {
            return randomBubbleDecrement();
        }
        return decrement;
    }

    private void drawBubbles(Canvas canvas, Paint paint) {
        int n = 0;
        while (n < mBubbles.size()) {
            CleanBubble bubble = mBubbles.get(n);
            canvas.drawCircle(bubble.cx, bubble.cy, bubble.radius, paint);
            Log.d(TAG, "bubble = " + bubble);
            bubble.decrease();
            if (isBubbleInCenterCircle(bubble)) {
                mBubbles.remove(bubble);
            } else {
                n++;
            }
        }
        initBubble();
    }

    public void reprovideBubble() {
        mBubbles.clear();
        initBubble();
        postInvalidate();
    }

    private boolean isBubbleInCenterCircle(CleanBubble bubble) {
        //勾股定理算出中心和原点的距离
//        double centerDistance = Math.sqrt(Math.pow(Math.abs(bubble.cx), 2) + Math.pow(Math.abs(bubble.cy), 2));
        return (bubble.distance + bubble.radius) < mCenterCircleRadius;
    }

    private class BubbleThread implements Runnable {

        boolean isCancel;

        @Override
        public void run() {
            while (!isCancel) {

            }
        }
    }

}
