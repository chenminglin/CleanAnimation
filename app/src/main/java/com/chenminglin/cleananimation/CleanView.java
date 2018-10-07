package com.chenminglin.cleananimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;


public class CleanView extends FrameLayout {

    final String TAG = getClass().getSimpleName();

    View mSwirlView;
    CleanSwirlAnimationView mCleanSwirlAnimationView;
    TextView mTvSize;
    TextView mTvUnit;

    ValueAnimator mAnimator;
    AnimatorSet mScaleAnimatorSet;

    int mMaxProgress;

    int mScaleProgressLimit;

    long mAmimationStartTime;

    long mJunkSize;


    public CleanView(Context context) {
        super(context);
        init(null, 0);
    }

    public CleanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CleanView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CleanView, defStyle, 0);


        a.recycle();


//        ViewStub viewStub = new ViewStub(getContext());
//        viewStub.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        viewStub.setInflatedId();
//        viewStub.
//        addView(viewStub);

        mSwirlView = LayoutInflater.from(getContext()).inflate(R.layout.view_clean_swirl, null);
        addView(mSwirlView);

        mCleanSwirlAnimationView = findViewById(R.id.swirl_animation_view);
        mTvSize = findViewById(R.id.txt_size);
        mTvUnit = findViewById(R.id.txt_unit);
    }


    public void startAnimation(final long duration, int maxProgress) throws Exception {

        if (mJunkSize <= 0) {
            throw new Exception("垃圾空间大小没有设置");
        }

        mMaxProgress = maxProgress;
        mScaleProgressLimit = (int) (maxProgress / 10f * 9);
        mAnimator = ValueAnimator.ofInt(1, maxProgress);
        mAnimator.setDuration(duration);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mCleanSwirlAnimationView.setProgress(value);

                if (value > mScaleProgressLimit) {
                    if (mScaleAnimatorSet == null) {
                        mScaleAnimatorSet = new AnimatorSet();
                        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mSwirlView, "scaleX", 1, 0.3f);
                        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mSwirlView, "scaleY", 1, 0.3f);

                        long currentTime = System.currentTimeMillis();
                        long timeLeft = duration - (currentTime - mAmimationStartTime);
                        mScaleAnimatorSet.setDuration(timeLeft);
                        if (timeLeft > 0) {
                            mScaleAnimatorSet.play(scaleXAnimator).with(scaleYAnimator);
                            mScaleAnimatorSet.setInterpolator(new LinearInterpolator());
                            mScaleAnimatorSet.start();
                        }
                    }
                }


            }
        });


        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAmimationStartTime = System.currentTimeMillis();
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

        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.start();


        ValueAnimator sizeAnimator = ValueAnimator.ofFloat(mJunkSize, 0);
        long duration2 = duration / 10 * 9;
        sizeAnimator.setDuration(duration2);
        sizeAnimator.setRepeatMode(ValueAnimator.REVERSE);

        sizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float size = (float) animation.getAnimatedValue();
                String[] sizeAndUnit = convertFileSize(Float.valueOf(size).longValue());

                mTvSize.setText(sizeAndUnit[0]);
                mTvUnit.setText(sizeAndUnit[1]);
            }
        });

        sizeAnimator.setInterpolator(new LinearInterpolator());
        sizeAnimator.start();
    }


    public static String[] convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return new String[]{String.format("%.2f", (float) size / gb), "GB"};
        } else if (size >= mb) {
            float f = (float) size / mb;
            return new String[]{String.format(f > 100 ? "%.0f" : "%.2f", f), "MB"};
        } else if (size >= kb) {
            float f = (float) size / kb;
            return new String[]{String.format(f > 100 ? "%.0f" : "%.2f", f), "KB"};
        } else return new String[]{String.format("%d", size), "B"};
    }


    public void setJunkSize(long size) {
        this.mJunkSize = size;
    }

    public void cancelAnimation() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    public ValueAnimator getAnimator() {
        return mAnimator;
    }

}
