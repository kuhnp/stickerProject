package com.example.rrhg5930.stickerproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrhg5930.stickerproject.util.StickerUtil;

import java.util.ArrayList;

/**
 * Created by pierre on 04/03/2015.
 */
public class FriendListAdapter extends BaseAdapter implements ListAdapter{

    private ArrayList<String> friendList = new ArrayList<String>();
    private Context context;


    public FriendListAdapter(ArrayList<String> list, Context context) {
        this.friendList = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friendlistview, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(friendList.get(position));

        //Handle buttons and add onClickListeners
        Button addBtn = (Button)view.findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Toast toast = Toast.makeText(context,"You have chosen: "+friendList.get(position),Toast.LENGTH_LONG);
                toast.show();
                notifyDataSetChanged();


            }
        });


        return view;
    }






}
