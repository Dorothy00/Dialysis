package com.reader.dialysis.Model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by dorothy on 15/5/9.
 */
@AVClassName("TableContents")
public class AVTableContents extends AVObject {
    private int bookId;
    private List<AVContent> AVContentList;

    public AVTableContents(){

    }

    public AVTableContents(int bookId, List<AVContent> AVContents){
        put("book_id",bookId);
        put("contens", AVContents);
    }

    public int getBookId() {
        return getInt("book_id");
    }

    public List<AVContent> getAVContentList() {
        return getList("content_list");
    }
}
