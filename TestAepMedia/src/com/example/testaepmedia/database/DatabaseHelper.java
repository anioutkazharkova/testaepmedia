package com.example.testaepmedia.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME="ImageCache.db";
	private final static int VERSION=1;
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sqlQuery="create table image_entity (id integer primary key, url text, image_bytes blob);";
		db.execSQL(sqlQuery);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
