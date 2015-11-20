package com.reader.dialysis.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.reader.dialysis.Model.AVReadTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import test.dorothy.graduation.activity.R;

/**
 * Created by dorothy on 15/5/18.
 */
public class ReadProgressView extends View {

    private final String[] week = {"M", "Tu", "W", "Th", "F", "Sa", "Su"};

    private final int LINE_HORIZONTAL = 7;
    private final int LINE_VERTICAL = 7;

    private float CELL_HEIGHT;
    private float CELL_WIDTH;

    private float VIEW_WIDTH;
    private float VIEW_HEIGHT;

    private float HORIZONTAL_LINE_WIDTH;
    private float VERTICAL_LINE_HEIGHT;

    private float TEXT_AND_LINE_SPACE;

    private Point mHorizontalStartPoint;
    private Point mVerticalStartPoint;
    private float VERTICAL_TEXT_LENGTH;
    private float VERTICAL_TEXT_HEIGHT;

    private Context mContext;

    private Paint mLinePaint;
    private Paint mCoordinatePaint;
    private Paint mPathPaint;
    private Paint mFillPaint;
    private Paint mCirclePaint;

  //  private List<AVReadTime> mAvRedTimeList = new ArrayList<>();
    private List<Long> mReadTimeList = new ArrayList<>();
    private List<Long> mOrdinateValues = new ArrayList<>();
    private List<Point> mLinePoints = new ArrayList<>();

    public ReadProgressView(Context context) {
        this(context, null);
    }

    public ReadProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = (int) VIEW_WIDTH;
        int desiredHeight = (int) VIEW_HEIGHT;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY) {
            VIEW_HEIGHT = height;
            VIEW_WIDTH = width;
            initPaint();
            init();
        }

        setMeasuredDimension(width, height);
    }

    private void init() {
        TEXT_AND_LINE_SPACE = mContext.getResources().getDimension(R.dimen.width3);
        Rect measureRect = new Rect();
        String measureStr = "99999";
        mCoordinatePaint.getTextBounds(measureStr, 0, measureStr.length(), measureRect);
        VERTICAL_TEXT_LENGTH = measureRect.width();
        VERTICAL_TEXT_HEIGHT = measureRect.height();
        HORIZONTAL_LINE_WIDTH = VIEW_WIDTH - VERTICAL_TEXT_LENGTH - TEXT_AND_LINE_SPACE;
        CELL_WIDTH = HORIZONTAL_LINE_WIDTH / LINE_VERTICAL;
        measureStr = "Sa";
        mCoordinatePaint.getTextBounds(measureStr, 0, measureStr.length(), measureRect);
        VERTICAL_LINE_HEIGHT = VIEW_HEIGHT - measureRect.height() - TEXT_AND_LINE_SPACE;
        CELL_HEIGHT = VERTICAL_LINE_HEIGHT / LINE_HORIZONTAL;

        mHorizontalStartPoint = new Point();
        mVerticalStartPoint = new Point();
        float xStartX = VERTICAL_TEXT_LENGTH + TEXT_AND_LINE_SPACE;
        float yStartX = xStartX + CELL_WIDTH / 2;
        mHorizontalStartPoint.set((int) xStartX, (int) CELL_HEIGHT);
        mVerticalStartPoint.set((int) yStartX, 0);
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mContext.getResources().getColor(R.color.progress_line_grey));
        mLinePaint.setStrokeWidth(mContext.getResources().getDimension(R.dimen.width0));
        mLinePaint.setAntiAlias(true);

        mCoordinatePaint = new Paint();
        mCoordinatePaint.setTextSize(mContext.getResources().getDimension(R.dimen.textsize13));
        mCoordinatePaint.setColor(mContext.getResources().getColor(R.color
                .progress_coordinate_grey));
        mCoordinatePaint.setStyle(Paint.Style.STROKE);
        mCoordinatePaint.setStrokeWidth(mContext.getResources().getDimension(R.dimen.width0));

        mPathPaint = new Paint();
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(mContext.getResources().getDimension(R.dimen.width0));
        mPathPaint.setAntiAlias(true);
        mPathPaint.setColor(Resources.getSystem().getColor(android.R.color.holo_blue_light));

        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mContext.getResources().getColor(R.color.progress_fill));

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(mContext.getResources().getDimension(R.dimen.width3));
        mCirclePaint.setColor(Resources.getSystem().getColor(android.R.color.holo_blue_dark));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int xStartX = mHorizontalStartPoint.x;
        int xStartY = mHorizontalStartPoint.y;
        for (int i = 0; i < LINE_HORIZONTAL; i++) {
            canvas.drawLine(xStartX, xStartY, xStartX + HORIZONTAL_LINE_WIDTH, xStartY, mLinePaint);
            xStartY += CELL_HEIGHT;
            d("xStartY" + xStartY);
        }

        int yStartX = mVerticalStartPoint.x;
        int yStartY = mVerticalStartPoint.y;
        for (int i = 0; i < LINE_VERTICAL; i++) {
            canvas.drawLine(yStartX, yStartY, yStartX, yStartY + VERTICAL_LINE_HEIGHT, mLinePaint);
            yStartX += CELL_WIDTH;
            d("yStartX" + yStartX);
        }

        Calendar curr = Calendar.getInstance();
        Rect rect = new Rect();
        mCoordinatePaint.getTextBounds("Sa", 0, 2, rect);
        int dayHeight = rect.height();
        Paint.FontMetrics metrics = mCoordinatePaint.getFontMetrics();
        float y = VERTICAL_LINE_HEIGHT + TEXT_AND_LINE_SPACE + dayHeight - metrics.bottom;
        for (int i = 0; i < 7; i++) {
            String day = week[curr.get(Calendar.DAY_OF_WEEK) - 1];
            float dayLength = mCoordinatePaint.measureText(day, 0, day.length());
            float x = mHorizontalStartPoint.x + CELL_WIDTH * (i + 0.5f) - dayLength / 2;
            canvas.drawText(day, x, y, mCoordinatePaint);
            curr.add(Calendar.DAY_OF_WEEK, -1);
        }

        drawOrdinate(canvas);
        drawPath(canvas);
        drawFill(canvas);
        drawCircle(canvas);

    }

    private void drawOrdinate(Canvas canvas) {
        Collections.sort(mReadTimeList, new Comparator<Long>() {
            @Override
            public int compare(Long lhs, Long rhs) {
                return lhs-rhs>0?1:-1;
            }
        });
        if (mReadTimeList.size() < 7) {
            for (int i = 0; i < 7-mReadTimeList.size(); i++) {
                mReadTimeList.add(0,0L);
            }
        }

        long maxTime = mReadTimeList.get(0);
        long minTime = mReadTimeList.get(mReadTimeList.size() - 1);
        long timeDis = (maxTime - minTime) / (LINE_VERTICAL - 2);
        long curTime = minTime;
        mOrdinateValues.clear();
        for (int i = 0; i < LINE_VERTICAL - 1; i++) {
            mOrdinateValues.add(curTime);
            curTime += timeDis;
        }

        for (int i = 0; i < mOrdinateValues.size(); i++) {
            String text = Long.toString(mOrdinateValues.get(i));
            float length = mCoordinatePaint.measureText(text, 0, text.length());
            float startX = VERTICAL_TEXT_LENGTH - length;
            float startY = CELL_HEIGHT * (i + 1) + VERTICAL_TEXT_HEIGHT / 2;
            canvas.drawText(text, startX, startY, mCoordinatePaint);
        }
    }

    private void drawPath(Canvas canvas) {
        Path linePath = new Path();
        long minTime = mReadTimeList.get(0);
        long maxTime = mReadTimeList.get(mReadTimeList.size() - 1);
        long timeTotalDis = maxTime - minTime;
        if (timeTotalDis <= 0) {
            return;
        }
        float coordinateLength = CELL_HEIGHT * (LINE_HORIZONTAL - 2);
        for (int i = 0; i < LINE_VERTICAL; i++) {
            Point point = new Point();
            float x = mVerticalStartPoint.x + i * CELL_WIDTH;
            long time = mReadTimeList.get(i);
            float y = VERTICAL_LINE_HEIGHT - ((time - minTime) * coordinateLength) /
                    timeTotalDis - CELL_HEIGHT;
            point.set((int) x, (int) y);
            mLinePoints.add(point);
            if (i == 0) {
                linePath.moveTo(x, y);
            } else {
                linePath.lineTo(x, y);
            }
        }

        canvas.drawPath(linePath, mPathPaint);
    }

    private void drawFill(Canvas canvas) {
        Path fillPath = new Path();
        fillPath.moveTo(mVerticalStartPoint.x, VERTICAL_LINE_HEIGHT);
        for (int i = 0; i < mLinePoints.size(); i++) {
            Point point = mLinePoints.get(i);
            fillPath.lineTo(point.x, point.y);
        }
        float x1 = mHorizontalStartPoint.x + HORIZONTAL_LINE_WIDTH - CELL_WIDTH / 2;
        float y1 = VERTICAL_LINE_HEIGHT;
        fillPath.lineTo((int) x1, (int) y1);

        float x2 = mVerticalStartPoint.x;
        float y2 = VERTICAL_LINE_HEIGHT;
        fillPath.lineTo((int) x2, (int) y2);

        fillPath.close();
        canvas.drawPath(fillPath, mFillPaint);
    }

    private void drawCircle(Canvas canvas) {
        for (int i = 0; i < mLinePoints.size(); i++) {
            Point point = mLinePoints.get(i);
            canvas.drawCircle(point.x, point.y, 8, mCirclePaint);
        }
    }

    public void setReadTime(List<AVReadTime> avReadTimeList) {
        mReadTimeList.clear();
        for(AVReadTime avReadTime: avReadTimeList){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(avReadTime.getDate("read_date"));
            d("day " + calendar.get(Calendar.DAY_OF_WEEK) + " time "  + avReadTime.getReadTime());
            mReadTimeList.add(avReadTime.getReadTime());
        }
        invalidate();
    }

    private void d(String msg) {
        Log.d("ReadProgressView", "ReadProgressView " + msg);
    }
}
