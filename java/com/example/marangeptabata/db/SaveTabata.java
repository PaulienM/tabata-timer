package com.example.marangeptabata.db;

import android.os.AsyncTask;

import com.example.marangeptabata.model.Tabata;

public class SaveTabata extends AsyncTask<Void, Void, Tabata> {
    private Tabata tabata;
    private DatabaseClient mDb;

    public void setTabata(Tabata tabata) {
        this.tabata = tabata;
    }

    public void setDatabase(DatabaseClient mDb) {
        this.mDb = mDb;
    }

    @Override
    protected Tabata doInBackground(Void... voids) {
        mDb.getAppDatabase()
                .tabataDao()
                .insert(tabata);
        return tabata;
    }

    @Override
    protected void onPostExecute(Tabata tabata) {
        super.onPostExecute(tabata);
        System.out.println("Saved");
    }
}
