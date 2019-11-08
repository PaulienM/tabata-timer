package com.example.marangeptabata.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "tabata")
public class Tabata implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "name")
    private String name;
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
    @Ignore
    private static final String[] tabataStep = {"prepareTime", "tabataNb", "cycleNb", "workTime", "restTime", "longRestTime"};
    @Ignore
    private Map<String, String> stepName;
    @Ignore
    private Map<String, Integer> stepColor;
    @Ignore
    private ArrayList<String> tabataCycle;

    @Ignore
    public Tabata(int prepareTime, int workTime, int restTime, int longRestTime, int cycleNb, int tabataNb) {
        this.prepareTime = prepareTime;
        this.workTime = workTime;
        this.restTime = restTime;
        this.longRestTime = longRestTime;
        this.cycleNb = cycleNb;
        this.tabataNb = tabataNb;
        stepName = new HashMap<>();
        stepColor = new HashMap<>();
        tabataCycle = new ArrayList<>();
        updateStepName();
        updateStepColor();
    }

    public Tabata(){
        this.prepareTime = 10;
        this.workTime = 30;
        this.restTime = 10;
        this.longRestTime = 30;
        this.cycleNb = 5;
        this.tabataNb = 1;
        stepName = new HashMap<>();
        stepColor = new HashMap<>();
        tabataCycle = new ArrayList<>();
        updateStepName();
        updateStepColor();
    }

    protected Tabata(Parcel in) {
        id = in.readLong();
        name = in.readString();
        prepareTime = in.readInt();
        workTime = in.readInt();
        restTime = in.readInt();
        longRestTime = in.readInt();
        cycleNb = in.readInt();
        tabataNb = in.readInt();
        tabataCycle = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(prepareTime);
        dest.writeInt(workTime);
        dest.writeInt(restTime);
        dest.writeInt(longRestTime);
        dest.writeInt(cycleNb);
        dest.writeInt(tabataNb);
        dest.writeStringList(tabataCycle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tabata> CREATOR = new Creator<Tabata>() {
        @Override
        public Tabata createFromParcel(Parcel in) {
            return new Tabata(in);
        }

        @Override
        public Tabata[] newArray(int size) {
            return new Tabata[size];
        }
    };

    public void updateValues() {
        stepName = new HashMap<>();
        stepColor = new HashMap<>();
        this.updateStepColor();
        this.updateStepName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String[] getTabataStep() { return tabataStep; }

    public Map<String, String> getStepName() { return stepName; }

    public Map<String, Integer> getStepColor() { return stepColor; }

    public String getValue(String step) {
        //Switch don't work
        if(step == tabataStep[0]) {
            int secs = this.getPrepareTime();
            int mins = secs / 60;
            secs = secs % 60;
            return ("" + mins + ":" + String.format("%02d", secs));
        }
        if(step == tabataStep[1]) { return Integer.toString(this.getTabataNb()); }
        if(step == tabataStep[2]) { return Integer.toString(this.getCycleNb()); }
        if(step == tabataStep[3]) {
            int secs = this.getWorkTime();
            int mins = secs / 60;
            secs = secs % 60;
            return ("" + mins + ":" + String.format("%02d", secs));
        }
        if(step == tabataStep[4]) {
            int secs = this.getRestTime();
            int mins = secs / 60;
            secs = secs % 60;
            return ("" + mins + ":" + String.format("%02d", secs));
        }
        if(step == tabataStep[5]) {
            int secs = this.getLongRestTime();
            int mins = secs / 60;
            secs = secs % 60;
            return ("" + mins + ":" + String.format("%02d", secs));
        }
        else { throw new IllegalStateException("Unexpected value: " + step); }
    }

    public void remove(String step) {
        if(step == tabataStep[0] && this.prepareTime > 1) { this.setPrepareTime(this.prepareTime - 1); }
        if(step == tabataStep[1] && this.tabataNb > 1) { this.setTabataNb(this.tabataNb - 1); }
        if(step == tabataStep[2] && this.cycleNb > 1) { this.setCycleNb(this.cycleNb - 1); }
        if(step == tabataStep[3] && this.workTime > 1) { this.setWorkTime(this.workTime - 1); }
        if(step == tabataStep[4] && this.restTime > 1) { this.setRestTime(this.restTime - 1); }
        if(step == tabataStep[5] && this.longRestTime > 1) { this.setLongRestTime(this.longRestTime - 1); }
    }

    public void add(String step) {
        if(step == tabataStep[0] && this.prepareTime < 999) { this.setPrepareTime(this.prepareTime + 1); }
        if(step == tabataStep[1] && this.tabataNb < 999) { this.setTabataNb(this.tabataNb + 1); }
        if(step == tabataStep[2] && this.cycleNb < 999) { this.setCycleNb(this.cycleNb + 1); }
        if(step == tabataStep[3] && this.workTime < 999) { this.setWorkTime(this.workTime + 1); }
        if(step == tabataStep[4] && this.restTime < 999) { this.setRestTime(this.restTime + 1); }
        if(step == tabataStep[5] && this.longRestTime < 999) { this.setLongRestTime(this.longRestTime + 1); }
    }

    private void updateStepName() {
        stepName.put(tabataStep[0], "Préparation");
        stepName.put(tabataStep[1], "Séquence : ");
        stepName.put(tabataStep[2], "Cycle : ");
        stepName.put(tabataStep[3], "Travail");
        stepName.put(tabataStep[4], "Repos");
        stepName.put(tabataStep[5], "Repos long");
    }

    private void updateStepColor() {
        stepColor.put(tabataStep[0], 0xFFF9E000);
        stepColor.put(tabataStep[1], 0xFFF5F9FA);
        stepColor.put(tabataStep[2], 0xFFECF6FF);
        stepColor.put(tabataStep[3], 0xFFFF7658);
        stepColor.put(tabataStep[4], 0xFFA0D800);
        stepColor.put(tabataStep[5], 0xFF72EEFF);
    }

    public ArrayList getTabataCycle() {
        this.tabataCycle.clear();

        this.tabataCycle.add("preparation");
        for(int repetition = 1; repetition <= this.getTabataNb(); repetition++) {
            for(int cycle = 1; cycle <= this.getCycleNb(); cycle++) {
                this.tabataCycle.add("work");
                if(cycle != this.getCycleNb()) {
                    this.tabataCycle.add("rest");
                }
            }
            if(repetition != this.getTabataNb()) {
                this.tabataCycle.add("long_rest");
            }
        }
        return this.tabataCycle;
    }

    public String getDuration() {
        int duration = 0;
        for(String step : this.tabataCycle) {
            switch (step) {
                case "work" : duration += this.getWorkTime();
                    break;
                case "rest" : duration += this.getRestTime();
                    break;
                case "preparation" : duration += this.getPrepareTime();
                    break;
                case "long_rest" : duration += this.getLongRestTime();
                    break;
                default: break;
            }
        }
        int secs = duration;
        int mins = secs / 60;
        secs = secs % 60;

        return ("" + mins + ":" + String.format("%02d", secs));
    }
}
