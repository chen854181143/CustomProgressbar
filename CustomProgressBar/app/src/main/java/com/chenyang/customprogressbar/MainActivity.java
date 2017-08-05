package com.chenyang.customprogressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.chenyang.customprogressbar.view.HorizontalProgressbarWithProgress;
import com.chenyang.customprogressbar.view.RoundProgressBarWithProgress;

public class MainActivity extends AppCompatActivity {


    private HorizontalProgressbarWithProgress horizontalProgressbarWithProgress;
    private RoundProgressBarWithProgress roundProgressBarWithProgress;
    private static final int MSG_UPDATE = 0X110;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = horizontalProgressbarWithProgress.getProgress();
            horizontalProgressbarWithProgress.setProgress(++progress);
            roundProgressBarWithProgress.setProgress(++progress);
            if (progress >= 100) {
                handler.removeMessages(MSG_UPDATE);
            }
            handler.sendEmptyMessageDelayed(MSG_UPDATE,100);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        horizontalProgressbarWithProgress= (HorizontalProgressbarWithProgress) findViewById(R.id.pg_id1);
        roundProgressBarWithProgress = (RoundProgressBarWithProgress) findViewById(R.id.round);
        handler.sendEmptyMessage(1);
    }
}
