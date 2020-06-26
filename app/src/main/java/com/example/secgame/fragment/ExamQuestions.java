package com.example.secgame.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.secgame.R;
import com.example.secgame.activity.GamePage;
import com.example.secgame.database.MyDBHelper;

import java.io.IOException;

public class ExamQuestions extends Fragment {

    GamePage activity;
    Button playButton;
    TextView preTv, postTv, countProgress;
    ProgressBar gameProgress;
    AutoCompleteTextView answerTv;
    int num, testNum, sec;
    MyDBHelper myHelper;
    SQLiteDatabase musicDB;
    MediaPlayer mPlayer;

    public ExamQuestions(int num, int testNum, int sec){
        this.num = num;
        this.testNum = testNum;
        this.sec = sec;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game, container, false);

        playButton = view.findViewById(R.id.playButton);
        preTv = view.findViewById(R.id.pre_button);
        postTv = view.findViewById(R.id.post_button);
        countProgress = view.findViewById(R.id.count_progress);
        gameProgress = view.findViewById(R.id.gameProgress);
        answerTv = view.findViewById(R.id.answerTv);
        activity = (GamePage)getActivity();

        myHelper = activity.myHelper;

        answerTv.setAdapter(activity.adapter);

        countProgress.setText(num+" / 5");
        gameProgress.setProgress(num);

        activity.mPlayer= this.mPlayer = new MediaPlayer();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicDB = myHelper.getReadableDatabase();
                Cursor cursor;
                String mp3 = null;
                String query = "SELECT mAddress FROM musicInfo";
                cursor = musicDB.rawQuery(query,null);
                while(cursor.moveToNext()){
                    if(cursor.getPosition() == testNum){
                        mp3 = cursor.getString(0);
                        break;
                    }
                }
                musicDB.close();
                try {
                    mPlayer.reset();
                    mPlayer.setDataSource(mp3);
                    mPlayer.prepare();
                    mPlayer.start();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mPlayer==null)return;
                            while(mPlayer.isPlaying())
                                if (mPlayer.getCurrentPosition() >= sec*1000)
                                    mPlayer.stop();
                        }
                    });
                    thread.start();
                }catch (IOException e){

                }
                activity.answer[num-1] = "";
            }
        });

        answerTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                activity.answer[num-1] = answerTv.getText().toString();
            }
        });

        preTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num!=1){
                    mPlayer.stop();
                    activity.onFragmentChanged(num-2);
                }
                else{
                    mPlayer.stop();
                    activity.onFragmentChanged(-1);}
            }
        });

        postTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num!=5){
                    mPlayer.stop();
                    activity.onFragmentChanged(num);
                }
                else{
                    mPlayer.stop();
                    activity.onFragmentChanged(-2);
                }
            }
        });
        return view;
    }
}
