package com.example.morningreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class FeedsDataSource {
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;

	private String[] columns = { SQLiteHelper.FEEDS_COLUMN_ID,
			SQLiteHelper.FEEDS_COLUMN_TITLE, SQLiteHelper.FEEDS_COLUMN_URL };

	public FeedsDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public Feed createRSS(String title, String url) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.FEEDS_COLUMN_TITLE, title);
		values.put(SQLiteHelper.FEEDS_COLUMN_URL, url);
		long insertID = database.insert(SQLiteHelper.TABLE_FEEDS, null, values);
		Cursor cursor = database.query(SQLiteHelper.TABLE_FEEDS, columns,
				SQLiteHelper.FEEDS_COLUMN_ID + " = " + insertID, null, null, null,
				null);
		cursor.moveToFirst();
		Feed ru = new Feed(title, url);
		cursor.close();
		return ru;
	}

	public List<Feed> getAllFeeds() {
		List<Feed> rssurls = new ArrayList<Feed>();

		Cursor cursor = database.query(SQLiteHelper.TABLE_FEEDS, columns, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Feed ra = cursorToRA(cursor);
			rssurls.add(ra);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return rssurls;
	}
	
	public Feed[] getAllFeedsArray(){
		List<Feed> feeds = getAllFeeds();
		Feed[] feedArray = new Feed[feeds.size()];
		int count = 0; 
		for( Feed f: feeds){
			feedArray[count] = f;
			count++;
		}
		return feedArray;
	}
	
	public int getIdForTitle(String title) {
		int id = -1;
		String[] urlCol = {SQLiteHelper.FEEDS_COLUMN_ID};
		Cursor cursor = database.query(SQLiteHelper.TABLE_FEEDS, urlCol,
				SQLiteHelper.FEEDS_COLUMN_TITLE + " = '" + title + "'", null, null,
				null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			id = cursor.getInt(0);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return id;
	}
	
	public String getUrlForTitle(String title) {
		String url = null;
		String[] urlCol = {SQLiteHelper.FEEDS_COLUMN_URL};
		Cursor cursor = database.query(SQLiteHelper.TABLE_FEEDS, urlCol,
				SQLiteHelper.FEEDS_COLUMN_TITLE + " = '" + title + "'", null, null,
				null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			url = cursor.getString(0);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
//		if (url == null){
//			//throw exception
//		}
		return url;
	}
	
	public void clear(){
		database.delete(SQLiteHelper.TABLE_FEEDS, null, null);
	}
	
	public Map<String, String> getRssMap(){
		Map<String, String> rssurls = new HashMap<String, String>();
		String[] cols = {SQLiteHelper.FEEDS_COLUMN_TITLE, SQLiteHelper.FEEDS_COLUMN_URL};
		Cursor cursor = database.query(SQLiteHelper.TABLE_FEEDS, cols, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			rssurls.put(cursor.getString(0), cursor.getString(1));
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return rssurls;
	}

	private Feed cursorToRA(Cursor cursor) {
		Feed ra = new Feed(cursor.getString(1), cursor.getString(2));
		return ra;
	}
}
