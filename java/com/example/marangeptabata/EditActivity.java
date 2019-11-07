package com.example.marangeptabata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.marangeptabata.db.DatabaseClient;
import com.example.marangeptabata.db.SaveTabata;
import com.example.marangeptabata.model.Tabata;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    // Data
    private Tabata tabata;
    //Views
    private LinearLayout tabataLayout;
    private LinearLayout principalLayout;
    private Map<String, TextView> stepTextView;
    private Map<String, TextView> stepEditView;
    private Map<String, LinearLayout> stepLayout;
    private Map<String, String> stepName;
    private Map<String, Integer> stepColor;
    private BottomNavigationView navigation;

    private SaveTabata save;
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

        //Get views
        principalLayout = (LinearLayout) findViewById(R.id.principal_layout);
        navigation = (BottomNavigationView) findViewById(R.id.menu);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        //Event

        //Graphic update
        for(String step : tabata.getTabataStep()){
            if(step == "tabataNb" || step == "cycleNb") {
                tabataLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.edit_repetition, null);
            } else {
                tabataLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.edit_time, null);
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
                case "prepareTime" :
                case "tabataNb": principalLayout.addView(tabataLayout);
                    break;
                case "workTime":
                case "restTime": ((LinearLayout) stepLayout.get("cycleNb").findViewById(R.id.repetition_layout)).addView(tabataLayout);
                    break;
                default: ((LinearLayout) stepLayout.get("tabataNb").findViewById(R.id.repetition_layout)).addView(tabataLayout);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.menu_home);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    return true;
                case R.id.menu_list:
                    startListActivity();
                    return true;
            }
            return false;
        }
    };

    private void startTimerActivity() {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("tabata",this.tabata);
        startActivity(intent);
    }

    private void startSaveActivity() {
        Intent intent = new Intent(this, SaveActivity.class);
        intent.putExtra("tabata",this.tabata);
        startActivity(intent);
    }

    private void startListActivity() {
        Intent intent = new Intent(this, ListActivity.class);
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

    public void onSave(View view) {
        startSaveActivity();
    }

    public void onPlay(View view) {
        startTimerActivity();
    }
}
