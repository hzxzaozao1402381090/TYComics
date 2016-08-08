package com.zaozao.comics.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zaozao.comics.R;

/**
 * Created by 胡章孝 on 2016/7/17.
 */
public class TextProgress extends TextView {

    /**
     * 画笔对象
     */
    private Paint mPaint = new Paint();

    /**
     * 圆环的颜色
     */
    private int circleColor = Color.GREEN;
    /**
     * 圆环的宽度
     */
    private int circleWidth = 10;
    /**
     * 文字的宽度
     */
    private int textWidth = 10;
    /**
     * view的显示区域
     */
    private Rect rect = new Rect();

    public TextProgress(Context context) {
        super(context);
    }

    public TextProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public TextProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initialize(Context context, AttributeSet attrs) {
        mPaint.setAntiAlias(true);
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.TextProgress);
        circleColor = typedArray.getColor(R.styleable.TextProgress_circle_color, 0);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.getDrawingRect(rect);
        int size = rect.width() > rect.height() ? rect.height() : rect.width();
        //绘制圆环
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(circleWidth);
        canvas.drawCircle(rect.centerX(), rect.centerY(), size / 2 - circleWidth / 2, mPaint);
        //绘制文字
        Paint paint = getPaint();
        paint.setStrokeWidth(textWidth);
        paint.setAntiAlias(true);
        paint.setColor(getCurrentTextColor());
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        float textY = rect.centerY() - (paint.ascent()+paint.descent())/2;
        canvas.drawText(getText().toString(),rect.centerX(),textY,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = width > height ? width : height;
        setMeasuredDimension(size, size);
    }
}
