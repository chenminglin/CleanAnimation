package com.chenminglin.cleananimation;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class SimpleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple);


        final CleanView cleanView = findViewById(R.id.clean_view);

//        SeekBar seekBar = findViewById(R.id.seekbar);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                cleanSwirlAnimationView.setProgress(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                ValueAnimator animator = cleanSwirlAnimationView.getAnimator();
//                if (animator != null && (animator.isRunning() || animator.isStarted())) {
//                    animator.cancel();
//                }
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });


        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cleanSwirlAnimationView.setProgress(1);
                try {
                    cleanView.setJunkSize(10000000 * 1000);
                    cleanView.startAnimation(5000, 500);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

//        findViewById(R.id.btn_provide_bubble).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                cleanView.reprovideBubble();
//            }
//        });


    }
}
