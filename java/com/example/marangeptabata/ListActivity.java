package com.example.marangeptabata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marangeptabata.db.DatabaseClient;
import com.example.marangeptabata.model.Tabata;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    //data
    private DatabaseClient mDb;
    private List<Tabata> allTabatas;
    //views
    private LinearLayout tabataList;
    private LinearLayout listItem;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDb = DatabaseClient.getInstance(getApplicationContext());
        getTabata();

        tabataList = findViewById(R.id.tabataList);
        navigation = (BottomNavigationView) findViewById(R.id.menu);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.menu_list);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    startEditActivity();
                    return true;
                case R.id.menu_list:
                    return true;
            }
            return false;
        }
    };

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

    private void findById(long id, final boolean startTimerActivity, final boolean startEditActivity, final boolean removeAfterFind) {
        class FindById extends AsyncTask<Void, Void, Tabata> {
            private long id;

            public void setId(long id) {
                this.id = id;
            }

            @Override
            protected Tabata doInBackground(Void... voids) {
                Tabata tabata = mDb.getAppDatabase()
                        .tabataDao()
                        .findById(id);
                return tabata;
            }

            @Override
            protected void onPostExecute(Tabata tabata) {
                super.onPostExecute(tabata);
                if(startEditActivity) {
                    startEditActivity(tabata);
                }
                if(startTimerActivity) {
                    startTimerActivity(tabata);
                }
                if(removeAfterFind) {
                    removeTabata(tabata);
                }
            }
        }
        FindById findById = new FindById();
        findById.setId(id);
        findById.execute();
    }

    private void removeTabata(final Tabata tabata) {
        class RemoveTabata extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                mDb.getAppDatabase()
                        .tabataDao()
                        .delete(tabata);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getTabata();
            }
        }
        RemoveTabata removeTabata = new RemoveTabata();
        removeTabata.execute();
    }

    private void displayTabatas() {
        tabataList.removeAllViews();
        for(Tabata tabata : allTabatas) {
            listItem = (LinearLayout) getLayoutInflater().inflate(R.layout.tabata_list_item, null);
            listItem.setTag(tabata.getId());
            ((TextView) listItem.findViewById(R.id.tabata_list_name)).setText(tabata.getName());
            ((TextView) listItem.findViewById(R.id.tabata_list_work_time)).setText("Travail : " + tabata.getWorkTime());
            ((TextView) listItem.findViewById(R.id.tabata_list_rest_time)).setText("Repos : " + tabata.getRestTime());
            ((TextView) listItem.findViewById(R.id.tabata_list_cylce_nb)).setText("Cycle : x" + tabata.getCycleNb());
            ((TextView) listItem.findViewById(R.id.tabata_list_tabata_nb)).setText("Repetitions : x" + tabata.getTabataNb());
            ((ImageButton) listItem.findViewById(R.id.removeButton)).setTag(tabata.getId());
            ((ImageButton) listItem.findViewById(R.id.editButton)).setTag(tabata.getId());
            tabataList.addView(listItem);
        }
    }

    public void startTabata(View view) {
        findById(Long.parseLong(view.getTag().toString()), true, false, false);
    }

    private void startTimerActivity(Tabata tabata) {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("tabata",tabata);
        startActivity(intent);
    }

    private void startEditActivity(Tabata tabata) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("tabata",tabata);
        startActivity(intent);
    }

    private void startEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void onEdit(View view) {
        findById(Long.parseLong(view.getTag().toString()), false, true, false);
    }

    public void onRemove(View view) {
        System.out.println("la");
        findById(Long.parseLong(view.getTag().toString()), false, false, true);
    }
}
