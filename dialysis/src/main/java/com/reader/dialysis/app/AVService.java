package com.reader.dialysis.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.reader.dialysis.Model.AVBook;
import com.reader.dialysis.Model.AVChapter;
import com.reader.dialysis.Model.AVTableContents;
import com.reader.dialysis.Model.AVUserBook;
import com.reader.dialysis.Model.Content;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/8.
 */
public class AVService {
    public static void fetchUserBooks() {
        AVQuery<AVUserBook> query = new AVQuery<AVUserBook>("AVUserBook");
        query.whereEqualTo("title", "ALICE'S ADVENTURES IN WONDERLAND");
        query.findInBackground(new FindCallback<AVUserBook>() {
            @Override
            public void done(List<AVUserBook> list, AVException e) {
                for (AVUserBook AVUserBook : list) {
                    try {
                        byte[] bytes = AVUserBook.getCoverUrl().getData();
                    } catch (AVException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        });
    }

    public static void uploadData(final Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open("Alice_in_Wonderland.jpg");
            byte[] bytes = IOUtils.toByteArray(is);
            final AVFile avFile = new AVFile("cover_url", bytes);
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Toast.makeText(context, "11success", Toast.LENGTH_LONG)
                                .show();
                        AVBook book = new AVBook("Alice in Wonderland",
                                "Lewis Carroll", avFile, 3);
                        book.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Toast.makeText(context, "success", Toast
                                            .LENGTH_LONG)
                                            .show();
                                } else {
                                    Toast.makeText(context, "failure" + e
                                            .getMessage(), Toast
                                            .LENGTH_LONG).show();
                                }
                            }
                        });


                    } else {
                        Toast.makeText(context, "11failure" + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });


        } catch (Exception e) {
            Toast.makeText(context, "ioecp" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }

    public static void uploadContents(final Context context) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(new Content());
        jsonArray.put(JSON.toJSONString(new Content(1, "A Scandal in Bohemia")));
        jsonArray.put(JSON.toJSONString(new Content(2, "The Red-Headed League")));
        jsonArray.put(JSON.toJSONString(new Content(3, "A Case of Identity")));
        jsonArray.put(JSON.toJSONString(new Content(4, "The Boscombe Valley Mystery")));
        jsonArray.put(JSON.toJSONString(new Content(5, "The Five Orange Pips")));
        jsonArray.put(JSON.toJSONString(new Content(6, "The Man with the Twisted Lip")));
        jsonArray.put(JSON.toJSONString(new Content(7, "The Adventure of the Blue Carbuncle")));
        jsonArray.put(JSON.toJSONString(new Content(8, "The Adventure of the Speckled Band")));
        jsonArray.put(JSON.toJSONString(new Content(9, "The Adventure of the Engineer’s Thumb")));
        jsonArray.put(JSON.toJSONString(new Content(10, "The Adventure of the Noble Bachelor")));
        jsonArray.put(JSON.toJSONString(new Content(11, "The Adventure of the Beryl Coronet")));
        jsonArray.put(JSON.toJSONString(new Content(12, "The Adventure of the Copper Beeches")));

        List<Content> contents = new ArrayList<>();
        contents.add(new Content(1,"PETER BREAKS THROUGH"));
        contents.add(new Content(2,"THE SHADOW"));
        contents.add(new Content(3,"Chapter 3 COME AWAY, COME AWAY!"));
        contents.add(new Content(4,"THE FLIGHT"));
        contents.add(new Content(5,"THE ISLAND COME TRUE"));
        contents.add(new Content(6,"Chapter 6 THE LITTLE HOUSE"));
        contents.add(new Content(7,"THE HOME UNDER THE GROUND"));
        contents.add(new Content(8,"THE MERMAIDS' LAGOON"));
        contents.add(new Content(9,"Chapter 9 THE NEVER BIRD"));
        contents.add(new Content(10,"THE HAPPY HOME"));
        contents.add(new Content(11,"Chapter 11 WENDY'S STORY"));
        contents.add(new Content(12,"THE CHILDREN ARE CARRIED OFF"));
        contents.add(new Content(13,"DO YOU BELIEVE IN FAIRIES?"));
        contents.add(new Content(14,"THE PIRATE SHIP"));
        contents.add(new Content(15,"\"HOOK OR ME THIS TIME\""));
        contents.add(new Content(16,"THE RETURN HOME"));
        contents.add(new Content(17,"WHEN WENDY GREW UP"));
        Gson gson = new Gson();


        AVTableContents AVTableContents = new AVTableContents(1, gson.toJson(contents));
        AVTableContents.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(context, "22success", Toast
                            .LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(context, "failure" + e.getMessage(), Toast
                            .LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public static void uploadChapter(final Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open("peter-pan-chapt");
            byte[] bytes = IOUtils.toByteArray(is);
            final AVFile avFile = new AVFile("chapter_content_xml", bytes);
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        AVChapter chapter = new AVChapter(2, 2, "THE SHADOW", avFile);
                        chapter.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Toast.makeText(context, "33success", Toast
                                            .LENGTH_LONG)
                                            .show();
                                } else {
                                    Toast.makeText(context, "failure00" + e
                                            .getMessage(), Toast
                                            .LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(context, "failure55" + e
                                .getMessage(), Toast
                                .LENGTH_LONG).show();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //
    }
}
