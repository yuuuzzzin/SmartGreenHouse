package com.example.smartgreenhouse.ui.plantInfo;

import android.graphics.drawable.Drawable;

public class PlantInfoItem {
    private String name = null;
    private String code = null;
    private  Drawable icon;
    private String course = null;
    private String filename = null;

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}