package com.chenminglin.cleananimation;

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
import java.util.List;
import java.util.Random;


public class CleanSwirlAnimationView extends View {
    final String TAG = getClass().getSimpleName();

    final int DEFAULT_COLOR = Color.parseColor("#32BD7B");
    //椭圆的默认角度
    final float OVAL1_DEFAULT_DEGREES = -15F;
    final float OVAL2_DEFAULT_DEGREES = -60F;
    final float OVAL3_DEFAULT_DEGREES = -100F;
    //椭圆的转动速率
    float mRate = 1;

    //水泡的默认个数
    final int BUBBLE_DEFAULT_NUM = 50;

    //水泡的最大半径
    float mBubbleMaxRadius = 0;
    //水泡的最小半径
    float mBubbleMinRadius = 0;
    //水泡离中心点的最小x坐标
    float mBubbleMinX = 0;
    //水泡离中心点的最大x坐标
    float mBubbleMaxX = 0;
    //水泡离中心点的最小Y坐标
    float mBubbleMinY = 0;
    //水泡离中心点的最大Y坐标
    float mBubbleMaxY = 0;
    //水泡中心最小距离
    float mBubbleMinCenterDistance;
    //水泡中心最大距离
    float mBubbleMaxCenterDistance;
    //水泡的个数
    int mBubbleNum;

    List<CleanBubble> mBubbles = new ArrayList<>();

    Paint mPaint;

    int mCenterX;
    int mCenterY;

    int mOuterCircleColor = Color.parseColor("#20FFFFFF");


    int mOval1Color = Color.parseColor("#43FFFFFF");
    RectF mOval1 = new RectF();
    float mOval1Degrees = OVAL1_DEFAULT_DEGREES;

    int mOval2Color = Color.parseColor("#50FFFFFF");
    RectF mOval2 = new RectF();
    float mOval2Degrees = OVAL2_DEFAULT_DEGREES;

    RectF mOval3 = new RectF();
    float mOval3Degrees = OVAL3_DEFAULT_DEGREES;

    int mBubbleColor = Color.parseColor("#7FFFFFFF");

    //中心圈的半径
    float mCenterCircleRadius;
    //中心圈的颜色
    int mCenterCircleColor;
    //画水泡的画布的角度
    float mBubbleCanvasDegrees;
    //水泡收缩递减最大值
    int mMaxDecrement;
    //水泡收缩递减最小值
    int mMinDecrement;

    float mOuterCircleRadius;


//    Camera mCamera;
//    Matrix mMatrix;
//    float mCameraZ;


    public CleanSwirlAnimationView(Context context) {
        super(context);
        init(null, 0);


    }

    public CleanSwirlAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);

    }

    public CleanSwirlAnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CleanSwirlAnimationView, defStyle, 0);


        mCenterCircleColor = a.getColor(R.styleable.CleanSwirlAnimationView_themeColor, DEFAULT_COLOR);

        mBubbleNum = a.getInt(R.styleable.CleanSwirlAnimationView_bubbleNum, BUBBLE_DEFAULT_NUM);

        if (mBubbleNum > BUBBLE_DEFAULT_NUM) {
            mBubbleNum = BUBBLE_DEFAULT_NUM;
        }

        mRate = a.getFloat(R.styleable.CleanSwirlAnimationView_rate, 1);

        a.recycle();
        initPaint();

//        mCamera = new Camera();
//        mMatrix = new Matrix();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;

        mOuterCircleRadius = w / 2 * (3.4f / 5);

        float oval1Left = w / 2 * (2.9f / 5);
        float oval1Top = w / 2 * (2.45f / 5);
        mOval1.set(-oval1Left, -oval1Top, oval1Left, oval1Top);

        float oval2Left = w / 2 * (2.8f / 5);
        float oval2Top = w / 2 * (2.45f / 5);
        mOval2.set(-oval2Left, -oval2Top, oval2Left, oval2Top);

        float oval3Left = w / 2 * (2.75f / 5);
        float oval3Top = w / 2 * (2.45f / 5);
        mOval3.set(-oval3Left, -oval3Top, oval3Left, oval3Top);

        mCenterCircleRadius = w / 2 * (2.4f / 5);

        mBubbleMaxRadius = w * 1.1f / 100f;
        mBubbleMinRadius = w * 0.3f / 100f;
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
        long startTime = System.currentTimeMillis();

        canvas.translate(mCenterX, mCenterY);

//        mMatrix.reset();
//        mCamera.save();
////        mCamera.rotateX(mBubbleCanvasDegrees);
//        mCamera.rotateZ(-mCameraZ);
//        mCamera.translate(0, 0, mCameraZ);
//        mCamera.getMatrix(mMatrix);
//        mCamera.restore();
////        mMatrix.preTranslate(-mCenterX, -mCenterY);
////        mMatrix.postTranslate(mCenterX, mCenterY);
//        canvas.concat(mMatrix);

        //画最外面的圆圈
//        mPaint.setColor(mOuterCircleColor);
//        canvas.drawCircle(0, 0, mOuterCircleRadius, mPaint);

        //画透明度27%的椭圆
        canvas.save();
        canvas.rotate(mOval1Degrees);
        mPaint.setColor(mOval1Color);
        canvas.drawOval(mOval1, mPaint);
        canvas.restore();

        //画透明度95%的椭圆
        canvas.save();
        canvas.rotate(mOval2Degrees);
        mPaint.setColor(mOval2Color);
        canvas.drawOval(mOval2, mPaint);
        canvas.restore();

        //画透明度95%的小椭圆
        canvas.save();
        canvas.rotate(mOval3Degrees);
        canvas.drawOval(mOval3, mPaint);
        canvas.restore();

        //画外面的点点
        canvas.save();
        canvas.rotate(mBubbleCanvasDegrees);
        mPaint.setColor(mBubbleColor);
        drawBubbles(canvas, mPaint);
        canvas.restore();

        //画中间的绿色圆圈
        canvas.save();
        mPaint.setColor(mCenterCircleColor);
        canvas.drawCircle(0, 0, mCenterCircleRadius, mPaint);
        canvas.restore();


//        canvas.rotate(mCanvasDegrees);

        long endTime = System.currentTimeMillis();
        Log.d(TAG, "draw time = " + (endTime - startTime));
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
        mOval1Degrees = OVAL1_DEFAULT_DEGREES + progress * mRate * 0.5f;
        mOval2Degrees = OVAL2_DEFAULT_DEGREES + progress * mRate * 0.3f;
        mOval3Degrees = OVAL3_DEFAULT_DEGREES + progress * mRate * 0.7f;
        mBubbleCanvasDegrees = progress * mRate * 0.3f;
        Log.d(TAG, "mOval1Degrees = " + mOval1Degrees);
        Log.d(TAG, "mOval2Degrees = " + mOval2Degrees);
        Log.d(TAG, "mOval3Degrees = " + mOval3Degrees);

        postInvalidate();
    }


    private void initBubble() {
        if (mBubbles.size() > mBubbleNum) {
            return;
        }

        long startTime = System.currentTimeMillis();
        for (int n = mBubbles.size(); n < mBubbleNum; n++) {
            mBubbles.add(provideBubble());
        }
        long endTime = System.currentTimeMillis();

        Log.d(TAG, "init bubble time = " + (endTime - startTime));
    }

    private CleanBubble provideBubble() {
        CleanBubble bubble = new CleanBubble();
        int mod = mBubbles.size() % 2;
        if (mod == 0) {
            bubble.cx = randomBubbleCenterX();
            bubble.cy = provideBubbleCenterY(bubble);
            bubble.radius = randomBubbleRadius();
            bubble.decrement = randomBubbleDecrement();
        } else {
            bubble.cy = randomBubbleCenterY();
            bubble.cx = provideBubbleCenterX(bubble);
            bubble.radius = randomBubbleRadius();
            bubble.decrement = randomBubbleDecrement();
        }
        return bubble;
    }

    Random random = new Random(System.currentTimeMillis());

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

    private float randomBubbleCenterY() {
        int coordinate = random.nextInt((int) mBubbleMaxY * 2);
        return coordinate + mBubbleMinY;
    }

    private float provideBubbleCenterX(CleanBubble bubble) {
        float distance = randomBubbleCenterDistance();
        double absX = Math.sqrt(Math.pow(distance, 2) - Math.pow(Math.abs(bubble.cy), 2));
        bubble.distance = distance;
        int i = random.nextInt(2);
        if (i == 0) {
            return (float) absX;
        } else {
            return (float) -absX;
        }
    }

    private float randomBubbleCenterDistance() {
        int distance = random.nextInt((int) mBubbleMaxCenterDistance);
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
        mPaint.setColor(Color.WHITE);
        int n = 0;
        while (n < mBubbles.size()) {
            CleanBubble bubble = mBubbles.get(n);
            canvas.drawCircle(bubble.cx, bubble.cy, bubble.radius, paint);
//            Log.d(TAG, "bubble = " + bubble);
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

    public void setRate(float rate) {
        this.mRate = rate;
    }

    public void setBubbleNum(int bubbleNum) {
        this.mBubbleNum = bubbleNum;
    }


}
