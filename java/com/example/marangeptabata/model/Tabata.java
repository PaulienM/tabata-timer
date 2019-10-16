package com.example.marangeptabata.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabata")
public class Tabata {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "prepare_time")
    private int prepareTime;
    @ColumnInfo(name = "work_time")
    private int workTime;
    @ColumnInfo(name = "rest_time")
    private int restTime;
    @ColumnInfo(name = "long_rest_time")
    private int longRestTime;
    @ColumnInfo(name = "cycle_nb")
    private int cycleNb;
    @ColumnInfo(name = "tabata_nb")
    private int tabataNb;

    public Tabata(int prepareTime, int workTime, int restTime, int longRestTime, int cycleNb, int tabataNb) {
        this.prepareTime = prepareTime;
        this.workTime = workTime;
        this.restTime = restTime;
        this.longRestTime = longRestTime;
        this.cycleNb = cycleNb;
        this.tabataNb = tabataNb;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getLongRestTime() {
        return longRestTime;
    }

    public void setLongRestTime(int longRestTime) {
        this.longRestTime = longRestTime;
    }

    public int getCycleNb() {
        return cycleNb;
    }

    public void setCycleNb(int cycleNb) {
        this.cycleNb = cycleNb;
    }

    public int getTabataNb() {
        return tabataNb;
    }

    public void setTabataNb(int tabataNb) {
        this.tabataNb = tabataNb;
    }
}
