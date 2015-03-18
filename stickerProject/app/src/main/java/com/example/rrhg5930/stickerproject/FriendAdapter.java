package com.example.rrhg5930.stickerproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pierre on 18/03/2015.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>  {

    private ArrayList<String> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public Button mButton;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.list_item_string);
            mButton = (Button) v.findViewById(R.id.add_btn);
        }
    }
    public FriendAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlistview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int i) {
        holder.mTextView.setText(mDataset.get(i));


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
