package com.reader.dialysis.Model;

/**
 * Created by dorothy on 15/5/12.
 */
public class WordBounds {
    private WordSpan wordSpan;
    private float left;
    private float right;
    private float top;
    private float bottom;

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public WordSpan getWordSpan() {
        return wordSpan;
    }

    public void setWordSpan(WordSpan wordSpan) {
        this.wordSpan = wordSpan;
    }
}
