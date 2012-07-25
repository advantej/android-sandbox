package com.advantej.mobile.android.tests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 7/23/12
 * Time: 2:23 PM
 */
public class MyDB {

    private static final String TAG = "MyDB";

    private static final String DB_NAME = "my.db";
    private static final int DB_VERSION = 1;
    public static final String T_TABLE = "mytable";


    /** The authority for the contacts provider */
    public static final String AUTHORITY = "com.advantej.mobile.android";

    /** A content:// style uri to the authority for this table */
    // Uri is used just to use for associating with a cursor so that we can use notifyChange.
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/mytable");

    private Context mContext;

    private MyDBHelper myDBHelper;

    private static MyDB mMyDB;

    private MyDB() {}

    private MyDB(Context context){
        myDBHelper = new MyDBHelper(context);
        mContext = context;
    }

    public static MyDB getDataBaseManager(Context context){

        if(mMyDB == null){
           mMyDB = new MyDB(context);
        }
        return mMyDB;
    }

    public class MyDBColumns implements BaseColumns {
        private MyDBColumns() {}

        public static final String MYSTRING = "mystring";

    }

    private class MyDBHelper extends SQLiteOpenHelper{

        private static final String DB_CREATE
                = "create table " + T_TABLE + " ( "
                + MyDBColumns._ID + " integer primary key, "
                + MyDBColumns.MYSTRING + " text "
                + ");" ;

        public MyDBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.d(TAG, "Upgrading database from version " + oldVersion + " to version  " + newVersion);

            // In a real app, you'd do a proper upgrade rather than dropping the table like this.
            sqLiteDatabase.execSQL("drop table if exists " + T_TABLE);
            onCreate(sqLiteDatabase);
        }
    }


    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy, String limit){
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        cursor.setNotificationUri(mContext.getContentResolver(), CONTENT_URI);
        return cursor;
    }

    public long insert(String table, String nullColumnHack, ContentValues values){
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        mContext.getContentResolver().notifyChange(CONTENT_URI, null);
        return db.insert(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        return db.update(table, values, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs){
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        return db.delete(table, whereClause, whereArgs);
    }

    //Methods for bulk operations go here

}
