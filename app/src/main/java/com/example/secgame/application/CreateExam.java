package com.example.secgame.application;

import java.util.Random;

public class CreateExam {

    private int exam[];

    public CreateExam(){
        exam = new int[5];
    }

    public int[] create(int dbsize){
        Random rnd = new Random();

        for(int i = 0; i < 5 ; i++){
            exam[i] = rnd.nextInt(dbsize+1);
            for(int k = 0; k < i ; k++){
                if(exam[i]==exam[k])
                    i--;
            }
        }

        return exam;
    }
}
