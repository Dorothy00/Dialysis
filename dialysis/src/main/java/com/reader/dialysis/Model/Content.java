package com.reader.dialysis.Model;

/**
 * Created by dorothy on 15/5/9.
 */
public class Content {
    private int chapterId;
    private String title;

    public Content(){

    }

    public Content(int chapterId, String title){
        this.chapterId = chapterId;
        this.title = title;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
