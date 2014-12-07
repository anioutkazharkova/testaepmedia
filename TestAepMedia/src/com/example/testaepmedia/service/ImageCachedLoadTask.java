package com.example.testaepmedia.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.testaepmedia.database.DatabaseHelper;

//Task to get cached image 
public class ImageCachedLoadTask extends AsyncTask<Integer,Integer, Bitmap> {

	private Context mContext;
	private ImageView image;	//imageview
	private DatabaseHelper dbHelper;
	
	private int index=-1;
	
	public ImageCachedLoadTask() {
		
	}
public	ImageCachedLoadTask(Context context,ImageView view){
	mContext=context;
	image=view;
	dbHelper=new DatabaseHelper(mContext);
}
	@Override
	protected Bitmap doInBackground(Integer... params) {
		
		if (params!=null && params.length>0)
		{
			index=params[0];
			//Get cached bitmap
			return getCachedBitmap(index);
			
		}
		return null;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		
		if (result!=null)
		{
			if (image!=null)
			{
				try{
				//Set image to imageview
				image.setImageBitmap(result);	
				}
				catch(OutOfMemoryError e)
				{
					
				}
				finally{
					result=null;
					
				}
			}
		}
		dbHelper.close();
	}
	
	//Retrieving cached image data
	private Bitmap getCachedBitmap(int index)
	{
		//Opening database to read
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		//Selecting row for specified row id
		Cursor cursor=db.rawQuery("select * from image_entity where id = ?", new String[]{index+""});
		if (cursor!=null && cursor.getCount()>0)
		{
			cursor.moveToFirst();
			//Getting image path
			String path =cursor.getString(cursor.getColumnIndex("url"));
			try{
			//Creating bitmap for current file
			return BitmapFactory.decodeFile(mContext.getCacheDir().getAbsolutePath()+"/"+UtilityMethods.CACHE+"/"+path);
			}
			catch(Exception e)
			{
				
			}
		}
		cursor.close();
		db.close();
		
		return null;
	}
	
}
