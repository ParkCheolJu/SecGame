package com.example.secgame;

public class MusicInfo {

    public int drawableId;
    public String customName, address;

    public MusicInfo(int drawableId, String customName, String address){
        this.drawableId = drawableId;
        this.customName = customName;
        this.address = address;
    }

    public String getName(){
        return customName;
    }

    public String getAddress(){
        return address;
    }
}
