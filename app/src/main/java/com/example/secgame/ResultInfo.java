package com.example.secgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultInfo extends AppCompatActivity {

    Button checkButton, returnButton;
    Intent resultPage;
    String answer[];
    String exam[];
    String oX[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_score);

        resultPage = getIntent();
        answer = resultPage.getExtras().getStringArray("sec");
        exam = resultPage.getExtras().getStringArray("exam");

        //ox비교해서 넣는것부터 시작

        checkButton = findViewById(R.id.checkButton);
        returnButton = findViewById(R.id.returnButton);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResultPage.class));
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