package com.mahmoudbashir.recorderapp.repository;

import android.app.Application;

import com.mahmoudbashir.recorderapp.model.AudioModel;
import com.mahmoudbashir.recorderapp.room.AudioDatabase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class AudioRepository implements IRepository{

    private AudioDatabase db;

    public AudioRepository(Application app){
        db = AudioDatabase.getIstance(app.getApplicationContext());
    }

    @Override
    public Completable insertAudio(AudioModel audioModel) {
        return db.dao().insertAudio(audioModel);
    }

    @Override
    public Observable<AudioModel> getAudioById(int idQ) {
        return db.dao().getAudioById(idQ);
    }

    @Override
    public Single<List<AudioModel>> getAllAudios() {
        return db.dao().getAllAudios();
    }

}