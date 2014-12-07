package com.example.testaepmedia.service;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.example.testaepmedia.R;
import com.example.testaepmedia.database.DatabaseHelper;

public class UtilityMethods {

	public static String BASE_URL="http://placekitten.com/g/"; 
	public static String CACHE="app_directory";
	public static String IMAGE_PFX=".JPEG";
	
	//Calculating width of image 
	public static int getWidth(Context context)
	{
		DisplayMetrics metrics=context.getResources().getDisplayMetrics();
		int screenWidth=metrics.widthPixels;
		
		//Image width equals a half of screen		
		int imageWidth= ((screenWidth/2)-2*DpToPx(context,(int)context.getResources().getDimension(R.dimen.padding_image))-DpToPx(context, (int)context.getResources().getDimension(R.dimen.padding_activity)));
		
		return imageWidth;
	}
	
	//Generating height of image
	public static int getHeight()
	{
		Random rand=new Random();
		int min=100;
		int max=1024;	
		
		//Image height is a random value between 100 px and 1024 px
		return (rand.nextInt(max-min)+min);
	}
	
	//Converting dp to px
	public static int DpToPx(Context context,int value)
	{
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
	}
	
	//Retrieving url for image
	public static String getImageUrl(Context context)
	{
		return BASE_URL+getWidth(context)+"/"+getHeight();
	}
	
	//Cleaning cache directory
	public static  void cleanCache(Context context)
	{
		//Getting cache folder of application
		File cacheDir=context.getCacheDir();
		
		//If cache directory exists, removing all image files from it
		if (cacheDir.exists())
		{
		 File[] files=cacheDir.listFiles();

         if(files!=null)
         {

         for(File item:files)

             item.delete();
         }
		}
		//Deleting all rows from database
		DatabaseHelper dbHelper=new DatabaseHelper(context);
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		db.delete("image_entity", null, null);
		db.close();
		dbHelper.close();
	}
	
	//Saving image to file
	public static String saveToFile(Context context,Bitmap bitmap,int index) {
		//Getting cache folder of application
	    File cacheDir = new File(context.getCacheDir(), CACHE);
	    
	    //If cache directory do not exists, create it
	    if (!cacheDir.exists())
	    {
	    cacheDir.mkdir();
	    }
	    
	    //Get byte array from bitmap and save it as image file
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    	 bitmap.compress(CompressFormat.PNG, 0, buffer); 
    	 
    	 //Generating filename
    	 String filename=index+IMAGE_PFX;
    	 
	    try {
	    	//Writing to specified file
	        FileOutputStream fos = new FileOutputStream(new File(cacheDir,filename));
	        fos.write(buffer.toByteArray());
	        fos.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return filename;
	}
	
	//Checking newtwork state
	public static  boolean isNetworkAvailable(Context context) {
	  
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	       
	        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	        if (networkInfo != null)
	        {
	        	return  networkInfo.isConnected();
	        }
	        else return false;
	}
}
