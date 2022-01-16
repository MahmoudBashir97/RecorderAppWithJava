package com.mahmoudbashir.recorderapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.mahmoudbashir.recorderapp.adapters.AudioListAdapter;
import com.mahmoudbashir.recorderapp.model.AudioModel;
import com.mahmoudbashir.recorderapp.viewModel.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecorderListActivity extends AppCompatActivity implements AudioListAdapter.OnClickItemInterface {

    private static final String TAG = "FirstFragment";
    
    ViewModel audioVM;
    RecyclerView rec_audio;
    AudioListAdapter listAdapter;
    List<AudioModel> audioModelList = new ArrayList<>();

    // private PlayButton   playButton = null;
    private MediaPlayer player = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.recorder_list_activity);

        doInitialize();
        setUpRecyclerView();
        getDataFromLocal();
    }

    public void doInitialize(){
        rec_audio = findViewById(R.id.rec_audios);
        audioVM = ViewModelProviders.of(this).get(ViewModel.class);
    }

    public void setUpRecyclerView()
    {

        listAdapter = new AudioListAdapter(this,audioModelList,this);
        rec_audio.setHasFixedSize(true);
        rec_audio.setLayoutManager(new LinearLayoutManager(this));
        rec_audio.setAdapter(listAdapter);

    }

    public void getDataFromLocal(){
        audioVM.getAllAudios().observe(this, new Observer<List<AudioModel>>() {
            @Override
            public void onChanged(List<AudioModel> audioModels) {
                if (audioModels != null){
                audioModelList.addAll(audioModels);

                Log.d("sizeList: ","size : "+audioModels.size());
                listAdapter.updateList(audioModels);
                }
            }
        });
    }

    @Override
    public void onClick(AudioModel audioModel, int position) {
      //  Toast.makeText(this, "file name : "+audioModel.getPathName(), Toast.LENGTH_SHORT).show();
        startPlaying(audioModel.getPathName());
    }

    private void startPlaying(String fileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }
}