package com.mahmoudbashir.recorderapp.room;

import android.content.Context;

import com.mahmoudbashir.recorderapp.model.AudioModel;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = AudioModel.class,exportSchema = false,version = 1)
public  abstract class AudioDatabase extends RoomDatabase {

    private static AudioDatabase instance;
    public abstract AudioDao dao();


    public static synchronized AudioDatabase getIstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AudioDatabase.class,
                    "audio_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}