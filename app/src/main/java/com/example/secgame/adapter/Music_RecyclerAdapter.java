package com.example.secgame.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secgame.dto.MusicInfo;
import com.example.secgame.R;

import java.util.ArrayList;

public class Music_RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<MusicInfo> musicInfoArrayList;
    //리스너 객체를 저장하는 변수
    private OnItemClickListener mListener = null;

    //외부에서 클릭이벤트를 처리하기위한 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
        void onItemLongClick(View v, int pos);
    }

    public Music_RecyclerAdapter(ArrayList<MusicInfo> musicInfoArrayList){
        this.musicInfoArrayList = musicInfoArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{

        ImageView musicLogo;
        TextView musicName;

        public MusicViewHolder(@NonNull final View itemView) {
            super(itemView);

            musicLogo = itemView.findViewById(R.id.music_logo);
            musicName = itemView.findViewById(R.id.music_name);
            //클릭이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!= null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
            //long클릭 이벤트
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!= null){
                            mListener.onItemLongClick(v,pos);
                        }
                    }
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_music, parent, false);

        return new MusicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MusicViewHolder musicViewHolder = (MusicViewHolder) holder;

        MusicInfo item = musicInfoArrayList.get(position);

        musicViewHolder.musicLogo.setImageResource(musicInfoArrayList.get(position).getDrawableId());
        musicViewHolder.musicName.setText(musicInfoArrayList.get(position).getName());
    }

    public MusicInfo getItem(int position){ return musicInfoArrayList.get(position);}

    @Override
    public int getItemCount() {
        return musicInfoArrayList.size();
    }
}
