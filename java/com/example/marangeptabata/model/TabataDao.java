package com.example.marangeptabata.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TabataDao {
    @Query("SELECT * FROM tabata")
    List<Tabata> getAll();

    @Query("SELECT * FROM tabata WHERE id = :id")
    Tabata findById(long id);

    @Insert
    long insert(Tabata tabata);

    @Update
    void update(Tabata tabata);

    @Delete
    void delete(Tabata tabata);
}
