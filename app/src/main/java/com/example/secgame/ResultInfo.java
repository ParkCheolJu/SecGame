package com.example.secgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultInfo extends AppCompatActivity {

    Button checkButton, returnButton;
    TextView resultCount, resultComment;
    Intent resultPage;
    String[] answer;
    int[] exam;
    String[] correctAnswer, oX;
    int correct;
    ArrayList<String> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_score);

        resultPage = getIntent();
        answer = resultPage.getExtras().getStringArray("answer");
        exam = resultPage.getExtras().getIntArray("exam");
        correctAnswer = new String[5];
        items = resultPage.getStringArrayListExtra("items");
        oX = new String[5];

        //하드코딩
        String comment[] = {"야 가서 다시듣고와", "하나? 고작 하나?", "그래도 음악을 조금 듣긴했네", "반타작 네이스~", "이정도면 엄청 잘한거지", "변태"};

        correct = 0;
        //채점점
        for(int i = 0; i < 5; i++){
            correctAnswer[i] = items.get(exam[i]);
            if(correctAnswer[i].equals(answer[i])){
                correct++;
                oX[i] = "O";
            }
            else
                oX[i] = "X";
        }

        checkButton = findViewById(R.id.checkButton);
        returnButton = findViewById(R.id.returnButton);
        resultCount = findViewById(R.id.result_count);
        resultComment = findViewById(R.id.result_comment);

        resultCount.setText("결과 : " + correct + " / 5");
        resultComment.setText(comment[correct]);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultinfo = new Intent(getApplicationContext(), ResultPage.class);
                resultinfo.putExtra("ox", oX);
                resultinfo.putExtra("correctAnswer", correctAnswer);
                resultinfo.putExtra("answer", answer);
                startActivity(resultinfo);
                finish();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}