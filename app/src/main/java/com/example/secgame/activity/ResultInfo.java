package com.example.secgame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.secgame.R;

import java.util.ArrayList;

public class ResultInfo extends Activity {

    Button checkButton, returnButton;
    TextView resultCount, resultComment;
    Intent resultPage;
    String[] answer;
    int[] exam;
    String[] correctAnswer, oX;
    int correct;
    ArrayList<String> items;
    ImageView resultImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_score);
        setTitle("결과");

        resultPage = getIntent();
        answer = resultPage.getExtras().getStringArray("answer");
        exam = resultPage.getExtras().getIntArray("exam");
        correctAnswer = new String[5];
        items = resultPage.getStringArrayListExtra("items");
        oX = new String[5];

        //하드코딩
        String comment[] = {"0점인데 솔루션을 어떻게줘", "하나? 고작 하나?", "그래도 음악을 조금 듣긴했네유", "한 주만 더 노력해봅시다", "이정도면 엄청 잘한거지", "이집 맛있네"};
        int image[] = {R.drawable.score0, R.drawable.score1, R.drawable.score2, R.drawable.score3, R.drawable.score4, R.drawable.score5 };

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
        resultImage = findViewById(R.id.result_image);

        resultImage.setImageResource(image[correct]);

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