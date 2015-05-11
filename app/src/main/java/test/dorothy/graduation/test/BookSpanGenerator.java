package test.dorothy.graduation.test;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/2.
 */
public class BookSpanGenerator {

    private List<WordSpan> setupWord(String para){

        List<WordSpan> wordSpanList = new ArrayList<WordSpan>();
        String[] words = para.split(" ");
        for (String word : words) {
            if (word != null && word.length() >= 0) {
                WordSpan wordSpan = new WordSpan(word);
                wordSpanList.add(wordSpan);
            }
        }

        return wordSpanList;
    }

    private List<LineSpan> setupLine(List<String> paras,PaintInfo paintInfo){
        Paint paint = paintInfo.getPaint();
        int cw = paintInfo.getScreenW() - paintInfo.getPaddingLeft() - paintInfo.getPaddingRight();
        int spaceW = paintInfo.getSpaceW();

        List<LineSpan> lineSpanList = new ArrayList<LineSpan>();
        for(String para:paras){
            List<WordSpan> wordSpanList = setupWord(para);

            int curLineW = 0;
            LineSpan lineSpan = new LineSpan();
            lineSpan.setLineType(LineSpan.LINE_START);
            for (WordSpan wordSpan : wordSpanList) {
                String word = wordSpan.getWord();
                float width = paint.measureText(word);
                if (curLineW >= cw) {
                    lineSpanList.add(lineSpan);
                    lineSpan = new LineSpan();
                    curLineW = 0;
                } else {
                    curLineW += width;
                    curLineW += spaceW;
                    wordSpan.setWidth(width);
                    lineSpan.addWordSpan(wordSpan);
                }
            }
            lineSpanList.get(lineSpanList.size()-1).setLineType(LineSpan.LINE_END);
        }

        return lineSpanList;
    }

    public List<PageSpan> setupPage(Book book,PaintInfo paintInfo) {
        List<PageSpan> pageSpanList = new ArrayList<PageSpan>();
        List<LineSpan> lineSpanList = setupLine(book.getParas(),paintInfo);
        int lineH = getLineHeight(paintInfo);
        int lpPage = getLinesPerPage(paintInfo);
        int curLineNum = 0;

        PageSpan pageSpan = new PageSpan();
        pageSpan.setPaintInfo(paintInfo);
        pageSpan.setLineH(lineH);

        for (LineSpan lineSpan:lineSpanList) {
           if(curLineNum>lpPage){
               pageSpanList.add(pageSpan);
               pageSpan = new PageSpan();
               curLineNum = 0;
           }else{
               curLineNum ++;
               pageSpan.addLine(lineSpan);
           }
        }

        return pageSpanList;
    }

    private int getLinesPerPage(PaintInfo paintInfo){
        int sH = paintInfo.getScreenH() - paintInfo.getPaddingTop() - paintInfo.getPaddingBottom();
        int spaceH = paintInfo.getSpaceH();
        int lineH = getLineHeight(paintInfo);
        int lines = (sH + spaceH)/(lineH + spaceH);

        return lines;
    }

    private int getLineHeight(PaintInfo paintInfo){
        Paint paint = paintInfo.getPaint();
        Rect bound = new Rect();
        paint.getTextBounds("hHGgfFjJ",0,7,bound);
        int lineH = bound.height();

        return lineH;
    }
}
