package com.chenminglin.cleananimation;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;

public class SimpleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple);


        final CleanAnimationView cleanAnimationView = findViewById(R.id.clean_view);

        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cleanAnimationView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ValueAnimator animator = cleanAnimationView.getAnimator();
                if (animator != null && (animator.isRunning() || animator.isStarted())) {
                    animator.cancel();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanAnimationView.setProgress(1);
                cleanAnimationView.startAnimation(10000);

            }
        });

        findViewById(R.id.btn_provide_bubble).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanAnimationView.reprovideBubble();
            }
        });


    }
}
