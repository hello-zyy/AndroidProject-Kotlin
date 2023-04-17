package com.hjq.demo.ui.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;


public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.95f;

    @SuppressLint("NewApi")
    public void transformPage(View view, float position) {

        Log.e("TAG", view + " , " + position + "");

        if (position < -1) { // [-Infinity,-1)
            // right
        } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
        { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);

            // Fade the page relative to its size.
//			view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
        }
    }
}