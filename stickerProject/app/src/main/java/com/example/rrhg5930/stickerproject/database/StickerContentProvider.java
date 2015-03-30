package com.example.rrhg5930.stickerproject.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by pierre on 26/03/2015.
 */
public class StickerContentProvider extends ContentProvider {

    DbAdapter database;

    private static final String AUTHORITY = "com.example.rrhg5930.stickerproject.database.StickerContentProvider";

    public static final Uri FRIENDS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + "friends");

    @Override
    public boolean onCreate() {
        database = new DbAdapter(getContext());
        database.open();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = database.getAllFriends(selection,selectionArgs,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d("DBAdapter", "After query, cursor size = " + cursor.getCount());
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        database.addFriends(values);
        getContext().getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        database.updateFriendship(values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }
}
