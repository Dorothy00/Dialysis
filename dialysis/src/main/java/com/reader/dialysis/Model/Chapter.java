package com.reader.dialysis.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/9.
 */
public class Chapter {

    private int bookId;
    private int chapterId;
    private String title;
    private List<String> paras;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    public List<String> getParas() {
        return paras;
    }

    public void setParas(List<String> paras) {
        List<String> newParas = new ArrayList<String>();
        newParas.addAll(paras);
        this.paras = newParas;
    }
}
