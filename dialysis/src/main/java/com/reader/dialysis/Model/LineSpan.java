package com.reader.dialysis.Model;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/1.
 */
public class LineSpan {
    public static final int LINE_START = 0;
    public static final int LINE_END = 1;
    public static final int LINE_NORMAL = 2;

    private List<WordSpan> lineSpan;
    private float wordTotalWidth;
    private float spaceW;
    private int type;

    public LineSpan(){
        lineSpan = new ArrayList<>();
        this.type = LINE_NORMAL;
    }

    public LineSpan(List<WordSpan> lineSpan) {
        this.lineSpan = lineSpan;
        this.type = LINE_NORMAL;
    }

    public List<WordSpan> getLineSpan() {
        return lineSpan;
    }

    public float getWordTotalWidth() {
        return wordTotalWidth;
    }

    public void setWordTotalWidth(float wordTotalWidth) {
        this.wordTotalWidth = wordTotalWidth;
    }

    public void addWordSpan(WordSpan wordSpan){
        if(lineSpan!=null) {
            lineSpan.add(wordSpan);
        }
    }

    public void setLineType(int type){
        this.type = type;
    }

    public int getLineType() {
        return type;
    }

    public int getWordSize(){
        return lineSpan.size();
    }

    public void setSpaceW(float spaceW) {
        this.spaceW = spaceW;
    }

    public float getSpaceW() {
        return spaceW;
    }

    //TODO REFACTOR
    public void draw(Canvas canvas,Paint textPaint,Paint bgPaint,int lineH,float xPos,float yPos){
        if(lineSpan==null){
            return;
        }
        float wordXPos = xPos;
        float wordYPos = yPos;
        for(WordSpan wordSpan : lineSpan){
            wordSpan.draw(canvas,textPaint,bgPaint,lineH,wordXPos,wordYPos);
            wordXPos = wordXPos + spaceW + wordSpan.getWidth();
        }
    }
}
