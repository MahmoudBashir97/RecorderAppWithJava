package com.mahmoudbashir.recorderapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mahmoudbashir.recorderapp.R;
import com.mahmoudbashir.recorderapp.model.AudioModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {

    Context context;
    private List<AudioModel> mlist;
    OnClickItemInterface onClickItemInterface;


    public AudioListAdapter(Context context,List<AudioModel> audioList,OnClickItemInterface onClickItemInterface) {
        this.context = context;
        this.mlist = audioList;
        this.onClickItemInterface = onClickItemInterface;
    }


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
        holder.txt_date.setText(item.getAudioDate());
        Log.d("audiosLog : ","id : "+item.getId()+" name :"+item.getAudioName()+" path:"+item.getPathName());
        holder.itemView.setOnClickListener(v -> {
            onClickItemInterface.onClick(item,position);
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_title,txt_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_date = itemView.findViewById(R.id.txt_date);
        }
    }

    public interface OnClickItemInterface{
        void onClick(AudioModel audioModel , int position);
    }
}
