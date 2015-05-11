package test.dorothy.graduation.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/1.
 */
public class Book {
    private String title;
    private String author;
    private List<String> paras;

    public List<String> getParas() {
        return paras;
    }

    public void setParas(List<String> paras) {
        List<String> newParas = new ArrayList<String>();
        newParas.addAll(paras);
        this.paras = newParas;
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

}
