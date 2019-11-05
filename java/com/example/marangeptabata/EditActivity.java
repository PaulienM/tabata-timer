package com.example.marangeptabata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marangeptabata.db.AppDatabase;
import com.example.marangeptabata.db.DatabaseClient;
import com.example.marangeptabata.model.Tabata;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    // Data
    private Tabata tabata;
    //Views
    private RelativeLayout tabataLayout;
    private LinearLayout principalLayout;
    private Map<String, TextView> stepTextView;
    private Map<String, TextView> stepEditView;
    private Map<String, RelativeLayout> stepLayout;
    private Map<String, String> stepName;
    private Map<String, Integer> stepColor;


    private DatabaseClient mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Data initialisation
        tabata = new Tabata();
        stepTextView = new HashMap<>();
        stepEditView = new HashMap<>();
        stepLayout = new HashMap<>();
        stepName = tabata.getStepName();
        stepColor = tabata.getStepColor();
        setContentView(R.layout.activity_edit);


        mDb = DatabaseClient.getInstance(getApplicationContext());
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tabata").build();


        //Get views
        principalLayout = (LinearLayout) findViewById(R.id.principal_layout);
        //Event

        //Graphic update
        for(String step : tabata.getTabataStep()){
            if(step == "tabataNb" || step == "cycleNb") {
                tabataLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.edit_repetition, null);
            } else {
                tabataLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.edit_time, null);
            }
            GradientDrawable background = (GradientDrawable) tabataLayout.getBackground().getCurrent();
            background.setColor(stepColor.get(step));
            stepTextView.put(step, (TextView) tabataLayout.findViewById(R.id.step_name));
            stepEditView.put(step, (TextView) tabataLayout.findViewById(R.id.step_value));
            stepLayout.put(step, tabataLayout);
            stepTextView.get(step).setText(stepName.get(step));
            update(step);
            ((ImageButton) tabataLayout.findViewById(R.id.step_add)).setTag(step);
            ((ImageButton) tabataLayout.findViewById(R.id.step_remove)).setTag(step);
            switch (step) {
                case "tabataNb": principalLayout.addView(tabataLayout);
                    break;
                case "workTime": ((LinearLayout) stepLayout.get("cycleNb").findViewById(R.id.repetition_layout)).addView(tabataLayout);
                    break;
                case "restTime": ((LinearLayout) stepLayout.get("cycleNb").findViewById(R.id.repetition_layout)).addView(tabataLayout);
                    break;
                default: ((LinearLayout) stepLayout.get("tabataNb").findViewById(R.id.repetition_layout)).addView(tabataLayout);
                    break;
            }
        }
    }

    public void start(View view) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("tabata",this.tabata);
        startActivity(intent);
    }

    public void add(View view) {
        String step = ((ImageButton)view).getTag().toString();
        tabata.add(step);
        update(step);
    }

    public void remove(View view) {
        String step = ((ImageButton)view).getTag().toString();
        tabata.remove(step);
        update(step);
    }

    public void update(String step) {
        stepEditView.get(step).setText(Integer.toString(tabata.getValue(step)));
    }
}
