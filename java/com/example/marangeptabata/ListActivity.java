package com.example.marangeptabata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marangeptabata.db.DatabaseClient;
import com.example.marangeptabata.model.Tabata;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    //data
    private DatabaseClient mDb;
    private List<Tabata> allTabatas;
    //views
    private LinearLayout tabataList;
    private LinearLayout listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDb = DatabaseClient.getInstance(getApplicationContext());
        getTabata();

        tabataList = findViewById(R.id.tabataList);
    }

    private void getTabata() {
        class GetTabatas extends AsyncTask<Void, Void, List<Tabata>> {
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
                allTabatas = tabatas;
                displayTabatas();
            }
        }
        GetTabatas getTabatas = new GetTabatas();
        getTabatas.execute();
    }

    private void findByIdAndStartTimer(long id) {
        class FindById extends AsyncTask<Void, Void, Tabata> {
            private long id;

            public void setId(long id) {
                this.id = id;
            }

            @Override
            protected Tabata doInBackground(Void... voids) {
                System.out.println(id);
                Tabata tabata = mDb.getAppDatabase()
                        .tabataDao()
                        .findById(id);
                return null;
            }

            @Override
            protected void onPostExecute(Tabata tabata) {
                super.onPostExecute(tabata);
                startTimerActivity(tabata);
            }
        }
        FindById findById = new FindById();
        findById.setId(id);
        findById.execute();
    }

    private void displayTabatas() {
        for(Tabata tabata : allTabatas) {
            listItem = (LinearLayout) getLayoutInflater().inflate(R.layout.tabata_list_item, null);
            listItem.setTag(tabata.getId());
            ((TextView) listItem.findViewById(R.id.tabata_list_name)).setText(tabata.getName());
            ((TextView) listItem.findViewById(R.id.tabata_list_work_time)).setText("Travail : " + tabata.getWorkTime());
            ((TextView) listItem.findViewById(R.id.tabata_list_rest_time)).setText("Repos : " + tabata.getRestTime());
            ((TextView) listItem.findViewById(R.id.tabata_list_cylce_nb)).setText("Cycle : x" + tabata.getCycleNb());
            ((TextView) listItem.findViewById(R.id.tabata_list_tabata_nb)).setText("Repetitions : x" + tabata.getTabataNb());
            tabataList.addView(listItem);
        }
    }

    public void startTabata(View view) {
        findByIdAndStartTimer(Long.parseLong(view.getTag().toString()));
    }

    private void startTimerActivity(Tabata tabata) {
        System.out.println(tabata);
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("tabata",tabata);
        startActivity(intent);
    }
}
