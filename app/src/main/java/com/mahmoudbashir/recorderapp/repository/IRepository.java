package com.mahmoudbashir.recorderapp.repository;

import com.mahmoudbashir.recorderapp.model.AudioModel;

import java.util.List;

import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IRepository {

    Completable insertAudio(AudioModel audioModel);

    Observable<AudioModel> getAudioById(int idQ );

    Single<List<AudioModel>> getAllAudios();
}
