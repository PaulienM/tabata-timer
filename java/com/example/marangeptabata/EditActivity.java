package com.example.marangeptabata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.marangeptabata.db.AppDatabase;
import com.example.marangeptabata.db.DatabaseClient;
import com.example.marangeptabata.db.GetTabatas;
import com.example.marangeptabata.db.SaveTabata;
import com.example.marangeptabata.model.Tabata;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private GetTabatas getTabatas;
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
        save = new SaveTabata();
        getTabatas = new GetTabatas();
        save.setDatabase(mDb);
        save.setTabata(tabata);
        getTabatas.setDatabase(mDb);


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

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_save:
                    return true;
                case R.id.menu_run:
                    startTimerActivity();
                    return true;
                case R.id.menu_home:
                    return true;
                case R.id.menu_list:
                    //startListActivity();
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

    public void add(View view) {
        String step = ((ImageButton)view).getTag().toString();
        tabata.add(step);
        update(step);
        //save.execute();
    }

    public void remove(View view) {
        String step = ((ImageButton)view).getTag().toString();
        tabata.remove(step);
        update(step);
        ArrayList<Tabata> tabatas = new ArrayList<>();
        //getTabatas.execute();
    }

    public void update(String step) {
        stepEditView.get(step).setText(Integer.toString(tabata.getValue(step)));
    }
}
