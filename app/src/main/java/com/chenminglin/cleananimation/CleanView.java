package com.chenminglin.cleananimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;


/**
 * 清理动画最外层视图，这个视图包括前面旋转动画部分{@link CleanView#mSwirlLayout}，
 * 和后面的奖杯动画{@link CleanView#mTrophyLayout}
 */
public class CleanView extends FrameLayout {

    final String TAG = getClass().getSimpleName();

    //清理动画数字为垃圾缓存
    public final static int CLEAN_TYPE_JUNK = 0;
    //清理动画数字为百分比
    public final static int CLEAN_TYPE_PERCENTAGE = 1;

    int cleanType;


    //旋转动画视图布局，为了方便旋转动画结束后统一缩放效果
    View mSwirlLayout;
    //旋转动画视图
    CleanSwirlAnimationView mCleanSwirlAnimationView;
    View mSwirlOuterCircleView;

    //缓存或者数字
    TextView mTvSize;
    TextView mTvUnit;
    LinearLayout mLayoutSizeUnit;

    //奖杯视图布局
    RelativeLayout mTrophyLayout;
    //奖杯视图
    View mTrophyView;
    ImageView mStar1;
    ImageView mStar2;
    ImageView mStar3;
    TextView mTvFinish;
    CleanCircleRippleView mRippleView;

    //旋转动画
    ValueAnimator mSwirlAnimator;
    //旋转动画布局缩放动画
    AnimatorSet mScaleAnimatorSet;
    //清理缓存递减动画
    ValueAnimator mSizeAnimator;
    //清理百分比递增缓存动画
    ValueAnimator mPercentageAnimator;
    //旋转动画外部圆圈扩散动画
    AnimatorSet mOuterCircleAnimatorSet;
    //奖杯旁边星星动画
    Set<Animator> startAnimators = new HashSet<>();

    int mMaxProgress;

    /**
     * 缩放动画界限，用于控制缩放动画占动画总时长
     */
    int mScaleProgressLimit;
    /**
     * 不再生成水泡界限
     */
    int mNoProvideBubbleLimit;

    long mAmimationStartTime;

    long mJunkSize;

    OnCleanAnimationListener mOnCleanAnimationListener;


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
        mSwirlLayout = LayoutInflater.from(getContext()).inflate(R.layout.view_clean_swirl, null);
        addView(mSwirlLayout);

        mTrophyView = LayoutInflater.from(getContext()).inflate(R.layout.view_trophy, null);
        addView(mTrophyView);

        mCleanSwirlAnimationView = findViewById(R.id.swirl_animation_view);
        mSwirlOuterCircleView = findViewById(R.id.swirl_outer_ripple_view);
        mTvSize = findViewById(R.id.txt_size);
        mTvUnit = findViewById(R.id.txt_unit);
        mLayoutSizeUnit = findViewById(R.id.layout_size_unit);

        mTrophyLayout = findViewById(R.id.layout_trophy);

        mStar1 = findViewById(R.id.star1);
        mStar2 = findViewById(R.id.star2);
        mStar3 = findViewById(R.id.star3);
        mTvFinish = findViewById(R.id.tv_clean_finish);

        mRippleView = findViewById(R.id.rippleView);
    }


    public void startAnimation(final long duration, int maxProgress) {
        mMaxProgress = maxProgress;
        mCleanSwirlAnimationView.setMaxProgress(maxProgress);
        mScaleProgressLimit = (int) (maxProgress / 10f * 9);
        mNoProvideBubbleLimit = (int) (maxProgress / 20f * 9);
        mSwirlAnimator = ValueAnimator.ofInt(1, maxProgress);
        mSwirlAnimator.setDuration(duration);
        mSwirlAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mSwirlAnimator.setInterpolator(new LinearInterpolator());
        mSwirlAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mCleanSwirlAnimationView.setProgress(value);

                if (value > mNoProvideBubbleLimit && mCleanSwirlAnimationView.isProvidable()) {
                    mCleanSwirlAnimationView.setProvidable(false);
                }

                if (value > mScaleProgressLimit) {
                    if (mScaleAnimatorSet == null) {
                        mScaleAnimatorSet = new AnimatorSet();
                        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mSwirlLayout, "scaleX", 1, 0.3f);
                        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mSwirlLayout, "scaleY", 1, 0.3f);
                        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mSwirlLayout, "alpha", 1, 0f);

                        long currentTime = System.currentTimeMillis();
                        long timeLeft = duration - (currentTime - mAmimationStartTime);

                        if (timeLeft > 0) {
                            mScaleAnimatorSet.setDuration(timeLeft);
                            mScaleAnimatorSet.play(scaleXAnimator).with(scaleYAnimator).with(alphaAnimator);
                            mScaleAnimatorSet.setInterpolator(new LinearInterpolator());
                            mScaleAnimatorSet.start();
                        }
                    }
                }
            }
        });


        mSwirlAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAmimationStartTime = System.currentTimeMillis();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startTrophyAnimation();
                mOuterCircleAnimatorSet.cancel();
                mOuterCircleAnimatorSet = null;
                removeView(mSwirlLayout);
                mSwirlLayout = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mSwirlAnimator.start();

        long duration2 = duration / 10 * 9;
        if (cleanType == CLEAN_TYPE_JUNK) {
            startSizeAnimation(duration2);
        } else {
            startPercentageAnimation(duration2);
        }
        startOuterCircleAnimation();
    }

    /**
     * 垃圾容量大小动画
     *
     * @param duration
     */
    private void startSizeAnimation(long duration) {
        mLayoutSizeUnit.setVisibility(VISIBLE);
        mSizeAnimator = ValueAnimator.ofFloat(mJunkSize, 0);
        mSizeAnimator.setDuration(duration);
        mSizeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float size = (float) animation.getAnimatedValue();
                String[] sizeAndUnit = convertFileSize(Float.valueOf(size).longValue());

                mTvSize.setText(sizeAndUnit[0]);
                mTvUnit.setText(sizeAndUnit[1]);
            }
        });
        mSizeAnimator.setInterpolator(new LinearInterpolator());
        mSizeAnimator.start();
    }

    /**
     * 清理百分比进度
     *
     * @param duration
     */
    private void startPercentageAnimation(long duration) {
        mLayoutSizeUnit.setVisibility(VISIBLE);
        mPercentageAnimator = ValueAnimator.ofInt(0, 100);
        mPercentageAnimator.setDuration(duration);
        mPercentageAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mPercentageAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int percent = (int) animation.getAnimatedValue();


                mTvSize.setText(String.format("%d%%", percent));
            }
        });
        mPercentageAnimator.setInterpolator(new LinearInterpolator());
        mPercentageAnimator.start();
    }

    /**
     * 外部圆圈的动画
     */
    private void startOuterCircleAnimation() {
        mOuterCircleAnimatorSet = new AnimatorSet();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mSwirlOuterCircleView, "scaleX", 1, 1.3f);
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.setRepeatMode(ValueAnimator.RESTART);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mSwirlOuterCircleView, "scaleY", 1, 1.3f);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setRepeatMode(ValueAnimator.RESTART);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mSwirlOuterCircleView, "alpha", 1, 0f);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator.setRepeatMode(ValueAnimator.RESTART);
        mOuterCircleAnimatorSet.setDuration(600);
        mOuterCircleAnimatorSet.play(scaleXAnimator).with(scaleYAnimator).with(alphaAnimator);
        mOuterCircleAnimatorSet.setInterpolator(new LinearInterpolator());
        mOuterCircleAnimatorSet.start();
    }

    /**
     * 奖杯动画
     */
    private void startTrophyAnimation() {
        mTrophyView.setVisibility(VISIBLE);
        AnimatorSet trophyAnimator = new AnimatorSet();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mTrophyView, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mTrophyView, "scaleY", 0.3f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mTrophyView, "alpha", 0f, 1f);

        trophyAnimator.setDuration(300);
        trophyAnimator.play(scaleXAnimator).with(scaleYAnimator).with(alphaAnimator);
        trophyAnimator.setInterpolator(new OvershootInterpolator());
        trophyAnimator.start();

        trophyAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mStar1.setVisibility(VISIBLE);
                mStar2.setVisibility(VISIBLE);
                mStar3.setVisibility(VISIBLE);
                mTvFinish.setVisibility(VISIBLE);

                provideStarAnimator(mStar1, 1000).start();
                provideStarAnimator(mStar2, 800).start();
                provideStarAnimator(mStar3, 700).start();

                mRippleView.startAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mTrophyView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mOnCleanAnimationListener != null) {
                    mOnCleanAnimationListener.onFinish();
                }
            }
        }, 1200);
    }

    /**
     * 奖杯的星星动画
     *
     * @param view
     * @param duration
     * @return
     */
    private ObjectAnimator provideStarAnimator(View view, long duration) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.setRepeatMode(ValueAnimator.RESTART);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        startAnimators.add(alphaAnimator);
        return alphaAnimator;
    }


    public static String[] convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return new String[]{String.format("%.1f", (float) size / gb), "GB"};
        } else if (size >= mb) {
            float f = (float) size / mb;
            return new String[]{String.format(f > 100 ? "%.0f" : "%.1f", f), "MB"};
        } else if (size >= kb) {
            float f = (float) size / kb;
            return new String[]{String.format(f > 100 ? "%.0f" : "%.1f", f), "KB"};
        } else return new String[]{String.format("%d", size), "B"};
    }


    public void setJunkSize(long size) {
        this.mJunkSize = size;
    }

    /**
     * 取消所有动画
     */
    public void cancelAnimation() {
        if (mSwirlAnimator != null) {
            mSwirlAnimator.cancel();
        }

        if (mSizeAnimator != null) {
            mSizeAnimator.cancel();
        }

        if (mOuterCircleAnimatorSet != null) {
            mOuterCircleAnimatorSet.cancel();
        }

        if (mScaleAnimatorSet != null) {
            mScaleAnimatorSet.cancel();
            mScaleAnimatorSet = null;
        }

        if (mRippleView != null) {
            mRippleView.cancelAnimation();
            mRippleView = null;
        }

        for (Animator animator : startAnimators) {
            animator.cancel();
        }
    }

    public ValueAnimator getAnimator() {
        return mSwirlAnimator;
    }

    public void setRate(float rate) {
        this.mCleanSwirlAnimationView.setRate(rate);
    }

    public void setBubbleNum(int bubbleNum) {
        this.mCleanSwirlAnimationView.setBubbleNum(bubbleNum);
    }

    public void setOnCleanAnimationListener(OnCleanAnimationListener listener) {
        this.mOnCleanAnimationListener = listener;
    }

    /**
     * 清理动画进度回调
     */
    public interface OnCleanAnimationListener {
        void onFinish();
    }

    public void setCleanType(int cleanType) {
        this.cleanType = cleanType;
    }
}
