package com.reader.dialysis.Model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * Created by dorothy on 15/5/8.
 */
@AVClassName("UserBook")
public class AVUserBook extends AVObject{
    private int bookId;
    private String title;
    private String author;
    private AVFile coverUrl;
    private int progress;
    private boolean isFinished;

    public AVUserBook(){

    }

    public AVUserBook(String title, String author, AVFile avFile, int progress, boolean isFinished) {
        put("title",title);
        put("author",author);
        put("cover_url",avFile);
        put("progress",progress);
        put("is_finish",isFinished);
    }

    public int getBookId(){
        return getInt("book_id");
    }
    public String getTitle() {
        return getString("title");
    }

    public String getAuthor() {
        return getString("author");
    }

    public AVFile getCoverUrl() {
        return getAVFile("cover_url");
    }

    public int getProgress() {
        return getInt("progress");
    }

    public boolean isFinished(){
        return getBoolean("is_finish");
    }
}
