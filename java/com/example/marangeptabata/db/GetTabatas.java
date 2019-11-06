package com.example.marangeptabata.db;

import android.os.AsyncTask;

import com.example.marangeptabata.model.Tabata;

import java.util.List;

public class GetTabatas extends AsyncTask<Void, Void, List<Tabata>> {
    private DatabaseClient mDb;
    private List<Tabata> tabatas;

    public void setDatabase(DatabaseClient mDb) {
        this.mDb = mDb;
    }

    @Override
    protected List<Tabata> doInBackground(Void... voids) {
        List<Tabata> tabataList = mDb.getAppDatabase()
                .tabataDao()
                .getAll();
        return tabataList;
    }

    @Override
    protected void onPostExecute(List<Tabata> tabatas) {
        super.onPostExecute(tabatas);
        this.tabatas = tabatas;
        for (Tabata tabata: tabatas) {
            System.out.println(tabata);
        }
    }
}
