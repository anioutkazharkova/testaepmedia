package com.example.testaepmedia.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.testaepmedia.database.DatabaseHelper;

public class ImageCachedLoadTask extends AsyncTask<Integer,Integer, Bitmap> {

	private Context mContext;
	private ImageView image;
	private DatabaseHelper dbHelper;
	
	private int index=-1;
	
	public ImageCachedLoadTask() {
		// TODO Auto-generated constructor stub
	}
public	ImageCachedLoadTask(Context context,ImageView view){
	mContext=context;
	image=view;
	dbHelper=new DatabaseHelper(mContext);
}
	@Override
	protected Bitmap doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		if (params!=null && params.length>0)
		{
			index=params[0];
			return getCachedBitmap(index);
			
		}
		return null;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		if (result!=null)
		{
			if (image!=null)
			{
				try{
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
	private Bitmap getCachedBitmap(int index)
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from image_entity where id = ?", new String[]{index+""});
		if (cursor!=null && cursor.getCount()>0)
		{
			cursor.moveToFirst();
			String path =cursor.getString(cursor.getColumnIndex("url"));
			try{
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
