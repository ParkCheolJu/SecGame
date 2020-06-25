package com.example.secgame;

public class MusicInfo {

    private int drawableId;
    private String customName, address;

    public MusicInfo(int drawableId, String customName, String address){
        this.drawableId = drawableId;
        this.customName = customName;
        this.address = address;
    }

    public int getDrawableId(){ return drawableId; }

    public String getName(){
        return customName;
    }

    public String getAddress(){
        return address;
    }
}
