package com.example.marangeptabata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Data
    private String[] tabataCycle;
    //Views
    private RelativeLayout tabataLayout;
    private LinearLayout principalLayout;
    private TextView stepName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Data initialisation
        tabataCycle = new String[] {"Préparation", "Séquence", "Cycle", "Travail", "Repos", "Repos long"};
        setContentView(R.layout.activity_main);
        //Get views
        principalLayout = (LinearLayout) findViewById(R.id.principal_layout);
        //Event

        //Graphic update
        for(String step : tabataCycle){
            tabataLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.tabata, null);
            stepName = (TextView) findViewById(R.id.step_name);
            stepName.setText(step);
            principalLayout.addView(tabataLayout);
        }
        principalLayout.addView(tabataLayout);
    }


    public void start(View view) {
        Toast.makeText(getApplicationContext(),"LANCEMENT DU TIMER", Toast.LENGTH_LONG).show();
    }
}
