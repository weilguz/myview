package com.myview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author weilgu
 * @time 2017/10/7  10:31
 * @desc 侧拉菜单
 */

public class CelaMenu extends ViewGroup {

    private View menu;
    private View context;
    private int menuWidth;
    private int menuHeight;
    private int conWidth;
    private int conHeight;
    private float startX;
    private float startY;
    private float endX;
    private int scrollX;
    private Scroller mScroller;

    public CelaMenu(Context context) {
        this(context,null,0);
    }

    public CelaMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CelaMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        menu = getChildAt(0);
        context = getChildAt(1);

        menuWidth = menu.getMeasuredWidth();
        menuHeight = menu.getMeasuredHeight();

        conWidth = context.getMeasuredWidth();
        conHeight = context.getMeasuredHeight();


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        menu.layout(-menuWidth+pianyi,0,0+pianyi,menuHeight);第一种方法
        menu.layout(-menuWidth,0,0,menuHeight);

//        context.layout(0+pianyi,0,conWidth+pianyi,conHeight);第一种方法
        context.layout(0,0,conWidth,conHeight);
    }

//    int pianyi = 0;第一种方法


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();
                float endY = ev.getY();

                //计算偏移量
                int dx = (int) (endX - startX);
                int dy = (int) (endY - startY);

                if (Math.abs(dx) > Math.abs(dy)){
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){

            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float endX =  event.getX();
                float endY = event.getY();

                //计算偏移量
                int dx= (int)(endX - startX);
                int dy= (int)(endY- startY);
//                pianyi += dx; 第一种方法
                scrollBy(-dx,0); //第二种方法  //方向是反的

                int scrollX =  getScrollX();


                if(scrollX > 0){  //第二种方法  判断是否过界
                    scrollTo(0,0);
                }else if(scrollX < -menuWidth){
                    scrollTo(-menuWidth,0);
                }
                startY = endY;
                startX = endX;
                break;
            case MotionEvent.ACTION_UP:
//                if (pianyi >= menuWidth /2){  //第一种方法
//                    pianyi = menuWidth ;
//                }else{
//                    pianyi = 0;
//                }
                int scrollX1 = getScrollX();  //第二种方法
                if (scrollX1 <= -menuWidth / 2){
                    mScroller.forceFinished(true);
                    // -scroll1 + ?  =-menuWidth
                    mScroller.startScroll(scrollX1,0,-menuWidth-scrollX1,0);
//                    scrollTo(-menuWidth,0);

                }else if(scrollX1 > -menuWidth / 2){
                    //scrollX1 + ? =0;
                    mScroller.startScroll(scrollX1,0,0-scrollX1,0);
//                    scrollTo(0,0);
                }

                break;
        }
//        requestLayout();  //第一种方法
        invalidate();
        return true;
    }

    //每次重绘都会调用该方法
    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()){  //判断是否正在计算滚动的位移 false 代表动画结束
            //获取控件在当前动画中所在的位置(x,y)
            int currX = mScroller.getCurrX();

            scrollTo(currX,0);
            invalidate();//
        }
    }
}
