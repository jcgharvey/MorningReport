package com.example.morningreport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_FEEDS = "feeds";
	public static final String FEEDS_COLUMN_ID = "id";
	public static final String FEEDS_COLUMN_TITLE = "title";
	public static final String FEEDS_COLUMN_URL = "url";

	public static final String TABLE_ITEMS = "items";
	public static final String ITEMS_COLUMN_ID = "id";
	public static final String ITEMS_COLUMN_TITLE = "title";
	public static final String ITEMS_COLUMN_URL = "url";
	public static final String ITEMS_COLUMN_DESC = "desc";
	public static final String ITEMS_COLUMN_FEED = "feed";
	public static final String ITEMS_COLUMN_DOA = "doa";


	private static final String DATABASE_NAME = "morningreport.db";
	private static final int DATABASE_VERSION = 2;

	private static final String FEEDS_TABLE_CREATE = "create table if not exists "
			+ TABLE_FEEDS
			+ "("
			+ FEEDS_COLUMN_ID
			+ " integer primary key autoincrement, "
			+ FEEDS_COLUMN_TITLE
			+ " text not null, " + FEEDS_COLUMN_URL + " text not null);";
	private static final String ITEMS_TABLE_CREATE = "create table if not exists "
			+ TABLE_ITEMS
			+ "("
			+ ITEMS_COLUMN_ID + " integer primary key autoincrement, "
			+ ITEMS_COLUMN_TITLE + " text not null, "
			+ ITEMS_COLUMN_URL + " text not null, "
			+ ITEMS_COLUMN_DESC + " text, "
			+ ITEMS_COLUMN_FEED + " text not null, "
			+ ITEMS_COLUMN_DOA + " text not null"
			+ ");";

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(FEEDS_TABLE_CREATE);
		database.execSQL(ITEMS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		onCreate(db);
	}

}
