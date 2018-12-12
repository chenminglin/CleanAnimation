package com.chenminglin.cleananimation;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class SimpleActivity extends AppCompatActivity {

    CleanView cleanView;

    Config mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                mConfig = getIntent().getExtras().getParcelable(Constants.KEY_PARAMS1);
            }
        }

        cleanView = findViewById(R.id.clean_view);
        if (mConfig != null) {
            cleanView.setJunkSize(mConfig.junkSize);
            cleanView.setBubbleNum(mConfig.bubbleNum);
            cleanView.setRate(mConfig.rate);
            try {
                cleanView.startAnimation(mConfig.duration, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cleanView.setOnCleanAnimationListener(new CleanView.OnCleanAnimationListener() {
            @Override
            public void onFinish() {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanView.cancelAnimation();
    }
}
