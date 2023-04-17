/*
 * CopyRight(c) WYZE CO.LTD 2018.
 */

package com.hjq.demo.ui.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
    private boolean scroll = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /*return false;//super.onTouchEvent(arg0);*/
        if (scroll)
            return super.onTouchEvent(arg0);
        else
            return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (scroll)
            return super.onInterceptTouchEvent(arg0);
        else
            return true;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }
}
