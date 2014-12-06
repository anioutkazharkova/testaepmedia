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
	
	public static int getWidth(Context context)
	{
		DisplayMetrics metrics=context.getResources().getDisplayMetrics();
		int screenWidth=metrics.widthPixels;
		int imageWidth= ((screenWidth/2)-2*DpToPx(context,(int)context.getResources().getDimension(R.dimen.padding_image))-DpToPx(context, (int)context.getResources().getDimension(R.dimen.padding_activity)));
		return imageWidth;
	}
	
	public static int getHeight()
	{
		Random rand=new Random();
		int min=100;
		int max=1024;
		
		
		
		return (rand.nextInt(max-min)+min);
	}
	
	public static int DpToPx(Context context,int value)
	{
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
	}
	
	public static String getImageUrl(Context context)
	{
		return BASE_URL+getWidth(context)+"/"+getHeight();
	}
	
	public static  void cleanCache(Context context)
	{
		File cacheDir=context.getCacheDir();
		if (cacheDir.exists())
		{
		 File[] files=cacheDir.listFiles();

         if(files!=null)
         {

         for(File item:files)

             item.delete();
         }
		}
		DatabaseHelper dbHelper=new DatabaseHelper(context);
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		db.delete("image_entity", null, null);
		db.close();
		dbHelper.close();
	}
	public static String saveToFile(Context context,Bitmap bitmap,int index) {
	    File cacheDir = new File(context.getCacheDir(), CACHE);
	    if (!cacheDir.exists())
	    {
	    cacheDir.mkdir();
	    }
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    	 bitmap.compress(CompressFormat.PNG, 0, buffer); 
    	 String filename=index+".JPEG";
	    try {
	        FileOutputStream fos = new FileOutputStream(new File(cacheDir,filename));
	        fos.write(buffer.toByteArray());
	        fos.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return filename;
	}
	public static  boolean isNetworkAvailable(Context context) {
	    if(context == null) { return false; }
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	   
	    try {
	        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	        if (networkInfo != null && networkInfo.isConnected()) {
	            return true;
	        }
	    } catch (Exception e) {
	        
	    }
	    return false;
	}
}
