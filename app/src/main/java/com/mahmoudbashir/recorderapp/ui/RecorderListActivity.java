package com.mahmoudbashir.recorderapp.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mahmoudbashir.recorderapp.R;
import com.mahmoudbashir.recorderapp.adapters.AudioListAdapter;
import com.mahmoudbashir.recorderapp.model.AudioModel;
import com.mahmoudbashir.recorderapp.viewModel.ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private BottomSheetBehavior bottomSheetBehavior;
    private ConstraintLayout playerSheet;

    //UI Elements
    private ImageButton playBtn;
    private TextView playerHeader;
    private TextView playerFilename;

    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;

    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    private File fileToPlay = null;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.recorder_list_activity);

        doInitialize();
        setUpRecyclerView();
        getDataFromLocal();
        bottomSheetInvokeCallback();
    }
    private void bottomSheetInvokeCallback(){

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //We cant do anything here for this app
            }
        });


        playBtn.setOnClickListener(v -> {
            if(isPlaying){
                pauseAudio();
            } else {
                if(fileToPlay != null){
                    resumeAudio();
                }
            }
        });

    }

    private void resumeAudio() {
        mediaPlayer.start();
        playBtn.setImageDrawable(this.getResources().getDrawable(R.drawable.player_pause_btn, null));
        isPlaying = true;

        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playBtn.setImageDrawable(this.getResources().getDrawable(R.drawable.player_play_btn, null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    public void doInitialize(){
        rec_audio = findViewById(R.id.rec_audios);
        playerSheet = findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        playBtn = findViewById(R.id.player_play_btn);
        playerHeader = findViewById(R.id.player_header_title);
        playerFilename = findViewById(R.id.player_filename);
        playerSeekbar = findViewById(R.id.player_seekbar);





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
      //  startPlaying(audioModel.getPathName());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        File directory = new File(audioModel.getPathName());
        fileToPlay = directory.getAbsoluteFile();


        if(isPlaying){
            stopAudio();
            playAudio(fileToPlay);
        } else {
            playAudio(fileToPlay);
        }

    }

    private void playAudio(File fileToPlay) {
        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playBtn.setImageDrawable(this.getResources().getDrawable(R.drawable.player_pause_btn, null));
        playerFilename.setText(fileToPlay.getName());
        playerHeader.setText("Playing");
        //Play the audio
        isPlaying = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

        playerSeekbar.setMax(mediaPlayer.getDuration());

        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void stopAudio() {
        //Stop The Audio
        playBtn.setImageDrawable(this.getResources().getDrawable(R.drawable.player_play_btn, null));
        playerHeader.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
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

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying) stopAudio();
    }
}