package com.example.amusu.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ChatDatabaseHelper";
    private static final String DATABASE_NAME = "Messages.db";
    private static int VERSION_NUM = 2;
    private static final String TABLE_NAME = "MessageList";
    private static final String KEY_ID = "_id", KEY_MESSAGE = "message";


    ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Calling onCreate");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE  + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Calling onUpgrade, oldVersion = " + oldVersion +"newVersion = " +newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);


    }
    public List<String> getAllMessages(){
        final List<String> list = new ArrayList<>();
        final SQLiteDatabase db = this.getReadableDatabase();
        this.onOpen(db);
        Cursor c = db.query(true, TABLE_NAME, null,null,null,null,null,null,null);
        Log.i(TAG, "Cursor's column count = " + c.getColumnCount());
        for (int i =0; i<c.getColumnCount();i++){
            Log.i(TAG,"Column name = " + c.getColumnName(i));
        }
        while (c.moveToNext()) {
            final String message = c.getString(c.getColumnIndex(KEY_MESSAGE));
            Log.i(TAG, "SQL MESSAGE: " + message);
            list.add(message);
        }
        c.close();
        return list;
    }

    public void insertMessage(String msg) {
        final ContentValues cValue= new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        cValue.put(KEY_MESSAGE,msg);
        long searchId = db.insert(TABLE_NAME,null,cValue);
        Cursor c = db.query(TABLE_NAME,new String[]{KEY_MESSAGE}, KEY_ID + " = " + searchId,null,null,null,null);
        c.moveToFirst();
        Log.i(TAG, "SQL MESSAGE: " + c.getString(c.getColumnIndex(KEY_MESSAGE)));
        c.close();

    }



}







