package com.advantej.mobile.android.tests.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    private static final int DB_VERSION = 2;


    private static final String TABLE1_CREATE
            = "create table " + DBContract.TABLE1 + " ( "
            + DBContract.Table1Columns._ID + " integer primary key, "
            + DBContract.Table1Columns.TABLE1COL1 + " text "
            + ");" ;

    private static final String TABLE2_CREATE
            = "create table " + DBContract.TABLE2 + " ( "
            + DBContract.Table2Columns._ID + " integer primary key, "
            + DBContract.Table2Columns.TABLE2COL1 + " text "
            + ");" ;

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

    private class MyDBHelper extends SQLiteOpenHelper{


        public MyDBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(TABLE1_CREATE);
            sqLiteDatabase.execSQL(TABLE2_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            Log.d(TAG, "Upgrading database from version " + oldVersion + " to version  " + newVersion);

            // In a real app, you'd do a proper upgrade rather than dropping the table like this.
            sqLiteDatabase.execSQL("drop table if exists " + DBContract.TABLE1);
            sqLiteDatabase.execSQL("drop table if exists " + DBContract.TABLE2);
            onCreate(sqLiteDatabase);
        }
    }


    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy, String limit){
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if(DBContract.TABLE1.equals(table)){
            cursor.setNotificationUri(mContext.getContentResolver(), DBContract.CONTENT_URI_TABLE1);
        }else if (DBContract.TABLE2.equals(table)){
            cursor.setNotificationUri(mContext.getContentResolver(), DBContract.CONTENT_URI_TABLE2);
        }

        return cursor;
    }

    public long insert(String table, String nullColumnHack, ContentValues values){
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        long rowID = db.insert(table, nullColumnHack, values);
        //Notify only if row(s) are inserted
        if (rowID > 0){
            notifyChangeToRespectiveObserver(table);
        }
        return rowID;
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        int count = db.update(table, values, whereClause, whereArgs);
        if(count > 0){
            notifyChangeToRespectiveObserver(table);
        }
        return count;
    }

    public int delete(String table, String whereClause, String[] whereArgs){
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        int count = db.delete(table, whereClause, whereArgs);
        //if(count > 0){
            notifyChangeToRespectiveObserver(table);
        //}
        return count;
    }

    private void notifyChangeToRespectiveObserver(String table){
        if(DBContract.TABLE1.equals(table)){
            mContext.getContentResolver().notifyChange(DBContract.CONTENT_URI_TABLE1, null);
        }else if (DBContract.TABLE2.equals(table)){
            mContext.getContentResolver().notifyChange(DBContract.CONTENT_URI_TABLE2, null);
        }
    }

    //Methods for bulk operations go here

}
