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


public class MainActivity extends Activity implements OnClickListener{

	LinearLayout leftList, rightList;
	Button resetButton;
	ScrollViewWithListener scroll;
	int IMAGE_COUNT=50;
	List<ImageEntity> imageList;
	boolean scrollingUp=false;
	
	private LoadDialogFragment dialog;
	public static final int PROGRESS_DIALOG=0;
	

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        leftList=(LinearLayout)findViewById(R.id.leftList);
        rightList=(LinearLayout)findViewById(R.id.rightList);
        resetButton=(Button)findViewById(R.id.resetButton);
        scroll=(ScrollViewWithListener)findViewById(R.id.scroll);
        resetButton.setOnClickListener(this);
        scroll.setOnScrollViewListener(new OnScrollViewListener() {
			
			@Override
			public void onScrollChanged(ScrollView scroll, int x, int y, int oldx,
					int oldy) {
				int totalHeight=scroll.getChildAt(0).getMeasuredHeight();
				// TODO Auto-generated method stub
				if (y>oldy)
				{
					scrollingUp=false;
					
				}
				else
				{
					scrollingUp=true;
					
				}
				float opacity=1-((float)y/(float)totalHeight);
				setButtonOpacity(opacity);
				Log.d("test", "scroll: "+y+" "+opacity+" "+totalHeight);
			}
		});
        Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initImagesFromDb();
			}
		});
        thread.start();
        
    }
    public void setButtonOpacity(float opacity)
    {
    resetButton.setAlpha(opacity);
    }

    public void initImagesFromDb(){
    	//check database and init images
    	leftList.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				leftList.removeAllViews();
			}
    		
    	});
		rightList.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				rightList.removeAllViews();
			}
		});
    	
    	
    	imageList=new ArrayList<ImageEntity>();
    	for(int i=0;i<IMAGE_COUNT;i++)
    	{
    		ImageEntity item=new ImageEntity();
    		item.setLink(UtilityMethods.getImageUrl(this));
    		imageList.add(item);
    	}
    	for(int i=0;i<IMAGE_COUNT;i++)
    	{
    		final View view=LayoutInflater.from(this).inflate(R.layout.image_item_layout,null);
    		ImageView image=(ImageView) view.findViewById(R.id.image);
    		
    		try {
    			
				Bitmap bm=new ImageCachedLoadTask(this,image).execute(i+1).get();
				bm=null;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		finally{
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
    	
    	if (dialog!=null)
    	{
    		dialog.dismiss();
    	}
    	
    }
  
	public void initImagesFromUrl()
    {
    	
    	leftList.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				leftList.removeAllViews();
			}
    		
    	});
		rightList.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				rightList.removeAllViews();
			}
		});
    	
    	
    	imageList=new ArrayList<ImageEntity>();
    	for(int i=0;i<IMAGE_COUNT;i++)
    	{
    		ImageEntity item=new ImageEntity();
    		item.setLink(UtilityMethods.getImageUrl(this));
    		imageList.add(item);
    	}
    	for(int i=0;i<IMAGE_COUNT;i++)
    	{
    		final View view=LayoutInflater.from(this).inflate(R.layout.image_item_layout,null);
    		ImageView image=(ImageView) view.findViewById(R.id.image);
    		
    		try {
    			
				Bitmap bm=new ImageLoadTask(image,this).execute(imageList.get(i).getLink(),(i+1)+"").get();
				bm=null;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		finally{
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
    	
    	if (dialog!=null)
    	{
    		dialog.dismiss();
    	}
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.resetButton:
		{
			if (UtilityMethods.isNetworkAvailable(this))
			{
			dialog=new LoadDialogFragment();
	    	dialog.show(getFragmentManager(), "load");
	    	UtilityMethods.cleanCache(this);
	    	Thread thread=new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					initImagesFromUrl();
				}
	    		
	    	});
	    	thread.start();
			}
			
		}
			break;
		}
	}
	


   
}
