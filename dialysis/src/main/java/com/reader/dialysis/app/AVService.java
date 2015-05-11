package com.reader.dialysis.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.reader.dialysis.Model.AVChapter;
import com.reader.dialysis.Model.AVContent;
import com.reader.dialysis.Model.AVTableContents;
import com.reader.dialysis.Model.AVUserBook;

import org.apache.commons.io.IOUtils;

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
            InputStream is = assetManager.open("The_Adventures_of_Sherlock_Holmes.jpg");
            byte[] bytes = IOUtils.toByteArray(is);
            final AVFile avFile = new AVFile("cover_url", bytes);
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Toast.makeText(context, "11success", Toast.LENGTH_LONG)
                                .show();
                        AVUserBook AVUserBook = new AVUserBook("The Adventures of Sherlock Holmes",
                                "Arthur Conan Doyle", avFile, 0, false);
                        AVUserBook.saveInBackground(new SaveCallback() {
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
        List<AVContent> AVContents = new ArrayList<AVContent>();

        AVContents.add(new AVContent(1,"PETER BREAKS THROUGH"));
        AVContents.add(new AVContent(2,"THE SHADOW"));
        AVContents.add(new AVContent(3,"COME AWAY, COME AWAY!"));
        AVContents.add(new AVContent(4,"THE FLIGHT"));
        AVContents.add(new AVContent(5,"THE ISLAND COME TRUE"));
        AVContents.add(new AVContent(6,"THE LITTLE HOUSE"));
        AVContents.add(new AVContent(7,"THE HOME UNDER THE GROUND"));
        AVContents.add(new AVContent(8,"THE MERMAIDS' LAGOON"));
        AVContents.add(new AVContent(9,"THE NEVER BIRD"));
        AVContents.add(new AVContent(10,"THE HAPPY HOME"));
        AVContents.add(new AVContent(11,"WENDY'S STORY"));
        AVContents.add(new AVContent(12,"THE CHILDREN ARE CARRIED OFF"));
        AVContents.add(new AVContent(13,"DO YOU BELIEVE IN FAIRIES?"));
        AVContents.add(new AVContent(14,"THE PIRATE SHIP"));
        AVContents.add(new AVContent(15,"\"HOOK OR ME THIS TIME\""));
        AVContents.add(new AVContent(16,"THE RETURN HOME"));
        AVContents.add(new AVContent(17,"WHEN WENDY GREW UP"));

        AVTableContents AVTableContents = new AVTableContents(2, AVContents);
        AVTableContents.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    Toast.makeText(context, "22success", Toast
                            .LENGTH_LONG)
                            .show();
                }else{
                    Toast.makeText(context, "failure" + e.getMessage(), Toast
                            .LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    public static void uploadChapter(final Context context){
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open("peter-pan-chapt");
            byte[] bytes = IOUtils.toByteArray(is);
            final AVFile avFile = new AVFile("chapter_content_xml",bytes);
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        AVChapter chapter= new AVChapter(2,2,"THE SHADOW",avFile);
                        chapter.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e==null){
                                    Toast.makeText(context, "33success", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }else{
                                    Toast.makeText(context, "failure00" + e
                                            .getMessage(), Toast
                                            .LENGTH_LONG).show();
                                }
                            }
                        });
                    }else{
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
