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
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CleanView extends FrameLayout {

    final String TAG = getClass().getSimpleName();

    View mSwirlView;
    View mTrophyView;
    CleanSwirlAnimationView mCleanSwirlAnimationView;
    TextView mTvSize;
    TextView mTvUnit;

    RelativeLayout mLayoutTrophy;
    ImageView mStar1;
    ImageView mStar2;
    ImageView mStar3;
    TextView mTvFinish;
    CleanCircleRippleView mRippleView;

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

        mTrophyView = LayoutInflater.from(getContext()).inflate(R.layout.view_trophy, null);
        addView(mTrophyView);

        mCleanSwirlAnimationView = findViewById(R.id.swirl_animation_view);
        mTvSize = findViewById(R.id.txt_size);
        mTvUnit = findViewById(R.id.txt_unit);


        mLayoutTrophy = findViewById(R.id.layout_trophy);

        mStar1 = findViewById(R.id.star1);
        mStar2 = findViewById(R.id.star2);
        mStar3 = findViewById(R.id.star3);
        mTvFinish = findViewById(R.id.tv_clean_finish);

        mRippleView = findViewById(R.id.rippleView);


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
                        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mSwirlView, "alpha", 1, 0f);

                        long currentTime = System.currentTimeMillis();
                        long timeLeft = duration - (currentTime - mAmimationStartTime);
                        mScaleAnimatorSet.setDuration(timeLeft);
                        if (timeLeft > 0) {
                            mScaleAnimatorSet.play(scaleXAnimator).with(scaleYAnimator).with(alphaAnimator);
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
                startTrophyAnimation();
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


        //缩小
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

    private void startTrophyAnimation(){
        mTrophyView.setVisibility(VISIBLE);
        AnimatorSet animatorSet2 = new AnimatorSet();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mTrophyView, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mTrophyView, "scaleY", 0.3f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mTrophyView, "alpha", 0f, 1f);

        animatorSet2.setDuration(300);
        animatorSet2.play(scaleXAnimator).with(scaleYAnimator).with(alphaAnimator);
        animatorSet2.setInterpolator(new OvershootInterpolator());
        animatorSet2.start();

        animatorSet2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mStar1.setVisibility(VISIBLE);
                mStar2.setVisibility(VISIBLE);
                mStar3.setVisibility(VISIBLE);
                mTvFinish.setVisibility(VISIBLE);

                provideStarAnimator(mStar1,1000).start();
                provideStarAnimator(mStar2,800).start();
                provideStarAnimator(mStar3,500).start();

                mRippleView.startAnimation();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private ObjectAnimator provideStarAnimator(View view,long duration){
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.setRepeatMode(ValueAnimator.RESTART);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        return alphaAnimator;
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
