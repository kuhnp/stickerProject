package com.example.rrhg5930.stickerproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.rrhg5930.stickerproject.adapter.FriendAdapter;
import com.example.rrhg5930.stickerproject.R;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.adapter.PendingFriendAdapter;
import com.example.rrhg5930.stickerproject.asynctask.AddFriendTask;
import com.example.rrhg5930.stickerproject.database.StickerContentProvider;
import com.example.rrhg5930.stickerproject.model.User;

import java.util.ArrayList;

/**
 * Created by pierre on 23/03/2015.
 */
public class FriendListFragment extends Fragment {



    private StickerApp application;
    private SharedPreferences sharedPreferences;
    private Context context;

    private RecyclerView friendListRecyclerView;
    private RecyclerView pendindFriendListRecyclerView;
    private RecyclerView.Adapter friendListAdapter;
    private RecyclerView.Adapter pendingFriendListAdapter;
    private ArrayList<String> friendList;
    private ArrayList<String> pendingFriendList;
    private RecyclerView.LayoutManager friendListLayoutManager;
    private RecyclerView.LayoutManager pendingFriendListLayoutManager;
    private Button showPendingFriendListButton;
    private EditText addFriendEditText;
    private Button addFriendButton;
    private RelativeLayout addFriendLayout;

    private User user;

    private boolean tmp = false;
    String whereClause;
    String[] whereArgs;

    public FriendListFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {





//        String a = null;
//        c.moveToNext();
//        if (c.getCount() >  1) {
//             a = c.getString(c.getColumnIndex("name"));
//        }

        application = (StickerApp) getActivity().getApplicationContext();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());

        friendList = new ArrayList<>();
        pendingFriendList = new ArrayList<>();

        user = User.getInstance();

        friendList = user.friendList;
        pendingFriendList = user.pendingFriendList;


        View rootView = inflater.inflate(R.layout.friendlist_layout, container, false);

        // Set the recyclerView with the list of friends
        friendListRecyclerView = (RecyclerView) rootView.findViewById(R.id.friendRV);
        friendListRecyclerView.setHasFixedSize(true);

        friendListLayoutManager = new LinearLayoutManager(getActivity());
        friendListRecyclerView.setLayoutManager(friendListLayoutManager);

        whereClause ="isfriend = ?";
        whereArgs = new String[] {
                "true"
        };
        Cursor c = context.getContentResolver().query(StickerContentProvider.FRIENDS_CONTENT_URI, null, whereClause, whereArgs, null);
        friendListAdapter = new FriendAdapter(friendList, false, application, sharedPreferences, context, c);
        friendListRecyclerView.setAdapter(friendListAdapter);



        // Set the second recyclerView, with the list of pending friends.
        pendindFriendListRecyclerView = (RecyclerView) rootView.findViewById(R.id.friendRV2);
        pendindFriendListRecyclerView.setHasFixedSize(true);

        pendingFriendListLayoutManager = new LinearLayoutManager(getActivity());
        pendindFriendListRecyclerView.setLayoutManager(pendingFriendListLayoutManager);


        whereClause ="isfriend = ?";
        whereArgs = new String[] {
                "false"
        };
        Cursor c1 = context.getContentResolver().query(StickerContentProvider.FRIENDS_CONTENT_URI, null, whereClause, whereArgs, null);

        pendingFriendListAdapter = new PendingFriendAdapter(pendingFriendList, true, application, sharedPreferences, context, c1);
        pendindFriendListRecyclerView.setAdapter(pendingFriendListAdapter);

        addFriendLayout = (RelativeLayout) rootView.findViewById(R.id.addFriendLayout);
        addFriendEditText = (EditText) rootView.findViewById(R.id.addFriendET);
        addFriendButton = (Button) rootView.findViewById(R.id.addFriendB);


        // Set the Button to show the pendingListFriend
        showPendingFriendListButton = (Button) rootView.findViewById(R.id.buttonFrien);
        showPendingFriendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tmp) {
                    if(pendindFriendListRecyclerView.getChildCount() !=  0) {
                        pendindFriendListRecyclerView.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
                        pendindFriendListRecyclerView.startAnimation(animation);
                        friendListRecyclerView.setVisibility(View.INVISIBLE);
                    }
                    addFriendLayout.setVisibility(View.VISIBLE);
                    Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
                    addFriendLayout.startAnimation(animation2);
                    tmp = true;
                } else {
                    pendindFriendListRecyclerView.setVisibility(View.INVISIBLE);
                    addFriendLayout.setVisibility(View.GONE);
                    friendListRecyclerView.setVisibility(View.VISIBLE);
                    tmp = false;
                }


            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendName = addFriendEditText.getText().toString();
                AddFriendTask addFriendTask = new AddFriendTask(application,sharedPreferences,application.getApplicationContext(),friendName);
                addFriendTask.execute();
            }
        });
        return rootView;

    }
}
