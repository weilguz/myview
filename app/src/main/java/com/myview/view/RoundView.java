package com.myview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.myview.R;

/**
 * @author weilgu
 * @time 2017/10/5  1:01
 * @desc 环形进度条,进度条实心/空心显示,中间显示/隐藏当前进度百分比
 */

public class RoundView extends View {

    /**
     * 圆形的颜色
     */
    private int roundColor;

    /**
     * 进度条的颜色
     */
    private int roundProgressColor;

    /**
     * 当前进度的颜色
     */
    private int progress;

    /**
     * 中间进度百分比的颜色
     */
    private int textColor;

    /**
     * 中间进度数字
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度显示的风格,是否实心
     */
    private int style;
    private static final int STROKE = 0;
    private static final int FILL =1;

    private Paint mPaint;


    public RoundView(Context context) {
        this(context,null,0);
    }

    public RoundView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();

        //获取TypedArray
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundView);

        //获取自定义属性和默认值
        roundColor = typedArray.getColor(R.styleable.RoundView_roundColor, Color.RED);
        roundProgressColor = typedArray.getColor(R.styleable.RoundView_roundProgressColor, Color.GREEN);
        textColor = typedArray.getColor(R.styleable.RoundView_textColor, Color.GREEN);

        textSize = typedArray.getDimension(R.styleable.RoundView_textSize, 15);
        roundWidth = typedArray.getDimension(R.styleable.RoundView_roundWidth, 5);

        max = typedArray.getInteger(R.styleable.RoundView_max, 100);
        textIsDisplayable = typedArray.getBoolean(R.styleable.RoundView_textIsDisplayable, true);

        style = typedArray.getInt(R.styleable.RoundView_style, 0);

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */

        int centre = getWidth() / 2;//获得圆心的坐标
        int radius = (int)(centre - roundWidth / 2);//圆环的半径
        mPaint.setColor(roundColor);//设置圆环的颜色
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        mPaint.setStrokeWidth(roundWidth);//设置圆环的宽度
        mPaint.setAntiAlias(true);//消除锯齿
        canvas.drawCircle(centre,centre,radius,mPaint);

        /**
         * 画进度百分比
         */
        mPaint.setStrokeWidth(0);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体
        //中间的进度都先转为float,在进行除法运算
        int percent =(int)((float) progress / (float) max * 100);
        float textWidth = mPaint.measureText(percent + "%");//测量字体的宽度

        if (textIsDisplayable && textWidth != 0 && style == STROKE){
            Paint.FontMetrics font = mPaint.getFontMetrics();
            float bottom = font.bottom;

            float ascent = mPaint.ascent();
            float descent = mPaint.descent();
            float v = bottom - ascent;


            //画出进度百分比   centre+(centre-v/2-.05f)
            canvas.drawText(percent+"%",centre-textWidth/2,centre+v/2+0.5f,mPaint);
        }

        /**
         * 画圆弧,画圆环的进度
         */
        //设置进度是实心还是空心
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setColor(roundProgressColor);
        //定义圆弧的形状和大小界限

        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);

        switch (style){
            case STROKE:
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval,0,360*progress/max,false,mPaint);
                break;
            case FILL:
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawArc(oval,0,360*progress/max,true,mPaint);
                break;
        }

    }

    public synchronized int getMax(){
        return max;
    }

    public synchronized void setMax(int max){
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max=max;
    }

    public synchronized int getProgress(){
        return progress;
    }

    public synchronized void setProgress(int progress){
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress>max){
            this.progress = max;
        }
        if (progress <= max){
            this.progress = progress;
            postInvalidate();//通知ui更新,可以在子线程中调用
            //Invalidata() //在主线程中通知ui更新.
        }
    }

    public int getRoundColor(){
        return roundColor;
    }

    public void setRoundColor(int roundColor){
        this.roundColor = roundColor;
    }

    public int getRoundProgressColor(){
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor){
        this.roundProgressColor = roundProgressColor;
    }

    public int getTextColor(){
        return textColor;
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    public float getTextSize(){
        return textSize;
    }

    public void setTextSize(float textSize){
        this.textSize = textSize;
    }

    public float getRoundWidth(){
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth){
        this.roundWidth = roundWidth;
    }

}
