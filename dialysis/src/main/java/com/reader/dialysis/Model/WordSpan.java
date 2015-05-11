package com.reader.dialysis.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by dorothy on 15/5/1.
 */
public class WordSpan {
    private String word;
    private float width;
    private boolean isSelected;

    public WordSpan(){

    }

    public WordSpan(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    //TODO REFACTOR
    public void draw(Canvas canvas, Paint textPaint ,Paint bgPaint,int lineH,float xPos,float yPos){
        if(isSelected){
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float top = yPos + metrics.bottom -lineH;
            canvas.drawRect(xPos,top,xPos+width,top+lineH,bgPaint);
        }
        canvas.drawText(word,xPos,yPos,textPaint);
    }
}
