package test.dorothy.graduation.test;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by dorothy on 15/5/1.
 */
public class WordSpan {
    private String word;
    private float width;

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

    public void draw(Canvas canvas, Paint paint ,float xPos,float yPos){
        canvas.drawText(word,xPos,yPos,paint);
    }
}
