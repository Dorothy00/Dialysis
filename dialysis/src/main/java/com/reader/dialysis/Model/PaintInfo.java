package com.reader.dialysis.Model;

import android.graphics.Paint;

/**
 * Created by dorothy on 15/5/2.
 */
public class PaintInfo {
    private Paint textPaint;
    private Paint backgroundPaint;
    private float textSize;
    private int foregroundColor;
    private int backgroundColor;
    private int screenW;
    private int screenH;
    private int spaceW;
    private int spaceH;
    private int paddingTop;
    private int paddingBottom;
    private int paddingRight;
    private int paddingLeft;

    public PaintInfo(){

    }

    public PaintInfo(Paint textPaint, int screenW,int screenH, int spaceW,int spaceH,int paddingTop,int paddingBottom,int paddingLeft,int paddingRight) {
        this.textPaint = textPaint;
        this.screenW = screenW;
        this.screenH = screenH;
        this.spaceW = spaceW;
        this.spaceH = spaceH;
    }

    public int getSpaceW() {
        return spaceW;
    }

    public void setSpaceW(int spaceW) {
        this.spaceW = spaceW;
    }

    public Paint getTextPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        paint.setColor(foregroundColor);
        return paint;
    }

    public Paint getBackgroundPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        return paint;
    }

    public int getScreenW() {
        return screenW;
    }

    public void setScreenW(int screenW) {
        this.screenW = screenW;
    }

    public int getSpaceH() {
        return spaceH;
    }

    public void setSpaceH(int spaceH) {
        this.spaceH = spaceH;
    }

    public int getScreenH() {
        return screenH;
    }

    public void setScreenH(int screenH) {
        this.screenH = screenH;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Paint.FontMetrics getFontMetrics(){
        return getTextPaint().getFontMetrics();
    }

}
