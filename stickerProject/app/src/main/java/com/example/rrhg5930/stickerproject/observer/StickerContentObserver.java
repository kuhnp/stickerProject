package com.example.rrhg5930.stickerproject.observer;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * Created by pierre on 30/03/2015.
 */
public class StickerContentObserver extends ContentObserver {

    int type;
    Handler mHandler;
    public StickerContentObserver(Handler handler, int type) {
        super(handler);
        this.type = type;
        this.mHandler = handler;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        mHandler.sendEmptyMessage(type);
    }
}
