package com.mahmoudbashir.recorderapp.viewModel;

import android.app.Application;

import com.mahmoudbashir.recorderapp.model.AudioModel;
import com.mahmoudbashir.recorderapp.repository.AudioRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ViewModel extends AndroidViewModel {

    private AudioRepository repo;
    MutableLiveData<List<AudioModel>> modelList = new MutableLiveData();

    public ViewModel(@NonNull Application application) {
        super(application);

        repo = new AudioRepository(application);
    }

   public Completable insertAudio(AudioModel audioModel){
        return repo.insertAudio(audioModel);
    }

    public LiveData<List<AudioModel>> getAllAudios(){
        repo.getAllAudios().subscribeOn(Schedulers.io()).observeOn(Schedulers.single()).subscribe(new SingleObserver<List<AudioModel>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull List<AudioModel> audioModels) {
                if (audioModels != null){
                    modelList.postValue(audioModels);
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });

        return modelList;
    }

}
