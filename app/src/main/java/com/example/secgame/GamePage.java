package com.example.secgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class GamePage extends AppCompatActivity {

    ExamQuestions[] gamePage;
    Button completeButton;
    Intent intent;
    int sec;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framepage);

        //프레그먼트 생성
        gamePage = new ExamQuestions[5];

        for(int i = 0; i < 5; i++)
            gamePage[i] = new ExamQuestions(i+1);

        //프레그먼트 넣기
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[0]).commit();

        intent = getIntent();
        sec = intent.getExtras().getInt("sec");

        completeButton = findViewById(R.id.completeButton);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResultInfo.class));
                finish();
            }
        });
    }

    //시간나면 스와이프도 구현해보기
    public void onFragmentChanged(int index){
        switch (index){
            case -1:
                Toast.makeText(this, "이전 문제가 없습니다.", Toast.LENGTH_SHORT).show();
            case 0: fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[0]).commit();break;
            case 1: fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[1]).commit();break;
            case 2: fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[2]).commit();break;
            case 3: fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[3]).commit();break;
            case 4: fragmentManager.beginTransaction().replace(R.id.gamePage, gamePage[4]).commit();break;
            default:
                Toast.makeText(this, "다음 문제가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
