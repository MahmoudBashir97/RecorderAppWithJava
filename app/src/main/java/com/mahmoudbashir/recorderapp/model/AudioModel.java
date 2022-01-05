package com.mahmoudbashir.recorderapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AudioModel {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "audio_name")
    String audioName;
    @ColumnInfo(name = "path_name")
    String pathName;

    public AudioModel(int id, String audioName, String pathName) {
        this.id = id;
        this.audioName = audioName;
        this.pathName = pathName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}
