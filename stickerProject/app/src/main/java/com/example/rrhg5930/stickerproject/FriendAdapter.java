package com.example.rrhg5930.stickerproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rrhg5930.stickerproject.asynctask.AcceptFriendTask;

import java.util.ArrayList;

/**
 * Created by pierre on 18/03/2015.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>  {

    private StickerApp application;
    private SharedPreferences sharedPreferences;
    private Context context;
    private boolean isPending;
    private String friendSelected;
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
    public FriendAdapter(ArrayList<String> myDataset, boolean isPending, StickerApp application, SharedPreferences sharedPreferences, Context context) {

        mDataset = myDataset;
        this.isPending = isPending;
        this.application = application;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }


    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlistview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int i) {
        holder.mTextView.setText(mDataset.get(i));
        friendSelected = mDataset.get(i);
        if(isPending)
            holder.mButton.setText("Accepts");
        else
            holder.mButton.setText("Post");

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPending){      // accept friend request
                    AcceptFriendTask acceptFriendTask = new AcceptFriendTask(application, sharedPreferences, context, friendSelected);
                    acceptFriendTask.execute();
                }
                else{               // post a sticker

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
