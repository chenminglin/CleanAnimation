package com.chenminglin.cleananimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


public class MyView extends View {


    int centerX;
    int centerY;
    private Path mPath;
    Paint mPaint;

    private float blackMagic = 0.551915024494f;
    private VPoint p2,p4;
    private HPoint p1,p3;

    private float radius;
    private float c;

    private int progress;


    public MyView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyView, defStyle, 0);

        a.recycle();
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
        mPath = new Path();
        p2 = new VPoint();
        p4 = new VPoint();

        p1 = new HPoint();
        p3 = new HPoint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = w / 6;
        c = radius*blackMagic;
    }

    private void invalidateTextPaintAndMeasurements() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX,centerY);
        mPath.reset();
        model0();
        float p2x = radius + progress * 0.05f * radius;
        p2.setX(p2x);
        mPath.moveTo(p1.x,p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x,p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x,p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x,p4.y);
        mPath.cubicTo(p4.bottom.x,p4.bottom.y,p1.left.x,p1.left.y,p1.x,p1.y);

        canvas.drawPath(mPath,mPaint);
    }

    private void model0(){
        p1.setY(radius);
        p3.setY(-radius);
        p3.x = p1.x = 0;
        p3.left.x =  p1.left.x = -c;
        p3.right.x = p1.right.x = c;

        p2.setX(radius);
        p4.setX(-radius);
        p2.y = p4.y = 0;
        p2.top.y =  p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = c;
    }

    class VPoint{
        public float x;
        public float y;
        public PointF top = new PointF();
        public PointF bottom = new PointF();

        public void setX(float x){
            this.x = x;
            top.x = x;
            bottom.x = x;
        }

        public void adjustY(float offset){
            top.y -= offset;
            bottom.y += offset;
        }

        public void adjustAllX(float offset){
            this.x+= offset;
            top.x+= offset;
            bottom.x+=offset;
        }
    }

    class HPoint{
        public float x;
        public float y;
        public PointF left = new PointF();
        public PointF right = new PointF();

        public void setY(float y){
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void adjustAllX(float offset){
            this.x +=offset;
            left.x +=offset;
            right.x +=offset;
        }
    }

    public void setProgress(int value){
        progress = value;
        postInvalidate();
    }
}
