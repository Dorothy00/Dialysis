package com.reader.dialysis.Model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by dorothy on 15/5/9.
 */
@AVClassName("TableContents")
public class AVTableContents extends AVObject {
    private int bookId;
    private String contents;

    public AVTableContents() {

    }

    public AVTableContents(int bookId, String contents) {
        put("book_id", bookId);
        put("contents", contents);
    }

    public int getBookId() {
        return getInt("book_id");
    }

    public String getAVContentList() {
        return getString("contents");
    }
}
