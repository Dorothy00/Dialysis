package com.reader.dialysis.Model;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.reader.dialysis.util.JsonUtil;

/**
 * Created by dorothy on 15/5/16.
 */
@AVClassName("Word")
public class AVWord extends AVObject {

    private Word word;

    public AVWord() {

    }

    public AVWord(int userId, Word word) {
        String jsonStr = JSON.toJSONString(word);
        put("word_obj", jsonStr);
        put("word", word.getWord());
        put("user_id", userId);
    }

    public Word getWordObj() {
        String jsonStr = getString("word_obj");
        return JsonUtil.createModel(jsonStr, Word.class);
    }

    public String getWord() {
        return getString("word");
    }

    public boolean isMaster() {
        return getBoolean("master");
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public void setMaster(boolean isMaster) {
        put("master", isMaster);
    }
}
