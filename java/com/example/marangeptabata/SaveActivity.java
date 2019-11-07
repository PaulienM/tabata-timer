package com.example.marangeptabata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.marangeptabata.db.DatabaseClient;
import com.example.marangeptabata.model.Tabata;

public class SaveActivity extends AppCompatActivity {

    private Tabata tabata;
    private boolean update;
    private DatabaseClient mDb;
    private EditText editTabataName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        //Data init
        tabata = getIntent().getParcelableExtra("tabata");
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
                if(update) {
                    mDb.getAppDatabase()
                        .tabataDao()
                        .update(tabata);
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
                System.out.println("Saved");
            }
        }
        SaveTabata saveTabata = new SaveTabata();
        saveTabata.execute();
    }

    public void saveTabata(View view) {
        tabata.setName(editTabataName.getText().toString());
        saveTabata();
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
