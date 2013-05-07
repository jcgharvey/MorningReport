package com.example.morningreport;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItemsDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] columns = { SQLiteHelper.ITEMS_COLUMN_ID,
            SQLiteHelper.ITEMS_COLUMN_TITLE, SQLiteHelper.ITEMS_COLUMN_URL, SQLiteHelper.ITEMS_COLUMN_DESC, SQLiteHelper.ITEMS_COLUMN_FEED, SQLiteHelper.ITEMS_COLUMN_DOA };

    public ItemsDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public Item createItem(String title, String url, String desc, String feedTitle ) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.ITEMS_COLUMN_TITLE, title);
        values.put(SQLiteHelper.ITEMS_COLUMN_URL, url);
        values.put(SQLiteHelper.ITEMS_COLUMN_DESC, desc);
        values.put(SQLiteHelper.ITEMS_COLUMN_FEED, feedTitle);
        values.put(SQLiteHelper.ITEMS_COLUMN_DOA, "ALIVE");
        long insertID = database.insert(SQLiteHelper.TABLE_ITEMS, null, values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_ITEMS, columns,
                SQLiteHelper.ITEMS_COLUMN_ID + " = " + insertID, null, null, null,
                null);
        cursor.moveToFirst();
        Item item = new Item(title, url, desc, feedTitle);
        cursor.close();
        return item;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_ITEMS, columns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return items;
    }
    
    public Item getItemForId(int id){
    	Item item = null;
        String[] urlCol = {SQLiteHelper.ITEMS_COLUMN_URL};
        Cursor cursor = database.query(SQLiteHelper.TABLE_FEEDS, urlCol,
                SQLiteHelper.ITEMS_COLUMN_ID + " = " + id, null, null,
                null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
        	item = new Item(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));//cursor.getString(0);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
    	return item;
    }

    public String getUrlForTitle(String title) {
        String url = null;
        String[] urlCol = {SQLiteHelper.ITEMS_COLUMN_URL};
        Cursor cursor = database.query(SQLiteHelper.TABLE_FEEDS, urlCol,
                SQLiteHelper.ITEMS_COLUMN_TITLE + " = '" + title + "'", null, null,
                null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            url = cursor.getString(0);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return url;
    }
    
    public void clear(){
        database.delete(SQLiteHelper.TABLE_ITEMS, null, null);
    }
    
    private Item cursorToItem(Cursor cursor) {
        // public Item(String title, String url, String desc, String feedTitle) 
        Item i = new Item(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return i;
    }
}