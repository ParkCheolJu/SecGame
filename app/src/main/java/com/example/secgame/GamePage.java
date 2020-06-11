package com.example.secgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class GamePage extends AppCompatActivity {

    ExamQuestions[] gamePage;
    Button completeButton;
    Intent game;
    int sec, dbSize;
    int exam[];
    String answer[];
    FragmentManager fragmentManager;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    MyDBHelper myHelper;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framepage);

        //액티비티 통신
        game = getIntent();
        sec = game.getExtras().getInt("sec");
        dbSize = game.getExtras().getInt("DBsize");
        exam = game.getIntArrayExtra("exam");
        items = game.getStringArrayListExtra("items");

        //프레그먼트에서 사용할 변수
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,items);
        myHelper = new MyDBHelper(this);
        answer = new String[5];

        //프레그먼트 생성
        gamePage = new ExamQuestions[5];

        //프레그먼트 초기화
        for(int i = 0; i < 5; i++) {
            gamePage[i] = new ExamQuestions(i + 1, exam[i], sec);
        }

        //프레그먼트 넣기
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[0]).commit();

        completeButton = findViewById(R.id.completeButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                Intent resultPage = new Intent(getApplicationContext(), ResultInfo.class);
                resultPage.putExtra("answer", answer);
                resultPage.putExtra("exam",exam);
                resultPage.putStringArrayListExtra("items",items);
                startActivity(resultPage);
                finish();
            }
        });

    }

    //시간나면 스와이프도 구현해보기
    public void onFragmentChanged(int index){
        switch (index){
            case -1:
                Toast.makeText(this, "이전 문제가 없습니다.", Toast.LENGTH_SHORT).show();break;
            case -2 :
                Toast.makeText(this, "다음 문제가 없습니다.", Toast.LENGTH_SHORT).show();break;
            case 0:fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[0]).commit();break;
            case 1: fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[1]).commit();break;
            case 2: fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[2]).commit();break;
            case 3:fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[3]).commit();break;
            case 4:fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[4]).commit();break;
            default: finish();
        }
    }
}
