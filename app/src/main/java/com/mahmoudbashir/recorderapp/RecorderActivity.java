package com.mahmoudbashir.recorderapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import com.github.znacloud.RippleView;
import com.mahmoudbashir.recorderapp.model.AudioModel;
import com.mahmoudbashir.recorderapp.viewModel.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RecorderActivity extends AppCompatActivity {


    private static final String TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

   // private PlayButton   playButton = null;
    private MediaPlayer player = null;

    private Chronometer timer;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }


   /* class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }*/


    AlertDialog dialog;
    ImageView record_btn;
    TextView counter_timer_tv;
    boolean mStartPlaying = true;
    boolean mStartRecording = true;
    Button btn_playRecentRecord;
    RippleView btn_ripple_v;
    ViewModel audioVM;
    String curr_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        doInitialize();

        // Record to the external cache directory for visibility
        //fileName = getExternalCacheDir().getAbsolutePath();



        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

       /* LinearLayout ll = new LinearLayout(this);
        recordButton = new RecordButton(this);
        ll.addView(recordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        playButton = new PlayButton(this);
        ll.addView(playButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);*/

        record_btn.setOnClickListener(v -> {
            onRecord(mStartRecording);
            if (mStartRecording) {
                Log.d(TAG,"record started");
            } else {
                Log.d(TAG,"record stopped");
            }
            mStartRecording = !mStartRecording;
        });

        btn_playRecentRecord.setOnClickListener(v -> {

            startActivity(new Intent(RecorderActivity.this, RecorderListActivity.class));

            /*audioVM.getAllAudios().observe(this, new Observer<List<AudioModel>>() {
                @Override
                public void onChanged(List<AudioModel> audioModels) {
                    showToast("size : "+audioModels.size()+" name : "+audioModels.get(0).getPathName());
                }
            });*/


            /*onPlay(mStartPlaying);
            if (mStartPlaying) {
                Log.d(TAG,"record played");
            } else {
                Log.d(TAG,"record stopped");
            }
            mStartPlaying = !mStartPlaying;*/
        });

    }

    public void showToast(String text){
        Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();
    }

    public void doInitialize(){

        record_btn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);
        btn_playRecentRecord = findViewById(R.id.btn_playRecentRecord);
        btn_ripple_v = findViewById(R.id.btn_ripple_v);
        btn_ripple_v.setVisibility(View.GONE);

        audioVM = ViewModelProviders.of(this).get(ViewModel.class);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
            record_btn.setImageResource(R.drawable.ic_stop_recording);
            btn_ripple_v.setVisibility(View.VISIBLE);
        } else {
            stopRecording();
            record_btn.setImageResource(R.drawable.rec_icon1);
            btn_ripple_v.setVisibility(View.GONE);
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
            record_btn.setImageResource(R.drawable.ic_stop_recording);
        } else {
            stopPlaying();
            record_btn.setImageResource(R.drawable.rec_icon1);
        }
    }

        private void startPlaying() {
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

        private void startRecording(){
            // Record to the external cache directory for visibility
            fileName = getExternalCacheDir().getAbsolutePath();

            String timeStamp = String.valueOf(System.currentTimeMillis());
            fileName += "/audiorecord"+timeStamp+".3gp";
            curr_time = timeStamp;

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(fileName);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            try {
                recorder.prepare();
                recorder.start();
            } catch (IOException e) {
                Log.e(TAG, "prepare() failed");
            }


        }

        private void stopRecording(){
            timer.stop();
            timer.setBase(SystemClock.elapsedRealtime());

            Log.d("timerM : ",timer.getFormat()+"");

            byte[] array = new byte[7]; // length is bounded by 7
            new Random().nextBytes(array);
            String generatedString = new String(array, StandardCharsets.UTF_8);
            String audName = "AUD"+curr_time;

            try{
                recorder.stop();
                recorder.release();
                recorder = null;
            }catch (Exception e){
                Log.e(TAG,"stopped failed"+e);
            }

            //Get current date and time
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date now = new Date();

            showDialogToSaveNewRecord(audName,fileName,formatter.format(now));
        }

    private void showDialogToSaveNewRecord(String audName,String flname,String audDate) {
        dialog =new  AlertDialog.Builder(this)
                .setTitle("Saving "+audName)
                .setMessage("Are you sure to save this new Record?")
                .setNegativeButton("Cancel",(dialog1, which) ->
                {
                    dialog1.dismiss();
                }).setPositiveButton("Ok",(dialog1, which) -> {
                    AudioModel model = new AudioModel();
                    model.setAudioName(audName);
                    model.setPathName(flname);
                    model.setAudioDate(audDate);

                    audioVM.insertAudio(model).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            showToast("Record saved successfully!");
                            dialog1.dismiss();
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            showToast("some errors !"+e.getMessage());
                        }
                    });
                })
                .setCancelable(false)
                .create();
        dialog.show();
    }
}