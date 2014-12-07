package com.example.testaepmedia.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

//Custom ScrollView class with custom ScrollChanged listener

public class ScrollViewWithListener extends ScrollView {

	private OnScrollViewListener mOnScrollViewListener; //custom listener

	//Set custom listener to monitor ScrollChanged event
	public void setOnScrollViewListener(OnScrollViewListener listener) {
	    this.mOnScrollViewListener = listener;
	}
	public ScrollViewWithListener(Context context) {
		super(context);
		
	}
	public ScrollViewWithListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewWithListener(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //Notify custom listener that ScrollChanged event has fired
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged( l, t, oldl, oldt );
    }
}
