package com.example.secgame;

import java.util.Random;

public class CreateExam {

    int exam[];

    CreateExam(){
        exam = new int[5];
    }

    int[] create(){
        Random rnd = new Random();

        for(int i = 0; i < 5 ; i++){
            exam[i] = rnd.nextInt(5);
            for(int k = 0; k < i ; k++){
                if(exam[i]==exam[k])
                    i--;
            }
        }

        return exam;
    }
}
