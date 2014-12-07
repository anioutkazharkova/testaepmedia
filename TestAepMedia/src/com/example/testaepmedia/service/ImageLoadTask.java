package com.example.testaepmedia.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.testaepmedia.database.DatabaseHelper;

//Task class to process image loading via url
public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {

	private ImageView image; //imageview to set received bitmap
	private String link;	//url link		
	private Context mContext;
	private DatabaseHelper dbHelper; 	
	private int index=-1; //index of created row
	
 public ImageLoadTask() {
		
	
	}
 

 public ImageLoadTask(ImageView view,Context context)
 {
	 this.image=view;
	 mContext=context;
	 dbHelper=new DatabaseHelper(mContext);
 }
	@Override
	protected Bitmap doInBackground(String... params) {
		
		if (params!=null && params.length>0)
		{
			link=params[0];
			index=Integer.parseInt(params[1]);
			try {
				//Downloading image via url
				return downloadUrl(link);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	@Override
	protected void onPostExecute(Bitmap result) {
		
		if (image!=null)
		{
			if (result!=null)
			{
				try{
				//Setting received bitmap to imageview
				image.setImageBitmap(result);
				//Saving image to file and getting file path
				String path=UtilityMethods.saveToFile(mContext, result, index);
				//Save path to database
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
	
	//Downloading image 
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
	 
	 //Saving image file path to database
	 private void savePath(String path)
	 {		 
     	//Opening database for writing
			SQLiteDatabase db=dbHelper.getWritableDatabase();
			//The row will be inserted in transaction
			db.beginTransaction();
			try
			{
			//Inserting new row (id, path) to base 
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
