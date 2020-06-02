package com.constantinpricope.cpnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DataBaseInterface {
    Context context;
    public long last_id = 0;
//    FeedReaderDbHelper dbHelper;

    public DataBaseInterface(Context context)
    {
        this.context = context;
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
    }

    public String readFromDatabase(String title)
    {
        String returned_string = "";
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };
        String[] selectionArgs = { title };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        try{
            if (cursor.moveToFirst()) {
                returned_string = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));
            }
        }
        catch (Exception e)
        {
            cursor.close();
        }

        return  returned_string;
    }

    public void writeToDatabase(String title, String subtitle)
    {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        last_id = newRowId;
    }

    public void deleteFromDatabase(String title)
    {
        // Define 'where' part of query.
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = { title };
// Issue SQL statement.
        int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    public long getLast_id() {
        return last_id;
    }
}
