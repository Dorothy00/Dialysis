package com.reader.dialysis.Model;

import com.avos.avoscloud.AVFile;

/**
 * Created by dorothy on 15/5/25.
 */
public class Book {
    private int bookId;
    private String title;
    private String author;
    private AVFile coverUrl;
    private boolean isAdded;

    public Book() {

    }

    public Book(AVBook avBook) {
        this.bookId = avBook.getBookId();
        this.title = avBook.getTitle();
        this.author = avBook.getAuthor();
        this.coverUrl = avBook.getCoverUrl();
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public AVFile getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(AVFile coverUrl) {
        this.coverUrl = coverUrl;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }
}
