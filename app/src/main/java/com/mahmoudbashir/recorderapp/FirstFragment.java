package com.mahmoudbashir.recorderapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoudbashir.recorderapp.adapters.AudioListAdapter;
import com.mahmoudbashir.recorderapp.model.AudioModel;
import com.mahmoudbashir.recorderapp.viewModel.ViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

public class FirstFragment extends AppCompatActivity {

    ViewModel audioVM;
    RecyclerView rec_audio;
    AudioListAdapter listAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.fragment_first);

        doInitialize();
        setUpRecyclerView();
        //getDataFromLocal();
    }

    public void doInitialize(){
        rec_audio = findViewById(R.id.rec_audios);
        //audioVM = ViewModelProviders.of(this).get(ViewModel.class);
    }

    public void setUpRecyclerView(){

        listAdapter = new AudioListAdapter(this);
        rec_audio.setHasFixedSize(true);
        rec_audio.setAdapter(listAdapter);

    }

    public void getDataFromLocal(){
        audioVM.getAllAudios().observe(this, new Observer<List<AudioModel>>() {
            @Override
            public void onChanged(List<AudioModel> audioModels) {
                listAdapter.updateList(audioModels);
            }
        });
    }
}