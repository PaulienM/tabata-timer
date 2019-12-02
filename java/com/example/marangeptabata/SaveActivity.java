package com.example.marangeptabata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.marangeptabata.db.DatabaseClient;
import com.example.marangeptabata.model.Tabata;

// activité permettant de sauvegarder un tabata
public class SaveActivity extends AppCompatActivity {

    private Tabata tabata;
    private boolean update;
    private DatabaseClient mDb;
    private EditText editTabataName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        // On recupere la valeur du tabata donné en intent
        //Data init
        tabata = getIntent().getParcelableExtra("tabata");
        // On verifie s'il s'agit d'une mise a jour d'un tabata existent ou non
        update = getIntent().getBooleanExtra("update", false);
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //Get view
        editTabataName = findViewById(R.id.editTabataName);
        editTabataName.setText(tabata.getName());
    }

    private void saveTabata() {
        class SaveTabata extends AsyncTask<Void, Void, Tabata> {

            @Override
            protected Tabata doInBackground(Void... voids) {
                // S'il s'agit d'une mise a jour, on utilise la methode update
                if(update) {
                    mDb.getAppDatabase()
                        .tabataDao()
                        .update(tabata);
                // Sinon, on utilise la methode insert
                } else {
                    mDb.getAppDatabase()
                        .tabataDao()
                        .insert(tabata);
                }
                return tabata;
            }

            @Override
            protected void onPostExecute(Tabata tabata) {
                super.onPostExecute(tabata);
            }
        }
        SaveTabata saveTabata = new SaveTabata();
        saveTabata.execute();
    }

    // Lorsque l'on appuie sur le bouton enregistrer, ajoute le nom a l'objet tabata, on l'enregistre
    // avec la methode saveTabata, puis on lance l'activit" liste
    public void saveTabata(View view) {
        tabata.setName(editTabataName.getText().toString());
        saveTabata();
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
