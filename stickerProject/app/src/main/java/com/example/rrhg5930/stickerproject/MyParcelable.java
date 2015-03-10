package com.example.rrhg5930.stickerproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pierre on 04/03/2015.
 */
public class MyParcelable implements Parcelable {

    private int mData;

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mData);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<MyParcelable> CREATOR = new Parcelable.Creator<MyParcelable>() {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private MyParcelable(Parcel in) {
        mData = in.readInt();
    }
}
