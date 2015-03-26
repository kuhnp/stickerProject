package com.example.rrhg5930.stickerproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pierre on 26/03/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StickerDatabase";



    private static final String DATABASE_CREATE = "create table friends (" +
        "_id integer primary key autoincrement, "+
        "name text not null);";


    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
