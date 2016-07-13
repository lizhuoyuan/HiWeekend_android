package com.geminno.animation;

import android.annotation.SuppressLint;
import android.view.View;

import com.google.android.support.v4.view.ViewPager;

public class Transformer implements ViewPager.PageTransformer {

    @SuppressLint("NewApi")
    public void transformPage(View view, float position) {

	if (position <= 1 && position >= -1) // a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
	{ // [-1,1]
	    float scaleFactor = 1.5f - Math.abs(position) / 2;
	    view.setScaleX(scaleFactor);
	    view.setScaleY(scaleFactor);
	}

    }
}