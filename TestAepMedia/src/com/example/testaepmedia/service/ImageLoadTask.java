package com.example.testaepmedia.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.testaepmedia.database.DatabaseHelper;

public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {

	private ImageView image;
	private String link;
	private Context mContext;
	private DatabaseHelper dbHelper;
	private int index=-1;
	
 public ImageLoadTask() {
		// TODO Auto-generated constructor stub
	
	}
 
 public ImageLoadTask(ImageView view,Context context)
 {
	 this.image=view;
	 mContext=context;
	 dbHelper=new DatabaseHelper(mContext);
 }
	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (params!=null && params.length>0)
		{
			link=params[0];
			index=Integer.parseInt(params[1]);
			try {
				return downloadUrl(link);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		if (image!=null)
		{
			if (result!=null)
			{
				try{
				image.setImageBitmap(result);
				String path=UtilityMethods.saveToFile(mContext, result, index);
				savePath(path);
				}
				catch(OutOfMemoryError er)
				{
					er.printStackTrace();
				}
				finally{
					result=null;
				}
			}
		}
	}
	
	 private Bitmap downloadUrl(String strUrl) throws IOException{
	        Bitmap bitmap=null;
	        InputStream stream = null;
	        try{
	            URL url = new URL(strUrl);
	            // Creating an http connection to communcate with url
	            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	 
	            // Connecting to url */
	            urlConnection.connect();
	 
	            // Reading data from url 
	            stream = urlConnection.getInputStream();
	 
	         
	        	
	            // Creating a bitmap from the stream returned from the url 
	            bitmap = BitmapFactory.decodeStream(stream);
	 
	        }catch(Exception e){
	            Log.d("Exception while downloading url", e.toString());
	        }finally{
	        	
	            stream.close();
	        }
	        return bitmap;
	    }
	 
	 private void savePath(String path)
	 {		 
     	
			SQLiteDatabase db=dbHelper.getWritableDatabase();
			db.beginTransaction();
			try
			{
			ContentValues cv=new ContentValues();
			cv.put("id", index);
			cv.put("url", path);
			db.insert("image_entity", null, cv);
			
			db.setTransactionSuccessful();
			}
			finally
			{
				db.endTransaction();
			}
			db.close();
			dbHelper.close();
		
	 }

}
