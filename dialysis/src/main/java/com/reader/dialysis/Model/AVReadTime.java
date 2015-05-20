package com.reader.dialysis.Model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.Date;

/**
 * Created by dorothy on 15/5/18.
 */
@AVClassName("ReadTime")
public class AVReadTime extends AVObject {

    private int userID;
    private Date readDate;
    private long readTime;

    public AVReadTime(){
        readTime = 0;
    }

    public int getUserID() {
        return getInt("user_id");
    }

    public Date getReadDate() {
        return getDate("read_date");
    }

    public long getReadTime() {
        return getLong("read_time");
    }

    public void addReadTime(long useTime) {
        long readTime = getReadTime() + useTime;
        put("read_time", readTime);
    }

    public void initialize(int userId, long readTime) {
        put("user_id", userId);
        put("read_time", readTime);
        put("read_date", new Date());
    }
}
