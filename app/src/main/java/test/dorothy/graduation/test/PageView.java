package test.dorothy.graduation.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dorothy on 15/5/2.
 */
public class PageView extends View {

    private PageSpan pageSpan;
    private PaintInfo paintInfo;

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(pageSpan==null || pageSpan.isEmpty() || paintInfo==null){
            return;
        }
        pageSpan.draw(canvas);
    }

    public PageSpan getPageSpan() {
        return pageSpan;
    }

    public void setPageSpan(PageSpan pageSpan) {
        this.pageSpan = pageSpan;
        invalidate();
    }

    public PaintInfo getPaintInfo() {
        return paintInfo;
    }

    public void setPaintInfo(PaintInfo paintInfo) {
        this.paintInfo = paintInfo;
        invalidate();
    }
}
