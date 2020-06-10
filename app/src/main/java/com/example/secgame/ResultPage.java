package com.example.secgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ResultPage extends Activity {

    Button returnButton;
    Intent resultPage;
    String[] ox, correctAnswer, answer;
    RecyclerView result;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_info);

        resultPage = getIntent();
        ox = resultPage.getStringArrayExtra("ox");
        correctAnswer = resultPage.getStringArrayExtra("correctAnswer");
        answer = resultPage.getStringArrayExtra("answer");

        result = findViewById(R.id.recyvle_result);
        result.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        result.setLayoutManager(layoutManager);

        adapter = new ResultRecyclerAdapter(correctAnswer, answer, ox);
        result.setAdapter(adapter);

        returnButton = findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}