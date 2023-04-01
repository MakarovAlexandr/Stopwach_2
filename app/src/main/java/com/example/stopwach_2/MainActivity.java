package com.example.stopwach_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart;
    private Button buttonPause;
    private Button buttonStop;
    private Button buttonTimeBack;
    private TextView stopwatchOut;

    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timePause = 0L;
    private long timeDerection = 0L;
    private long updatedTime = 0L;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.buttonStart);
        buttonPause = findViewById(R.id.buttonPause);
        buttonStop = findViewById(R.id.buttonStop);
        buttonTimeBack = findViewById(R.id.buttonTimeBack);
        stopwatchOut = findViewById(R.id.stopwatchOut);

        buttonStart.setOnClickListener(listener);
        buttonPause.setOnClickListener(listener);
        buttonStop.setOnClickListener(listener);
        buttonTimeBack.setOnClickListener(listener);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.buttonStart:
                    timeDerection = 1L;
                    startTime = SystemClock.uptimeMillis();
                    handler.removeCallbacks(backTimerThread);
                    handler.postDelayed(updateTimerThread, 0);
                    break;
                case R.id.buttonPause:
                    timePause += timeInMilliseconds * timeDerection;
                    handler.removeCallbacks(updateTimerThread);
                    handler.removeCallbacks(backTimerThread);
                    break;
                case R.id.buttonStop:
                    startTime = 0L;
                    timePause = 0L;
                    handler.removeCallbacks(updateTimerThread);
                    handler.removeCallbacks(backTimerThread);
                    stopwatchOut.setText(getString(R.string.messageStop));
                    break;
                case R.id.buttonTimeBack:
                    timeDerection = -1L;
                    startTime = SystemClock.uptimeMillis();
                    handler.removeCallbacks(updateTimerThread);
                    handler.postDelayed(backTimerThread, 0);
                    break;
            }
        }
    };

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timePause + timeInMilliseconds;

            int milliseconds = (int) (updatedTime % 1000);
            int second = (int) (updatedTime / 1000);
            int minute = second / 60;
            int hour = minute / 60;
            int day = hour / 24;
            second = second % 60;
            minute = minute % 60;
            hour = hour % 24;

            stopwatchOut.setText("" + day + ":" + hour + ":" + minute + ":" + String.format("%02d", second) + ":" + String.format("%03d", milliseconds));
            handler.postDelayed(this, 0);
        }
    };
    private Runnable backTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timePause - timeInMilliseconds;

            if (updatedTime < 0) {
                updatedTime = 0;
            }
            int milliseconds = (int) (updatedTime % 1000);
            int second = (int) (updatedTime / 1000);
            int minute = second / 60;
            int hour = minute / 60;
            int day = hour / 24;
            second = second % 60;
            minute = minute % 60;
            hour = hour % 24;

            stopwatchOut.setText("" + day + ":" + hour + ":" + minute + ":" + String.format("%02d", second) + ":" + String.format("%03d", milliseconds));
            if (updatedTime > 0) {
                handler.postDelayed(this, 0);
            }
            if (updatedTime == 0) {
                timePause = 0;
            }
        }
    };
}