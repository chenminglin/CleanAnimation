package com.chenminglin.cleananimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView tvDuration = findViewById(R.id.tv_duration);
        final SeekBar sbDuration = findViewById(R.id.sb_duration);
        sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDuration.setText(getResources().getString(R.string.main_duration, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvDuration.setText(getResources().getString(R.string.main_duration, sbDuration.getProgress()));


        final TextView tvRate = findViewById(R.id.tv_rate);
        final SeekBar sbRate = findViewById(R.id.sb_rate);
        sbRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float rate = progress * 0.01f;
                tvRate.setText(getResources().getString(R.string.main_rate, rate));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        float rate = sbDuration.getProgress() * 0.01f;
        tvRate.setText(getResources().getString(R.string.main_rate, rate));

        final TextView tvBubbleNum = findViewById(R.id.tv_bubble_num);
        final SeekBar sbBubbleNum = findViewById(R.id.sb_bubble_num);
        sbBubbleNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvBubbleNum.setText(getResources().getString(R.string.main_bubble_num, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tvBubbleNum.setText(getResources().getString(R.string.main_bubble_num, sbBubbleNum.getProgress()));


        final TextView tvDrawFrequency = findViewById(R.id.tv_draw_frequency);
        final SeekBar sbDrawFrequency = findViewById(R.id.sb_draw_frequency);
        sbDrawFrequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int frequency = progress * 500;
                tvDrawFrequency.setText(getResources().getString(R.string.main_draw_frequency, frequency));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int frequency = sbDrawFrequency.getProgress() * 500;
        tvDrawFrequency.setText(getResources().getString(R.string.main_draw_frequency, frequency));

        final TextView tvSize = findViewById(R.id.tv_size);
        final SeekBar sbSize = findViewById(R.id.sb_size);
        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int size = progress * 6;
                tvSize.setText(getResources().getString(R.string.main_size, size));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int size = sbSize.getProgress() * 3;
        tvSize.setText(getResources().getString(R.string.main_size, size));

        final RadioGroup rgButton = findViewById(R.id.rg_size_unit);

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SimpleActivity.class);
                Config config = new Config();
                config.bubbleNum = sbBubbleNum.getProgress();
                config.drawFrequency = sbDrawFrequency.getProgress() * 500;
                config.duration = sbDuration.getProgress() * 1000;
                config.rate = sbRate.getProgress() * 0.01f;
                int sizeProgress = sbSize.getProgress();
                int checkedId = rgButton.getCheckedRadioButtonId();
                long size = 0;
                if (checkedId == R.id.rb_size_unit_KB) {
                    size = sizeProgress * 1024;
                } else if (checkedId == R.id.rb_size_unit_MB) {
                    size = ((long) sizeProgress) * 1024 * 1024;
                } else if (checkedId == R.id.rb_size_unit_GB) {
                    size = ((long) sizeProgress) * 1024 * 1024 * 1024;
                }
                config.junkSize = size;
                intent.putExtra(Constants.KEY_PARAMS1, config);

                startActivity(intent);
            }
        });
    }
}
