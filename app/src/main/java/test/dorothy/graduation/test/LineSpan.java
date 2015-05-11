package test.dorothy.graduation.test;

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
    private int type;

    public LineSpan(){
        lineSpan = new ArrayList<WordSpan>();
        this.type = LINE_NORMAL;
    }

    public LineSpan(List<WordSpan> lineSpan) {
        this.lineSpan = lineSpan;
        this.type = LINE_NORMAL;
    }

    public List<WordSpan> getLineSpan() {
        return lineSpan;
    }

    public void setLineSpan(List<WordSpan> lineSpan) {
        this.lineSpan = lineSpan;
    }

    public void addWordSpan(WordSpan wordSpan){
        if(lineSpan!=null) {
            lineSpan.add(wordSpan);
        }
    }

    public void setLineType(int type){
        this.type = type;
    }

    public void draw(Canvas canvas,Paint paint,float xPos,float yPos,float spaceW){
        if(lineSpan==null){
            return;
        }
        float wordXPos = xPos;
        float wordYPos = yPos;
        for(WordSpan wordSpan : lineSpan){
            wordSpan.draw(canvas,paint,wordXPos,wordYPos);
            wordXPos = wordXPos + spaceW + wordSpan.getWidth();
        }
    }
}
