package com.reader.dialysis.util;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.reader.dialysis.Model.Chapter;
import com.reader.dialysis.Model.LineSpan;
import com.reader.dialysis.Model.PageSpan;
import com.reader.dialysis.Model.PaintInfo;
import com.reader.dialysis.Model.WordSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/9.
 */
public class DialysisSpanGenerator {
    private List<WordSpan> setupWord(String para) {

        List<WordSpan> wordSpanList = new ArrayList<>();
        String[] words = para.split(" ");
        for (String word : words) {
            if (!TextUtils.isEmpty(word) && !TextUtils.equals(word, "\n")) {
                // TODO trim "\n"
                WordSpan wordSpan = new WordSpan(word);
                wordSpanList.add(wordSpan);
            }
        }

        return wordSpanList;
    }

    private List<LineSpan> setupLine(List<String> paras, PaintInfo paintInfo) {
        Paint paint = paintInfo.getTextPaint();
        // 绘制长度
        int cw = paintInfo.getScreenW() - paintInfo.getPaddingLeft() - paintInfo.getPaddingRight();
        int spaceW = paintInfo.getSpaceW();

        List<LineSpan> lineSpanList = new ArrayList<>();
        for (String para : paras) {
            List<WordSpan> wordSpanList = setupWord(para);

            int curLineW = 0;
            float totalWidth = 0;
            LineSpan lineSpan = new LineSpan();
            lineSpan.setLineType(LineSpan.LINE_START);
            for (WordSpan wordSpan : wordSpanList) {
                String word = wordSpan.getWord();
                float width = paint.measureText(word);
                if (curLineW + width >= cw) {
                    lineSpan.setWordTotalWidth(totalWidth);
                    lineSpanList.add(lineSpan);
                    lineSpan = new LineSpan();
                    curLineW = 0;
                    totalWidth = 0;
                }
                curLineW += width;
                curLineW += spaceW;
                totalWidth += width;
                wordSpan.setWidth(width);
                lineSpan.addWordSpan(wordSpan);
            }
            lineSpanList.add(lineSpan);
            lineSpanList.get(lineSpanList.size() - 1).setLineType(LineSpan.LINE_END);
        }

        return lineSpanList;
    }

    public List<PageSpan> setupPage(Chapter chapter, PaintInfo paintInfo) {
        List<PageSpan> pageSpanList = new ArrayList<>();
        List<LineSpan> lineSpanList = setupLine(chapter.getParas(), paintInfo);
        int lineH = getLineHeight(paintInfo);
        int lpPage = getLinesPerPage(paintInfo);
        int curLineNum = 0;

        PageSpan pageSpan = new PageSpan();
        pageSpan.setPaintInfo(paintInfo);
        pageSpan.setLineH(lineH);

        for (LineSpan lineSpan : lineSpanList) {
            if (curLineNum >= lpPage) {
                pageSpanList.add(pageSpan);
                pageSpan = new PageSpan();
                pageSpan.setPaintInfo(paintInfo);
                pageSpan.setLineH(lineH);
                curLineNum = 0;
            }
            curLineNum++;
            pageSpan.addLine(lineSpan);

        }
        pageSpanList.add(pageSpan);

        return pageSpanList;
    }

    private int getLinesPerPage(PaintInfo paintInfo) {
        int sH = paintInfo.getScreenH() - paintInfo.getPaddingTop() - paintInfo.getPaddingBottom();
        int spaceH = paintInfo.getSpaceH();
        int lineH = getLineHeight(paintInfo);

        return (sH + spaceH) / (lineH + spaceH);
    }

    private int getLineHeight(PaintInfo paintInfo) {
        Paint paint = paintInfo.getTextPaint();
        Rect bound = new Rect();
        paint.getTextBounds("hHGgfFjJ", 0, 7, bound);

        return bound.height();
    }
}
