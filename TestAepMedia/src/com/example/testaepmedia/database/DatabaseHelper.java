package com.example.testaepmedia.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Custom SqliteOpenHelper to create database that stores cached images pathes
public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME="ImageCache.db"; //database name
	private final static int VERSION=1;	
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		//Creating table to store pathes to image files
		String sqlQuery="create table image_entity (id integer primary key, url text);";
		db.execSQL(sqlQuery);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}

}
