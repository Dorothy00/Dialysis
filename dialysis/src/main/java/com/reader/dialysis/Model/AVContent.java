package com.reader.dialysis.Model;

/**
 * Created by dorothy on 15/5/9.
 */
public class AVContent {
    int chapterId;
    String title;

    public AVContent(){

    }

    public AVContent(int chapterId, String title){
        this.chapterId = chapterId;
        this.title = title;
    }

    public int getChapterId() {
        return chapterId;
    }

    public String getTitle() {
        return title;
    }
}
