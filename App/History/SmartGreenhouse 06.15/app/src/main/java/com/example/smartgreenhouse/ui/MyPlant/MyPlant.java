package com.example.smartgreenhouse.ui.MyPlant;

import android.graphics.Bitmap;

public class MyPlant {
    String plantNickname;
    Bitmap img;
    String plantPosition;


    public String getPlantNickname() {
        return plantNickname;
    }

    public void setPlantNickname(String plantNickname) {
        this.plantNickname = plantNickname;
    }

    public Bitmap getImage() {
        return img;
    }

    public void setImage(Bitmap img) {
        this.img = img;
    }

    public String getPlantPosition() {
        return plantPosition;
    }

    public void setPlantPosition(String plantPosition) {
        this.plantPosition = plantPosition;
    }
}
