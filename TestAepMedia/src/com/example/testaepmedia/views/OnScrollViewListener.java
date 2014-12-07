package com.example.testaepmedia.views;

import android.widget.ScrollView;

//Custom listener to monitor ScrollChanged event of ScrollView
public interface OnScrollViewListener {
void onScrollChanged(ScrollView scroll,int x, int y, int oldx, int oldy);
}
