package com.example.rrhg5930.stickerproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by pierre on 26/03/2015.
 */
public class DbAdapter {

    private static final String DATABASE_FRIENDS = "friends";

    private Context context;
    private SQLiteDatabase database;
    public DbHelper dbHelper;

    public DbAdapter(Context context){
        this.context = context;
    }

    public DbAdapter open(){
        dbHelper = new DbHelper(this.context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long addFriends(ContentValues values){

        long res = database.insert(DATABASE_FRIENDS, null, values);
        Log.d("DBAdapter", "res = "+res);
        return res;
    }

    public Cursor getAllFriends(String selection, String[] selectionArgs, String order){
        Log.d("Content Provider", "Before query");
        Cursor mCursor = database.query(true,DATABASE_FRIENDS, new String[] {
                "_id","name","friendId"
        },selection,selectionArgs,null,null,order,null);
        Log.d("Content Provider", "After query, cursor size = "+mCursor.getCount());
        return mCursor;
    }
}
