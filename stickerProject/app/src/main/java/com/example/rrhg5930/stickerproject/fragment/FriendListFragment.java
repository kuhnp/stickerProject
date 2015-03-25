package com.example.rrhg5930.stickerproject.fragment;

import android.os.Bundle;
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

import com.example.rrhg5930.stickerproject.FriendAdapter;
import com.example.rrhg5930.stickerproject.R;
import com.example.rrhg5930.stickerproject.model.User;

import java.util.ArrayList;

/**
 * Created by pierre on 23/03/2015.
 */
public class FriendListFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


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

        friendListAdapter = new FriendAdapter(friendList);
        friendListRecyclerView.setAdapter(friendListAdapter);



        // Set the second recyclerView, with the list of pending friends.
        pendindFriendListRecyclerView = (RecyclerView) rootView.findViewById(R.id.friendRV2);
        pendindFriendListRecyclerView.setHasFixedSize(true);

        pendingFriendListLayoutManager = new LinearLayoutManager(getActivity());
        pendindFriendListRecyclerView.setLayoutManager(pendingFriendListLayoutManager);

        pendingFriendListAdapter = new FriendAdapter(pendingFriendList);
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
                    pendindFriendListRecyclerView.setVisibility(View.VISIBLE);
                    addFriendLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
                    pendindFriendListRecyclerView.startAnimation(animation);
                    friendListRecyclerView.setVisibility(View.INVISIBLE);

                    tmp = true;
                } else {
                    pendindFriendListRecyclerView.setVisibility(View.GONE);
                    addFriendLayout.setVisibility(View.GONE);
                    friendListRecyclerView.setVisibility(View.VISIBLE);
                    tmp = false;
                }


            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;

    }
}
