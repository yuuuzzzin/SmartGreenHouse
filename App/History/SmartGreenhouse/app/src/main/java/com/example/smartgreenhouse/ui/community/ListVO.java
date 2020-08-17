package com.example.smartgreenhouse.ui.community;

public class ListVO {
    private String Title;
    private String writer;
    private String date;
    private String hit;

    public String getTitle()
    {
        return Title;
    }

    public String getWriter()
    {
        return writer;
    }

    public String getDate()
    {
        return date;
    }

    public String getHit()
    {
        return hit;
    }

    public void setTitle(String title)
    {
        Title = title;
    }

    public void setWriter(String writer)
    {
        this.writer = writer;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setHit(String hit)
    {
        this.hit = hit;
    }
}
