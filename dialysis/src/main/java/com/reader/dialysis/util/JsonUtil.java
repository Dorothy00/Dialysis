package com.reader.dialysis.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dorothy on 15/5/13.
 */
public class JsonUtil {

    public static <T> List<T> createList(String str, Class<T> tClass) throws JsonParseException{
        List<T> list = new ArrayList<>();
        JsonElement jsonElement = new JsonParser().parse(str);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Iterator<JsonElement> iterator = jsonArray.iterator();
        Gson gson = new Gson();
        while (iterator.hasNext()) {
            T t = gson.fromJson(iterator.next(), tClass);
            list.add(t);
        }
        return list;
    }

    public static String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T createModel(String jsonStr,Class<T> tClass){
        Gson gson = new Gson();
        return gson.fromJson(jsonStr,tClass);
    }
}
