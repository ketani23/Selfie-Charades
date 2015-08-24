package com.example.jenil.parsedemo;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by adas on 5/9/2015.
 */
public class PagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener{
    private ViewPager vPager;
    boolean needsRedraw = false;

    boolean deleteAction=false;

    private Point center = new Point();
    private Point initialTouch = new Point();

    int oldPos;

    public PagerContainer(Context context){
        super(context);
        init();
    }

    public PagerContainer(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public PagerContainer(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setClipChildren(false);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onFinishInflate(){
        try {
            vPager = (ViewPager) getChildAt(0);
            vPager.setOnPageChangeListener(this);
            oldPos = vPager.getCurrentItem();
        }catch(Exception e){
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }

    public ViewPager getViewPager(){
        return vPager;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        center.x = w/2;
        center.y = h/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                initialTouch.x = (int)ev.getX();
                initialTouch.y = (int)ev.getY();
            default:
                ev.offsetLocation(center.x - initialTouch.x, center.y - initialTouch.y);
        }
        return vPager.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
        if(needsRedraw) {
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int Position){
        //nothing to do
    }

    @Override
    public void onPageScrollStateChanged(int state){
        needsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);
    }
}
