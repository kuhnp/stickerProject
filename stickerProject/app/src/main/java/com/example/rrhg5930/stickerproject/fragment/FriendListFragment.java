package com.example.rrhg5930.stickerproject.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.rrhg5930.stickerproject.MainActivity;
import com.example.rrhg5930.stickerproject.adapter.FriendAdapter;
import com.example.rrhg5930.stickerproject.R;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.adapter.PendingFriendAdapter;
import com.example.rrhg5930.stickerproject.asynctask.AddFriendTask;
import com.example.rrhg5930.stickerproject.database.StickerContentProvider;
import com.example.rrhg5930.stickerproject.model.User;
import com.example.rrhg5930.stickerproject.observer.StickerContentObserver;

import java.util.ArrayList;

/**
 * Created by pierre on 23/03/2015.
 */
public class FriendListFragment extends Fragment {

    OnHeadlineSelectedListener mCallback;

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }




    private StickerApp application;
    private SharedPreferences sharedPreferences;
    private Context context;

    public RecyclerView friendListRecyclerView;
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

    private StickerContentObserver contentObserver;

    private User user;
    Cursor c;
    Cursor c1;

    private boolean tmp = false;

    public FriendListFragment(Context context){
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        application = (StickerApp) getActivity().getApplicationContext();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());

        friendList = new ArrayList<>();
        pendingFriendList = new ArrayList<>();

        user = User.getInstance();

        friendList = user.friendList;
        pendingFriendList = user.pendingFriendList;

        Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // When data change, reset both recycler views with new data
                c = findFriendinDb();
                friendListAdapter = new FriendAdapter(false, application, sharedPreferences, context, c);
                friendListRecyclerView.setAdapter(friendListAdapter);

                c1 = findPendingFriendinDb();
                pendingFriendListAdapter = new PendingFriendAdapter(pendingFriendList, true, application, sharedPreferences, context, c1);
                pendindFriendListRecyclerView.setAdapter(pendingFriendListAdapter);
            }
        };
        contentObserver = new StickerContentObserver(mHandler, 0);
        this.getActivity().getContentResolver().registerContentObserver(StickerContentProvider.FRIENDS_CONTENT_URI, true, contentObserver);


        View rootView = inflater.inflate(R.layout.friendlist_layout, container, false);

        // Set the recyclerView with the list of friends
        friendListRecyclerView = (RecyclerView) rootView.findViewById(R.id.friendRV);
        friendListRecyclerView.setHasFixedSize(true);

        friendListLayoutManager = new LinearLayoutManager(getActivity());
        friendListRecyclerView.setLayoutManager(friendListLayoutManager);

        // Query db for friends
        c = findFriendinDb();
        friendListAdapter = new FriendAdapter(false, application, sharedPreferences, context, c);
        friendListRecyclerView.setAdapter(friendListAdapter);




        // Set the second recyclerView, with the list of pending friends.
        pendindFriendListRecyclerView = (RecyclerView) rootView.findViewById(R.id.friendRV2);
        pendindFriendListRecyclerView.setHasFixedSize(true);

        pendingFriendListLayoutManager = new LinearLayoutManager(getActivity());
        pendindFriendListRecyclerView.setLayoutManager(pendingFriendListLayoutManager);

        // Query db for pending friends
        c1 = findPendingFriendinDb();
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

    public void updateFragment(int position, Uri uri){
        View v = this.friendListRecyclerView.getChildAt(position);
        ImageView iv = (ImageView) v.findViewById(R.id.friendListIV);
        iv.setImageURI(uri);
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,r.getDisplayMetrics());
        int px2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,r.getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(px, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0,0,px2,0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        iv.setLayoutParams(params);
        TranslateAnimation ta = new TranslateAnimation(px2,0,0,0);
        ta.setDuration(500);
        iv.startAnimation(ta);
    }

    Cursor findFriendinDb(){
        String whereClause ="isfriend = ?";
        String[] whereArgs = new String[] {
                "true"
        };
        Cursor cursor = context.getContentResolver().query(StickerContentProvider.FRIENDS_CONTENT_URI, null, whereClause, whereArgs, null);
        return cursor;
    }

    Cursor findPendingFriendinDb(){
        String whereClause ="isfriend = ?";
        String[] whereArgs = new String[] {
                "false"
        };
        Cursor cursor = context.getContentResolver().query(StickerContentProvider.FRIENDS_CONTENT_URI, null, whereClause, whereArgs, null);
        return cursor;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
