package com.example.marangeptabata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.media.MediaPlayer;

import com.example.marangeptabata.model.Tabata;

import java.util.ArrayList;
import java.util.Map;

public class TimerActivity extends AppCompatActivity {
    //Data
    private Tabata tabata;
    private CountDownTimer timer;
    private long updatedTime = 50000;
    private int stepNb = 0;
    private ArrayList<String> tabataCycle;
    private Map<String, Integer> color;
    private Map<String, String> stepNameList;
    private boolean timerIsRunning = false;
    //View
    private MediaPlayer mediaPlayer;
    private Button startPauseButton;
    private Button stopButton;
    private TextView timerValue;
    private RelativeLayout activityTimer;
    private TextView stepName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //Data initialisation
        //tabata = new Tabata(2,5,3,4,2,2);
        tabata = getIntent().getParcelableExtra("tabata");
        tabata.updateValues();
        tabataCycle = tabata.getTabataCycle();
        color = tabata.getStepColor();
        stepNameList = tabata.getStepName();
        //Get views
        activityTimer = (RelativeLayout) findViewById(R.id.activity_timer);
        timerValue = (TextView) findViewById(R.id.timerValue);
        startPauseButton = (Button) findViewById(R.id.startPauseButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        stepName = (TextView) findViewById(R.id.step_name);
        //Event
        getSupportActionBar().hide();
        startPauseButton.setText("PAUSE");
        if (savedInstanceState != null) {
            stepNb = savedInstanceState.getInt("step");
            updatedTime = savedInstanceState.getLong("time");
            switch (tabataCycle.get(stepNb - 1)) {
                case "work":
                    setColorAndText("workTime");
                    break;
                case "rest":
                    setColorAndText("restTime");
                    break;
                case "preparation":
                    setColorAndText("prepareTime");
                    break;
                case "long_rest":
                    setColorAndText("longRestTime");
                    break;
                default:
                    break;
            }
            startTimer();
        } else {
            update();
            nextStep();
        }

    }

    private void nextStep() {
        if(tabataCycle.size() > stepNb) {
            switch (tabataCycle.get(stepNb)) {
                case "work":
                    startWork();
                    break;
                case "rest":
                    startRest();
                    break;
                case "preparation":
                    startPreparation();
                    break;
                case "long_rest":
                    startLongRest();
                    break;
                default:
                    break;
            }
            stepNb++;
        } else {
            lancerSon("repos");
            finish();
        }
    }

    private void startWork() {
        lancerSon("work");
        setColorAndText("workTime");
        updatedTime = tabata.getWorkTime() * 1000;
        this.startTimer();
    }

    private void startRest() {
        lancerSon("repos");
        setColorAndText("restTime");
        updatedTime = tabata.getRestTime() * 1000;
        this.startTimer();
    }

    private void startPreparation() {
        setColorAndText("prepareTime");
        updatedTime = tabata.getPrepareTime() * 1000;
        this.startTimer();
    }

    private void startLongRest() {
        lancerSon("repos");
        setColorAndText("longRestTime");
        updatedTime = tabata.getLongRestTime() * 1000;
        this.startTimer();
    }

    private void setColorAndText(String step) {
        int stepColor = color.get(step);
        activityTimer.setBackgroundColor(stepColor);
        startPauseButton.setTextColor(stepColor);
        stopButton.setTextColor(stepColor);
        stepName.setText(stepNameList.get(step));
    }

    private void update() {
        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        timerValue.setText("" + mins + ":" + String.format("%02d", secs));
    }

    private void startTimer() {
        timer = new CountDownTimer(updatedTime, 10) {

            public void onTick(long millisUntilFinished) {
                timerIsRunning = true;
                updatedTime = millisUntilFinished;
                update();
            }

            public void onFinish() {
                timerIsRunning = false;
                updatedTime = 0;
                update();
                nextStep();
            }
        }.start();
    }

    public void onStartPause(View view) {
        if(timerIsRunning) {
            timer.cancel();
            timerIsRunning = false;
            startPauseButton.setText("PLAY");
            stopButton.setVisibility(View.VISIBLE);
        } else {
            startTimer();
            startPauseButton.setText("PAUSE");
            stopButton.setVisibility(View.GONE);
        }
    }

    public void onStop(View view) {
        finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("step", stepNb);
        outState.putLong("time", updatedTime);
        timer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void lancerSon(String id) {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        this.mediaPlayer = MediaPlayer.create(
                this,
                this.getResources().getIdentifier(id, "raw", this.getPackageName())
        );
        this.mediaPlayer.start();
    }
}
