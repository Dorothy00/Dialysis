package test.dorothy.graduation.test;

import android.graphics.Paint;

/**
 * Created by dorothy on 15/5/2.
 */
public class PaintInfo {
    private Paint paint;
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

    public PaintInfo(Paint paint, int screenW,int screenH, int spaceW,int spaceH,int paddingTop,int paddingBottom,int paddingLeft,int paddingRight) {
        this.paint = paint;
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

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
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



    public Paint.FontMetrics getFontMetrics(){
        return paint.getFontMetrics();
    }

}
