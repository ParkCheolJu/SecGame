package com.example.secgame.dto;

public class ResultForm {

    private String num, correct, answer, oorx;


    public ResultForm(String num, String correct, String answer, String oorx) {
        this.num = num;
        this.correct = correct;
        this.answer = answer;
        this.oorx = oorx;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOorx() {
        return oorx;
    }

    public void setOorx(String oorx) {
        this.oorx = oorx;
    }
}
