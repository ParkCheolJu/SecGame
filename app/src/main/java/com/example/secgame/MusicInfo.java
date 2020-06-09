package com.example.secgame;

public class MusicInfo {

    public int drawableId;
    public String name;

    public MusicInfo(int drawableId, String name){
        this.drawableId = drawableId;
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
