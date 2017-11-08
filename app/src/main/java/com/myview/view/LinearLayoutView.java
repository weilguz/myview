package com.myview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.myview.R;

/**
 * @author weilgu
 * @time 2017/10/6  10:57
 * @desc 横向自定义LinearLayout
 */

public class LinearLayoutView extends ViewGroup {

    private LinearLayout linearLayout;
    private int orientation;
    private static final int vertical=0;
    private static final int horizontal=1;

    public LinearLayoutView(Context context) {
        this(context,null,0);
    }

    public LinearLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LinearLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*TypedArray typedArray = context.obtainStyledAttributes(R.styleable.LinearLayoutView);

        orientation = typedArray.getInt(R.styleable.LinearLayoutView_orientation,0);*/
        if (attrs != null){
            //获取AttributeSet中所有的XML属性的数量
            int attributeCount = attrs.getAttributeCount();
            //遍历AttributeSet的XML属性
            for (int i = 0; i < attributeCount; i++) {
                //获取attr的资源ID
                int attributeNameResource = attrs.getAttributeNameResource(i);
                switch (attributeNameResource){
                    case R.attr.orientation:
                        orientation = attrs.getAttributeIntValue(i,0);
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //要先测量下子控件,不然拿不到子控件的高度和宽度
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        switch (widthMode){

            case MeasureSpec.AT_MOST:
                if (orientation == horizontal){
                    //获得子控件的宽度和
                    int widSum = getChildWidthSum();
                    widthSize = widSum;
                }else if(orientation == vertical){
                    //获得子控件宽度的最大值
                    int widthMax = getChildWidthMax();
                    widthSize = widthMax;
                }

                break;
        }

        switch (heightMode){
            case MeasureSpec.AT_MOST:
                if (orientation == horizontal){
                    //获得子控键高度的最大值
                    int hei =getChildHeightMax();
                    heightSize = hei;
                }else if(orientation == vertical){
                    //获得子控件的高度和
                    int heiSum = getChildHeightMum();
                    heightSize = heiSum;
                }

                break;
        }
        setMeasuredDimension(widthSize,heightSize);


    }

    private int getChildWidthMax() {
        int childCount = getChildCount();
        int max = 0;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int measuredWidth = childAt.getMeasuredWidth();
            if (max < measuredWidth){
                max=measuredWidth;
            }
        }
        return max;
    }

    private int getChildHeightMum() {
        int childCount = getChildCount();
        int sum = 0;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int measuredHeight = childAt.getMeasuredHeight();
            sum += measuredHeight;
        }
        return sum;
    }

    private int getChildHeightMax() {
        int childCount = getChildCount();
        int max = 0;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int measuredHeight = childAt.getMeasuredHeight();
            if (max < measuredHeight){
                max=measuredHeight;
            }
        }
        return max;
    }

    private int getChildWidthSum() {
        int childCount = getChildCount();
        int sum = 0;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int measuredWidth = childAt.getMeasuredWidth();
            sum += measuredWidth;
        }
        return sum;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        int iWidth = 0;
        int iHeith = 0;
        for (int i = 0; i <childCount; i++) {

            View childAt = getChildAt(i);

            int width = childAt.getMeasuredWidth();
            int height = childAt.getMeasuredHeight();

            if (orientation == horizontal){
                childAt.layout(iWidth,0,width+iWidth,height);
                iWidth+=width;
            }else if(orientation == vertical){
                childAt.layout(0,iHeith,width,iHeith+height);
                iHeith += height;
            }
        }
    }
}
