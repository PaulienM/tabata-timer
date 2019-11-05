package com.example.marangeptabata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    //View
    private Button startPauseButton;
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
        stepName = (TextView) findViewById(R.id.step_name);
        //Event

        //Graphic update
        update();
        nextStep();
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
            //fin
        }
    }

    private void startWork() {
        setBackgroundAndText("workTime");
        updatedTime = tabata.getWorkTime() * 1000;
        this.startTimer();
    }

    private void startRest() {
        setBackgroundAndText("restTime");
        updatedTime = tabata.getRestTime() * 1000;
        this.startTimer();
    }

    private void startPreparation() {
        setBackgroundAndText("prepareTime");
        updatedTime = tabata.getPrepareTime() * 1000;
        this.startTimer();
    }

    private void startLongRest() {
        setBackgroundAndText("longRestTime");
        updatedTime = tabata.getLongRestTime() * 1000;
        this.startTimer();
    }

    private void setBackgroundAndText(String step) {
        activityTimer.setBackgroundColor(color.get(step));
        stepName.setText(stepNameList.get(step));
    }

    private void onStart(View view) {

        timer = new CountDownTimer(updatedTime, 10) {

            public void onTick(long millisUntilFinished) {
                updatedTime = millisUntilFinished;
                update();
            }

            public void onFinish() {
                updatedTime = 0;
                update();
            }
        }.start();
    }

    private void onPause(View view) {
        if (timer != null) {
            timer.cancel();
        }
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
                updatedTime = millisUntilFinished;
                update();
            }

            public void onFinish() {
                updatedTime = 0;
                update();
                nextStep();
            }
        }.start();
    }
}
