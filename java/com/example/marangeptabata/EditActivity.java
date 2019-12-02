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

import com.example.marangeptabata.Libraries.RepeatListener;
import com.example.marangeptabata.model.Tabata;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    // Data

    // Objet représentant une scéance
    private Tabata tabata;
    // True si nous somme dans le cas de l'édition d'une scéance existant en base de donnée
    private Boolean update;

    //Views

    // Layout contenant tous les elements d'édition de la scéance
    private LinearLayout tabataLayout;
    // Layout contenant toute l'activitée
    private LinearLayout principalLayout;
    // Map reliant l'identifiant d'étape et le textview contenant le temps de l'étape ou le nombre de repetitions
    private Map<String, TextView> stepEditView;
    // Map reliant l'identifiant d'étape et le linearlayout contenant les elements d'edition d'une etape
    private Map<String, LinearLayout> stepLayout;
    // Map reliant l'identifiant d'étape et le nom de l'étape (le nom qui sera affiché)
    private Map<String, String> stepName;
    // Map reliant l'identifiant d'étape et la couleur de l'étape
    private Map<String, Integer> stepColor;
    // Bar de navigation inferieur
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Data initialisation
        // Si l'on vient de l'activité list, et que donc il existe un intent, on recupere la valeur du tabata donné en paramètre
        if(getIntent().getParcelableExtra("tabata") != null) {
            tabata = getIntent().getParcelableExtra("tabata");
            // On definit les valeurs de nom et de couleur du tabata (correspond a stepName et stepColor)
            tabata.updateValues();
            // Nous somme dans le cas d'une mise a jour d'une scéance existant deja en base de donnée
            update = true;
        // Si l'on se trouve dans le cas d'un changement d'orientation
        } else if (savedInstanceState != null) {
            // On recupere les valeurs de la scéance
            tabata = (Tabata) savedInstanceState.getParcelable("tabata");
            tabata.updateValues();
        // Si nous sommes dans le cas ou nous n'avons pas de paramètres
        } else {
            // On créé une nouvelle scéance
            tabata = new Tabata();
            // Nous ne sommes pas dans le cas d'une mise a jour d'une scéance existante
            update = false;
        }
        // Initialisation des variables
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
        // Pour toutes les étapes de la scéance
        for(String step : tabata.getTabataStep()){
            // Si nous sommes dans le cas du nombre de cycle ou du nombre de repetition
            if(step == "tabataNb" || step == "cycleNb") {
                // Nous selectionnons un template particulier
                tabataLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.edit_repetition, null);
            } else {
                // Sinon, nous recuperons le template classique
                tabataLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.edit_time, null);
            }
            // On recupère l'arriere plan du layout de l'étape
            GradientDrawable background = (GradientDrawable) tabataLayout.getBackground().getCurrent();
            // On le met de la couleur de l'étape
            background.setColor(stepColor.get(step));
            // On met le text view correspondant à la valeur du de l'étape dans un HashMap
            stepEditView.put(step, (TextView) tabataLayout.findViewById(R.id.step_value));
            // On met le layout comportant tous les elements propre à cette étape dans une hashmap
            stepLayout.put(step, tabataLayout);
            // On recupere la textview correspondant au nom de l'étape et on lui set le nom de l'étape
            ((TextView) tabataLayout.findViewById(R.id.step_name)).setText(stepName.get(step));
            // On fait un update de la valeur de cette étape
            update(step);
            // On ajoute l'id de l'étape en tag sur les boutons
            ((ImageButton) tabataLayout.findViewById(R.id.step_add)).setTag(step);
            ((ImageButton) tabataLayout.findViewById(R.id.step_remove)).setTag(step);
            // On ajoute un listener sur les boutons
            ((ImageButton) tabataLayout.findViewById(R.id.step_add)).setOnTouchListener(new RepeatListener(400, 70, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add(view);
                }
            }));
            ((ImageButton) tabataLayout.findViewById(R.id.step_remove)).setOnTouchListener(new RepeatListener(400, 70, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove(view);
                }
            }));
            switch (step) {
                // Si l'étape est la préparation ou le nombre de répétition on ajoute son layout au layout principal
                case "prepareTime" :
                case "tabataNb": principalLayout.addView(tabataLayout);
                    break;
                // Si l'étape est un temps de travail ou un temps de repos, on ajoute son layout dans le layout du nombre
                // de cycle
                case "workTime":
                case "restTime": ((LinearLayout) stepLayout.get("cycleNb").findViewById(R.id.repetition_layout)).addView(tabataLayout);
                    break;
                // Dans les autres cas on ajoute le layout de l'étape dans le layout du nombre de répétitions
                default: ((LinearLayout) stepLayout.get("tabataNb").findViewById(R.id.repetition_layout)).addView(tabataLayout);
                    break;
            }
        }
    }

    @Override
    // On change l'onglet selectioné sur la bar de navigation
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.menu_home);
    }

    // On defini les action pour chaque onglet de la bar de navigation
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

    // On lance l'activité du timer en lui mettant en parametre l'objet tabata
    private void startTimerActivity() {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("tabata",this.tabata);
        startActivity(intent);
    }

    // On lance l'activitée qui permet de sauvegarder le tabata en lui donnant l'objet tabata en parametre
    private void startSaveActivity() {
        Intent intent = new Intent(this, SaveActivity.class);
        intent.putExtra("tabata",this.tabata);
        intent.putExtra("update", this.update);
        startActivity(intent);
    }

    // On lance l'activité qui liste les séances
    private void startListActivity() {
        Intent intent = new Intent(this, ListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // Incremente le temps d'une étape
    public void add(View view) {
        String step = ((ImageButton)view).getTag().toString();
        tabata.add(step);
        update(step);
    }

    // Decremente le temps d'une étape
    public void remove(View view) {
        String step = ((ImageButton)view).getTag().toString();
        tabata.remove(step);
        update(step);
    }

    // Methode permettant de récuperer la valeur du temps pour une étape et de l'afficher dans le
    // Text view corresondant
    public void update(String step) {
        stepEditView.get(step).setText(tabata.getValue(step));
        tabata.getTabataCycle();
        setTitle("Tabata : (" + tabata.getDuration() + ")");
    }

    // Lorsque l'on appuie sur save, on lance l'activité save
    public void onSave(View view) {
        startSaveActivity();
    }

    // Lorsque l'on appuie sur play, on lance l'activité timer
    public void onPlay(View view) {
        startTimerActivity();
    }

    // On enregistre l'objet tabtata avant que l'activité soit detruite
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("tabata", this.tabata);
    }
}
