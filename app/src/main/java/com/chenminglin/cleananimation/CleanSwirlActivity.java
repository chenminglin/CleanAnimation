package com.chenminglin.cleananimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CleanSwirlActivity extends AppCompatActivity {

    CleanSwirlAnimationView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_swirl);

        mView = findViewById(R.id.swirl_animation_view);

        mView.setBubbleNum(40);
        mView.setRate(10);

    }
}
