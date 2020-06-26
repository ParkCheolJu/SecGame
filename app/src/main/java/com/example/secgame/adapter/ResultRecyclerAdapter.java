package com.example.secgame.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secgame.R;

public class ResultRecyclerAdapter extends RecyclerView.Adapter<ResultRecyclerAdapter.MyViewHolder> {


    private String[] correct, userAnswer, oorx;

    public ResultRecyclerAdapter(String[] correct, String[] userAnswer, String[] oorx) {
        this.correct = correct;
        this.userAnswer = userAnswer;
        this.oorx = oorx;
    }

    //리사이클러뷰에 들어갈 뷰 홀더, 뷰 홀더에 들어갈 아이템 지정
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv1,tv2,tv3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv1 = itemView.findViewById(R.id.correct);
            this.tv2 = itemView.findViewById(R.id.userAnswer);
            this.tv3 = itemView.findViewById(R.id.oorx);
        }
    }

    @NonNull
    @Override
    //리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야 하며, extends의 Adapter<>에서 <>안에 들어가는 타입을 따른다고함
    public ResultRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_result, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(holderView);
        return myViewHolder;
    }

    @Override
    //실제 각 뷰 홀더에 데이터를 연결해주는 함수
    public void onBindViewHolder(@NonNull ResultRecyclerAdapter.MyViewHolder holder, int position) {
        holder.tv1.setText(this.correct[position]);
        holder.tv2.setText(this.userAnswer[position]);
        holder.tv3.setText(this.oorx[position]);
    }

    @Override
    public int getItemCount() {
        return correct.length;
    }
}
