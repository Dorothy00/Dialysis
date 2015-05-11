package test.dorothy.graduation.test;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.text.BoringLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/1.
 */
public class PageSpan {
    private List<LineSpan> lineSpanList;
    private PaintInfo paintInfo;
    private int lineH;

    public PageSpan(){
        lineSpanList = new ArrayList<LineSpan>();
    }

    public List<LineSpan> getLineSpanList() {
        return lineSpanList;
    }

    public void setLineSpanList(List<LineSpan> lineSpanList) {
        this.lineSpanList = lineSpanList;
    }

    public PaintInfo getPaintInfo() {
        return paintInfo;
    }

    public void setPaintInfo(PaintInfo paintInfo) {
        this.paintInfo = paintInfo;
    }

    public int getLineH() {
        return lineH;
    }

    public void setLineH(int lineH) {
        this.lineH = lineH;
    }

    public void addLine(LineSpan lineSpan){
        if(lineSpanList!=null){
            lineSpanList.add(lineSpan);
        }
    }

    public boolean isEmpty(){
        return lineSpanList==null || lineSpanList.isEmpty();
    }

    public void draw(Canvas canvas){
        if(lineSpanList==null){
            return;
        }

        Paint.FontMetrics metrics = paintInfo.getFontMetrics();

        float xPos = paintInfo.getPaddingLeft();
        float yPos = paintInfo.getPaddingTop() + lineH - metrics.bottom;
        for (LineSpan lineSpan : lineSpanList){
            lineSpan.draw(canvas,paintInfo.getPaint(),xPos,yPos,paintInfo.getSpaceW());
            yPos = yPos + paintInfo.getSpaceH() + lineH;
        }
    }
}
