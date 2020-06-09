package com.example.secgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ExamQuestions extends Fragment {


    GamePage activity;
    Button replayButton;
    TextView preTv, postTv, countProgress;
    ProgressBar gameProgress;
    AutoCompleteTextView answerTv;
    int num;

    ExamQuestions(int num){
        this.num = num;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game, container, false);

        replayButton = (Button)view.findViewById(R.id.replayButton);
        preTv = (TextView)view.findViewById(R.id.pre_button);
        postTv = (TextView)view.findViewById(R.id.post_button);
        countProgress = (TextView)view.findViewById(R.id.count_progress);
        gameProgress = (ProgressBar)view.findViewById(R.id.gameProgress);
        answerTv = (AutoCompleteTextView)view.findViewById(R.id.answerTv);
        activity = (GamePage)getActivity();

        countProgress.setText(num+" / 5");
        gameProgress.setProgress(num);

        preTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num!=1)
                    activity.onFragmentChanged(num-2);
                else
                    activity.onFragmentChanged(-1);
            }
        });

        postTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num!=5)
                    activity.onFragmentChanged(num);
                else
                    activity.onFragmentChanged(-2);
            }
        });

        return view;
    }
}
