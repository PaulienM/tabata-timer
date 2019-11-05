package com.example.marangeptabata.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.marangeptabata.model.Tabata;
import com.example.marangeptabata.model.TabataDao;

@Database(entities = {Tabata.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TabataDao tabataDao();
}
