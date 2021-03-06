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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rrhg5930.stickerproject.StickerConfig;
import com.example.rrhg5930.stickerproject.adapter.FriendAdapter;
import com.example.rrhg5930.stickerproject.R;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.adapter.PendingFriendAdapter;
import com.example.rrhg5930.stickerproject.asynctask.AddFriendTask;
import com.example.rrhg5930.stickerproject.database.StickerContentProvider;
import com.example.rrhg5930.stickerproject.model.User;
import com.example.rrhg5930.stickerproject.observer.StickerContentObserver;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by pierre on 23/03/2015.
 */
public class FriendListFragment extends Fragment {



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
    private FloatingActionsMenu showPendingFriendListButton;
    private EditText addFriendEditText;
    private Button addFriendButton;
    private RelativeLayout addFriendLayout;

    private StickerContentObserver contentObserver;

    private User user;
    Cursor c;
    Cursor c1;

    public FriendListFragment(Context context){
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        application = (StickerApp) getActivity().getApplicationContext();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        View rootView = inflater.inflate(R.layout.friendlist_layout, container, false);
        showPendingFriendListButton = (FloatingActionsMenu) rootView.findViewById(R.id.buttonFrien);

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




        // Set the recyclerView with the list of friends
        friendListRecyclerView = (RecyclerView) rootView.findViewById(R.id.friendRV);

        friendListLayoutManager = new LinearLayoutManager(getActivity());

        friendListRecyclerView.setLayoutManager(friendListLayoutManager);

        // Query db for friends
        c = findFriendinDb();
        int length = c.getCount();
        friendListAdapter = new FriendAdapter(false, application, sharedPreferences, context, c);
        friendListRecyclerView.setAdapter(friendListAdapter);

        friendListRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    showPendingFriendListButton.setVisibility(View.VISIBLE);
                }
                else
                    showPendingFriendListButton.setVisibility(View.INVISIBLE);
            }
        });




        // Set the second recyclerView, with the list of pending friends.
        pendindFriendListRecyclerView = (RecyclerView) rootView.findViewById(R.id.friendRV2);

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



        showPendingFriendListButton.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {

                if (pendindFriendListRecyclerView.getChildCount() != 0) {
                    pendindFriendListRecyclerView.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
                    pendindFriendListRecyclerView.startAnimation(animation);
                    friendListRecyclerView.setVisibility(View.INVISIBLE);
                }
                addFriendLayout.setVisibility(View.VISIBLE);
                Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
                addFriendLayout.startAnimation(animation2);
            }

            @Override
            public void onMenuCollapsed() {
                pendindFriendListRecyclerView.setVisibility(View.INVISIBLE);
                addFriendLayout.setVisibility(View.GONE);
                friendListRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendName = addFriendEditText.getText().toString();
                if(!friendName.isEmpty()) {
                    AddFriendTask addFriendTask = new AddFriendTask(application, sharedPreferences, application.getApplicationContext(), friendName);
                    addFriendTask.execute();
                }
                else{
                    Toast.makeText(context,"You must enter a friend name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;

    }


    public void updateFragmentBeforePost(int position, Uri uri){
        this.friendListRecyclerView.getAdapter().notifyDataSetChanged();

//        View v = this.friendListRecyclerView.getChildAt(position);
//
//        int size = this.friendListRecyclerView.getChildCount();
//        int size2 = this.friendListRecyclerView.getAdapter().getItemCount();
//
//
//        CardView cardView = (CardView) v.findViewById(R.id.card_view);
//        Resources r = context.getResources();
//        ImageView iv = (ImageView) v.findViewById(R.id.friendListIV);
//        Button btn = (Button) v.findViewById(R.id.sendB);
//        Button btn2 = (Button) v.findViewById(R.id.cancelB);
//        iv.setImageURI(uri);
//
//        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
//        int newCardSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,508,r.getDisplayMetrics());
//        layoutParams.height = newCardSize;
//        cardView.setLayoutParams(layoutParams);
//
//        btn.setVisibility(View.VISIBLE);
//        btn2.setVisibility(View.VISIBLE);
    }

    public void updateFragmentAfterPost(int position) {
        this.friendListRecyclerView.getAdapter().notifyDataSetChanged();
//        View v = this.friendListRecyclerView.getChildAt(position);
//        ImageView iv = (ImageView) v.findViewById(R.id.friendListIV);
//        TextView tv = (TextView) v.findViewById(R.id.friendStickerNameTV);
//        Button btn = (Button) v.findViewById(R.id.sendB);
//        Button btn2 = (Button) v.findViewById(R.id.cancelB);
//        String name = tv.getText().toString();
//        CardView cardView = (CardView) v.findViewById(R.id.card_view);
//        Resources r = context.getResources();
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.cancelDisplayTask(iv);
//        imageLoader.displayImage(StickerConfig.PARAM_URL + "/sticker/" + name, iv);
//
//        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
//        int newCardSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,420,r.getDisplayMetrics());
//        layoutParams.height = newCardSize;
//        cardView.setLayoutParams(layoutParams);
//
//        btn.setVisibility(View.GONE);
//        btn2.setVisibility(View.GONE);
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
