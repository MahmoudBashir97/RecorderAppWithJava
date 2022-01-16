package com.mahmoudbashir.recorderapp.room;

import com.mahmoudbashir.recorderapp.model.AudioModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface AudioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAudio(AudioModel audioModel);

    @Query("SELECT * FROM AudioModel WHERE id LIKE :id")
    Observable<AudioModel> getAudioById(int id);


    @Query("SELECT * FROM AudioModel Order by id ASC")
    Single<List<AudioModel>> getAllAudios();

}
