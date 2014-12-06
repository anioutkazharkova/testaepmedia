package com.example.testaepmedia;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollViewWithListener extends ScrollView {

	private OnScrollViewListener mOnScrollViewListener;

	public void setOnScrollViewListener(OnScrollViewListener listener) {
	    this.mOnScrollViewListener = listener;
	}
	public ScrollViewWithListener(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ScrollViewWithListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewWithListener(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged( l, t, oldl, oldt );
    }
}
