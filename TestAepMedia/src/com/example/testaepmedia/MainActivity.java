package com.example.testaepmedia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.testaepmedia.entities.ImageEntity;
import com.example.testaepmedia.service.ImageCachedLoadTask;
import com.example.testaepmedia.service.ImageLoadTask;
import com.example.testaepmedia.service.UtilityMethods;
import com.example.testaepmedia.views.OnScrollViewListener;
import com.example.testaepmedia.views.ScrollViewWithListener;


public class MainActivity extends Activity implements OnClickListener{

	LinearLayout leftList, rightList; //layouts to simulate two-columned listview in common scroll
	Button resetButton;	
	
	ScrollViewWithListener scroll;	//common scroll
	
	int IMAGE_COUNT=50;		//quantity of images 
	
	List<ImageEntity> imageList;	//list of image entities
	boolean scrollingUp=false;
	
	private LoadDialogFragment dialog;	

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Initializing controls 
        leftList=(LinearLayout)findViewById(R.id.leftList);
        rightList=(LinearLayout)findViewById(R.id.rightList);
        resetButton=(Button)findViewById(R.id.resetButton);
        scroll=(ScrollViewWithListener)findViewById(R.id.scroll);
        resetButton.setOnClickListener(this);
        
        //Setting custom listener to scroll
        scroll.setOnScrollViewListener(new OnScrollViewListener() {
			
			@Override
			public void onScrollChanged(ScrollView scroll, int x, int y, int oldx,
					int oldy) {
				
				//Height of scroll view 
				int totalHeight=scroll.getChildAt(0).getMeasuredHeight();		
				
				//Getting the direction of scrolling
				if (y>oldy)
				{
					scrollingUp=false;
					
				}
				else
				{
					scrollingUp=true;
					
				}
				//Changing the opacity of button depending of scroll position
				float opacity=1-((float)y/(float)totalHeight);
				setButtonOpacity(opacity);				
			
			}
		});
        
        //Loading and setting cached images on thread
        Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {				
				initCachedImages();
			}
		});
        thread.start();
        
    }
    
    //Changing button opacity
    public void setButtonOpacity(float opacity)
    {
    resetButton.setAlpha(opacity);
    }

    public void initCachedImages(){
    	//Cleaning both layouts from images
    	leftList.post(new Runnable(){

			@Override
			public void run() {
				
				leftList.removeAllViews();
			}
    		
    	});
		rightList.post(new Runnable() {
			
			@Override
			public void run() {				
				rightList.removeAllViews();
			}
		});  	
    	
    	for(int i=0;i<IMAGE_COUNT;i++)
    	{
    		//Creating new view to add to layout
    		final View view=LayoutInflater.from(this).inflate(R.layout.image_item_layout,null);
    		ImageView image=(ImageView) view.findViewById(R.id.image);
    		
    		try {
    			//Getting cached image bitmap via index and setting to imageview
				Bitmap bm=new ImageCachedLoadTask(this,image).execute(i+1).get();
				bm=null;
			} catch (InterruptedException e) {				
				e.printStackTrace();
			} catch (ExecutionException e) {				
				e.printStackTrace();
			}
    		finally{
    			//Adding created view to layout
    			if (i%2==0)
    			{
    				leftList.post(new Runnable(){

    					@Override
    					public void run() {
    						// TODO Auto-generated method stub
    						leftList.addView(view);
    					}
    		    		
    		    	});
    			
    			}
    			else
    			{
    				rightList.post(new Runnable() {
    					
    					@Override
    					public void run() {
    						// TODO Auto-generated method stub
    						rightList.addView(view);
    					}
    				});
    			}
    		}
    	}
    	//Dismissing dialog
    	if (dialog!=null)
    	{
    		dialog.dismiss();
    	}
    	
    }
  
    //Loading images from network
	public void initImagesFromUrl()
    {
    	//Removing all views from both layouts
    	leftList.post(new Runnable(){

			@Override
			public void run() {
				
				leftList.removeAllViews();
			}
    		
    	});
		rightList.post(new Runnable() {
			
			@Override
			public void run() {				
				rightList.removeAllViews();
			}
		});
    	
    	//Creating new list of images
    	imageList=new ArrayList<ImageEntity>();
    	
    	//For each of 50 items generating new url
    	for(int i=0;i<IMAGE_COUNT;i++)
    	{
    		ImageEntity item=new ImageEntity();
    		item.setLink(UtilityMethods.getImageUrl(this));
    		imageList.add(item);
    	}
    	
    	for(int i=0;i<IMAGE_COUNT;i++)
    	{
    		//Creating new view
    		final View view=LayoutInflater.from(this).inflate(R.layout.image_item_layout,null);
    		ImageView image=(ImageView) view.findViewById(R.id.image);
    		
    		try {
    			//Loading image from network and setting to imageview
				Bitmap bm=new ImageLoadTask(image,this).execute(imageList.get(i).getLink(),(i+1)+"").get();
				bm=null;
			} catch (InterruptedException e) {				
				e.printStackTrace();
			} catch (ExecutionException e) {				
				e.printStackTrace();
			}
    		finally{
    			//Adding new view to layout
    			if (i%2==0)
    			{
    				leftList.post(new Runnable(){

    					@Override
    					public void run() {    						
    						leftList.addView(view);
    					}
    		    		
    		    	});
    			
    			}
    			else
    			{
    				rightList.post(new Runnable() {
    					
    					@Override
    					public void run() {    						
    						rightList.addView(view);
    					}
    				});
    		    	
    			}
    			
    		}
    	}
    	//Dismissing dialog
    	if (dialog!=null)
    	{
    		dialog.dismiss();
    	}
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		//If user clicked to reset button
		case R.id.resetButton:
		{
			if (UtilityMethods.isNetworkAvailable(this))
			{
			//Showing progress dialog
			dialog=new LoadDialogFragment();
	    	dialog.show(getFragmentManager(), "load");
	    	
	    	//If network is available
	    	
	    	//Cleaning all cache
	    	UtilityMethods.cleanCache(this);
	    	
	    	//For better performance using of thread to load and set list of images
	    	Thread thread=new Thread(new Runnable(){

				@Override
				public void run() {
					//Loading images from network
					initImagesFromUrl();
				}
	    		
	    	});
	    	thread.start();
			}
			else
			{
				InfoDialogFragment infoDialog=new InfoDialogFragment();
				infoDialog.show(getFragmentManager(), "info");
			}
			
		}
			break;
		}
	}
	


   
}
