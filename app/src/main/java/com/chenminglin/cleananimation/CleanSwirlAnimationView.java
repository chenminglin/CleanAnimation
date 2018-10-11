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


/**
 * 旋转动画
 */
public class CleanSwirlAnimationView extends View {
    final String TAG = getClass().getSimpleName();

    final int DEFAULT_COLOR = Color.parseColor("#32BD7B");
    //椭圆的默认角度
    final float OVAL1_DEFAULT_DEGREES = -15F;
    final float OVAL2_DEFAULT_DEGREES = -60F;
    final float OVAL3_DEFAULT_DEGREES = -100F;

    Paint mPaint;
    int mCenterX;
    int mCenterY;

    //椭圆的转动速率
    float mRate = 1;
    float mRealRate;

    //水泡的默认个数
    final int BUBBLE_DEFAULT_NUM = 50;


    //水泡半径
    float[] mBubbleRadiuses;
    //水泡中心距离
    float[] mBubbleCenterDistance;
    //水泡中心距离递减值
    int[] mBubbleDecrement;

    //水泡离中心点的最小x坐标
    float mBubbleMinX = 0;
    //水泡离中心点的最大x坐标
    float mBubbleMaxX = 0;
    //水泡离中心点的最小Y坐标
    float mBubbleMinY = 0;
    //水泡离中心点的最大Y坐标
    float mBubbleMaxY = 0;
    //画水泡的画布的角度
    float mBubbleCanvasDegrees;
    //水泡的个数
    int mBubbleNum;
    List<CleanBubble> mBubbles = new ArrayList<>();


    int mOval1Color = Color.parseColor("#43FFFFFF");
    RectF mOval1 = new RectF();
    float mOval1Degrees = OVAL1_DEFAULT_DEGREES;

    int mOval2Color = Color.parseColor("#50FFFFFF");
    RectF mOval2 = new RectF();
    float mOval2Degrees = OVAL2_DEFAULT_DEGREES;

    RectF mOval3 = new RectF();
    float mOval3Degrees = OVAL3_DEFAULT_DEGREES;

    int mBubbleColor = Color.WHITE;

    //中心圈的半径
    float mCenterCircleRadius;
    //中心圈的颜色
    int mCenterCircleColor;

    //用于判断是否还继续生成水泡
    private boolean isProvidable = true;

    int mMaxProgress;

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

        mBubbleMinX = -(w / 2 * (4.5f / 5));
        mBubbleMaxX = w / 2 * (4.5f / 5);
        mBubbleMinY = mBubbleMinX;
        mBubbleMaxY = mBubbleMaxX;

        mBubbleRadiuses = new float[]{w * 1.1f / 100f, w * 0.7f / 100f, w * 0.3f / 100f};
        mBubbleCenterDistance = new float[]{w / 2 * (4.3f / 5), w / 2 * (4.5f / 5), w / 2 * (4.7f / 5)};
        mBubbleDecrement = new int[]{w / 80, w / 160};

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

        long endTime = System.currentTimeMillis();
        Log.d(TAG,  "draw time = " + (endTime - startTime));
    }

    /**
     * 重置角度
     */
    private void resetDegrees() {
        mOval1Degrees = OVAL1_DEFAULT_DEGREES;
        mOval2Degrees = OVAL2_DEFAULT_DEGREES;
        mOval3Degrees = OVAL3_DEFAULT_DEGREES;
        mBubbleCanvasDegrees = 0;
    }


    /**
     * 动画进度设置
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress == 1) {
            resetDegrees();
        }
        progressToRate(progress);
        Log.d(TAG, "progress = " + progress);
        //后面浮点差值时用于戳开三个椭圆的旋转的角度
        mOval1Degrees = OVAL1_DEFAULT_DEGREES + progress * mRealRate * 0.5f;
        mOval2Degrees = OVAL2_DEFAULT_DEGREES + progress * mRealRate * 0.3f;
        mOval3Degrees = OVAL3_DEFAULT_DEGREES + progress * mRealRate * 0.7f;
        mBubbleCanvasDegrees = progress * mRealRate * 0.3f;
        Log.d(TAG,"mOval1Degrees = " + mOval1Degrees);
        Log.d(TAG, "mOval2Degrees = " + mOval2Degrees);
        Log.d(TAG, "mOval3Degrees = " + mOval3Degrees);

        postInvalidate();
    }

    /**
     * 通过转换算出真实的速率，progress^2是为了速度递增加速，一开始慢，后面会很快，可以达到这个效果
     * 60000f是随便取的一个值
     * @param progress
     */
    private void progressToRate(int progress) {
        mRealRate = (mRate * progress * progress) / 60000f;
    }


    /**
     * 初始化水泡
     */
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

    /**
     * 生成水泡
     * @return
     */
    private CleanBubble provideBubble() {
        CleanBubble bubble = new CleanBubble();
        int mod = mBubbles.size() % 2;
        if (mod == 0) {//两种生成中心坐标方式，这样水泡分布会更加均匀
            bubble.cx = randomBubbleCenterX();
            bubble.cy = provideBubbleCenterY(bubble);
        } else {
            bubble.cy = randomBubbleCenterY();
            bubble.cx = provideBubbleCenterX(bubble);
        }
        bubble.radius = randomBubbleRadius();
        bubble.initDistance = bubble.distance;
        bubble.decrement = randomBubbleDecrement();
        return bubble;
    }

    Random random = new Random(System.currentTimeMillis());

    /**
     * 随机生成水泡中心x坐标
     * @return 中心x坐标
     */
    private float randomBubbleCenterX() {
        int coordinate = random.nextInt((int) mBubbleMaxX * 2);
        return coordinate + mBubbleMinX;
    }

    /**
     * 根据随机中心距离，生成中心y坐标
     * @param bubble
     * @return 中心y坐标
     */
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

    /**
     * 随机生成水泡中心距离
     * @return
     */
    private float randomBubbleCenterDistance() {
        int distanceIndex = random.nextInt(3);
        return mBubbleCenterDistance[distanceIndex];
    }

    /**
     * 随机生成水泡半径
     * @return
     */
    private float randomBubbleRadius() {
        int radiusIndex = random.nextInt(3);
        return mBubbleRadiuses[radiusIndex];
    }

    /**
     * 随机生成水泡往内聚起来的递减幅度，让每个水泡没有统一速度往里面靠。
     * @return
     */
    private int randomBubbleDecrement() {
        int decrementIndex = random.nextInt(2);
        return mBubbleDecrement[decrementIndex];
    }

    /**
     * 画所有的水泡，水泡一开始透明的为0，越靠里面，透明度越大。
     * @param canvas
     * @param paint
     */
    private void drawBubbles(Canvas canvas, Paint paint) {
        mPaint.setColor(Color.WHITE);
        int n = 0;
        while (n < mBubbles.size()) {
            CleanBubble bubble = mBubbles.get(n);
            float a = bubble.distance - mCenterCircleRadius;
            float b = bubble.initDistance - mCenterCircleRadius;
            int alpha = (int) (255 * (1 - (a / b)));
            paint.setAlpha(alpha);
            canvas.drawCircle(bubble.cx, bubble.cy, bubble.radius, paint);
//            Log.d(TAG, "bubble = " + bubble);
            bubble.decrease();
            //已经移动到里面的水泡需要移除
            if (isBubbleInCenterCircle(bubble)) {
                mBubbles.remove(bubble);
            } else {
                n++;
            }
        }
        /**
         * 如果已经不再生成水泡，则不需要继续生成，不再生成时机参考{@link CleanSwirlAnimationView#setProvidable(boolean)}
         */
        if (isProvidable) {
            //移除后的水泡需要重新生成
            initBubble();
        }
    }

    public void reprovideBubble() {
        mBubbles.clear();
        initBubble();
        postInvalidate();
    }

    /**
     * 判断水泡是否在中心圆圈内
     * @param bubble
     * @return
     */
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

    public boolean isProvidable() {
        return isProvidable;
    }

    public void setProvidable(boolean providable) {
        isProvidable = providable;
    }


    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }
}
