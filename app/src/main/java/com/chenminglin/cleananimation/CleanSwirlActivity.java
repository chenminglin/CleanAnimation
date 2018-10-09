package com.chenminglin.cleananimation;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class CleanSwirlActivity extends AppCompatActivity {

    CleanSwirlAnimationView mView;
    Button mBtnStart;

    Config mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_swirl);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                mConfig = getIntent().getExtras().getParcelable(Constants.KEY_PARAMS1);
            }
        }

        mView = findViewById(R.id.swirl_animation_view);
        mBtnStart = findViewById(R.id.btn_start);

        mView.setBubbleNum(mConfig.bubbleNum);
        mView.setRate(mConfig.rate);
        if (mConfig != null) {
            mView.setBubbleNum(mConfig.bubbleNum);
            mView.setRate(mConfig.rate);
            mBtnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(1,mConfig.drawFrequency);
                    valueAnimator.setDuration(mConfig.duration);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int progress = (int) animation.getAnimatedValue();
                            mView.setProgress(progress);
                        }
                    });
                    valueAnimator.start();
                }
            });
        }


//        final MyView myView = findViewById(R.id.myview);

        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
