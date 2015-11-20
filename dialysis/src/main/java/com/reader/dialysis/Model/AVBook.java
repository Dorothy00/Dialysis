package com.reader.dialysis.Model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * Created by dorothy on 15/5/25.
 */
@AVClassName("Book")
public class AVBook extends AVObject {
    private int bookId;
    private String title;
    private String author;
    private AVFile coverUrl;

    public AVBook() {

    }

    public AVBook(String title, String author, AVFile avFile,int bookId) {
        put("title", title);
        put("author", author);
        put("cover_url", avFile);
        put("book_id",bookId);
    }

    public int getBookId() {
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
}
