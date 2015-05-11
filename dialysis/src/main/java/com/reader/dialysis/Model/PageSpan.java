package com.reader.dialysis.Model;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/1.
 */
public class PageSpan {
    private List<LineSpan> lineSpanList;
    private PaintInfo paintInfo;
    private int lineH;

    public PageSpan() {
        lineSpanList = new ArrayList<>();
    }

    public List<LineSpan> getLineSpanList() {
        return lineSpanList;
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

    public void addLine(LineSpan lineSpan) {
        if (lineSpanList != null) {
            lineSpanList.add(lineSpan);
        }
    }

    public boolean isEmpty() {
        return lineSpanList == null || lineSpanList.isEmpty();
    }

    public int getLineSize() {
        return lineSpanList == null ? 0 : lineSpanList.size();
    }

    public void draw(Canvas canvas) {
        if (lineSpanList == null) {
            return;
        }

        Paint.FontMetrics metrics = paintInfo.getFontMetrics();

        float xPos = paintInfo.getPaddingLeft();
        float yPos = paintInfo.getPaddingTop() + lineH - metrics.bottom;
        for (LineSpan lineSpan : lineSpanList) {
            float spaceW = paintInfo.getSpaceW();
            if (lineSpan.getLineType() != LineSpan.LINE_END) {
                float sw = paintInfo.getScreenW() - paintInfo.getPaddingLeft() - paintInfo
                        .getPaddingRight();
                spaceW = (sw - lineSpan.getWordTotalWidth()) / (lineSpan.getWordSize() - 1);
            }
            lineSpan.setSpaceW(spaceW);
            lineSpan.draw(canvas, paintInfo.getTextPaint(), paintInfo.getBackgroundPaint(),
                    lineH, xPos, yPos);
            yPos = yPos + paintInfo.getSpaceH() + lineH;
        }
    }
}
