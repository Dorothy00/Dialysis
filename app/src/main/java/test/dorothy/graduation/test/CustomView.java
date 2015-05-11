package test.dorothy.graduation.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dorothy on 15/5/1.
 */
public class CustomView extends View {

    private Paint mPaint;
    private Rect mRect;
    private Context mContext;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(40);
        mContext = context;

    }

    @Override
    protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);
//        mPaint.setColor(Color.RED);
//        int baseX = 0;
//        int baseY = 60;
//        String text = "abcdefghijk";
//        Paint.FontMetrics metrics = mPaint.getFontMetrics();
//        canvas.drawText(text, baseX, baseY, mPaint);
//        mPaint.setColor(Color.BLACK);
//        float stopX = baseX + mPaint.measureText(text);
//        canvas.drawLine(baseX,baseY, stopX,baseY,mPaint);
//
//        mPaint.setColor(Color.RED);
//        canvas.drawLine(baseX,baseY+metrics.ascent, stopX,baseY+metrics.ascent,mPaint);
//
//        float topy = baseY + metrics.top;
//        mPaint.setColor(Color.RED);
//        canvas.drawLine(baseX,topy,stopX,topy,mPaint);
//
//        float bottomY = baseY + metrics.bottom;
//        mPaint.setColor(Color.GREEN);
//        canvas.drawLine(baseX,bottomY,stopX,bottomY,mPaint);
//
//        float descentY = baseY + metrics.descent;
//        mPaint.setColor(Color.RED);
//        canvas.drawLine(baseX,descentY,stopX,descentY,mPaint);



        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Rect rec = new Rect(0,0,width,height);
        mPaint.setColor(Color.RED);
        canvas.drawRect(rec,mPaint);




    }



}
