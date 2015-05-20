package com.reader.dialysis.Model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * Created by dorothy on 15/5/9.
 */
@AVClassName("AVChapter")
public class AVChapter extends AVObject {
    private int bookId;
    private int chapterId;
    private int wordNum;
    private String title;
    private AVFile contentXml;

    public AVChapter() {

    }

    public AVChapter(int bookId, int chapterId, String title, AVFile
            contentXml) {
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.title = title;

        put("book_id", bookId);
        put("chapter_id", chapterId);
        put("title", title);
        put("content_xml", contentXml);

    }

    public int getBookId() {
        return getInt("book_id");
    }

    public int getChapterId() {
        return getInt("chapter_id");
    }

    public String getTitle() {
        return getString("title");
    }

    public int getWordNum() {
        return getInt("word_num");
    }

    public AVFile getContentXml() {
        return getAVFile("content_xml");
    }
}
