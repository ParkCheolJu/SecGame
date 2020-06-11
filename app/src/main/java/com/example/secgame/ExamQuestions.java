package com.example.secgame;

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

    ExamQuestions(int num, int testNum, int sec){
        this.num = num;
        this.testNum = testNum;
        this.sec = sec;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game, container, false);

        playButton = (Button)view.findViewById(R.id.playButton);
        preTv = (TextView)view.findViewById(R.id.pre_button);
        postTv = (TextView)view.findViewById(R.id.post_button);
        countProgress = (TextView)view.findViewById(R.id.count_progress);
        gameProgress = (ProgressBar)view.findViewById(R.id.gameProgress);
        answerTv = (AutoCompleteTextView)view.findViewById(R.id.answerTv);
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
                String query = "SELECT mAddress FROM musicInfo WHERE mIndex = " + testNum + ";";
                cursor = musicDB.rawQuery(query,null);
                cursor.moveToNext();
                String mp3 = cursor.getString(0);
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
