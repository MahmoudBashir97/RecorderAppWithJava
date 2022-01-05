package com.mahmoudbashir.recorderapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahmoudbashir.recorderapp.R;
import com.mahmoudbashir.recorderapp.model.AudioModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {

    Context context;

    public AudioListAdapter(Context context) {
        this.context = context;
    }

    private List<AudioModel> mlist;
    public void updateList(List<AudioModel> modelList){
        this.mlist = modelList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AudioListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_audio,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AudioListAdapter.ViewHolder holder, int position) {
        AudioModel item = mlist.get(position);

        holder.txt_title.setText(item.getAudioName()+"");

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
        }
    }
}
