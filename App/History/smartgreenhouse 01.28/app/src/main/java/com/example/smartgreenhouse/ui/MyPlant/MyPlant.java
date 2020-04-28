package com.example.smartgreenhouse.ui.MyPlant;

public class MyPlant {
    String text;
    int img;
    int location;

    public MyPlant(String text, int image, int location) {
        this.text = text;
        this.img = image;
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return img;
    }

    public void setImage(int img) {
        this.img = img;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
