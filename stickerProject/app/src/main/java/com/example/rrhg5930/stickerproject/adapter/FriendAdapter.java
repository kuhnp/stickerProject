package com.example.rrhg5930.stickerproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rrhg5930.stickerproject.MainActivity;
import com.example.rrhg5930.stickerproject.R;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.StickerConfig;
import com.example.rrhg5930.stickerproject.asynctask.PostStickerTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by pierre on 18/03/2015.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>  {

    CursorAdapter cursorAdapter;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private StickerApp application;
    private SharedPreferences sharedPreferences;
    private Context context;
    HashMap<String,Boolean> map;




    public FriendAdapter(final boolean isPending, StickerApp application, SharedPreferences sharedPreferences, Context context, Cursor c ) {

        this.map = new HashMap<>();
        this.application = application;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.cursorAdapter = new CursorAdapter(this.context, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return null;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                final ViewHolder holder = (ViewHolder) view.getTag(R.string.viewHolder);
                final String name = cursor.getString(cursor.getColumnIndex("name"));

                holder.mButtonChoosePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.isPosting = true;
                        chosePicture(v.getContext());
                        map.put(name,true);
                    }
                });

                holder.mButtonPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PostStickerTask postStickerTask = new PostStickerTask(name, MainActivity.mImagePath,
                                (StickerApp) v.getContext().getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(v.getContext()), v.getContext(), 0);
                        postStickerTask.execute();
                        map.put(name,false);
                    }
                });

                holder.mButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        map.put(name,false);
                        notifyDataSetChanged();
                    }
                });

                holder.mTextView.setText(Html.fromHtml(name));
                updateHolderLayout(holder, name);
            }
        };
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlistview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int i) {
        Cursor cursor = cursorAdapter.getCursor();
        cursor.moveToPosition(i);
        holder.setPosition(i);
        holder.itemView.setTag(R.string.viewHolder,holder);
        cursorAdapter.bindView(holder.itemView, context, cursor);
    }


    void updateHolderLayout(FriendAdapter.ViewHolder holder, String name){

        if(map.get(name) != null){
            if(map.get(name))
                extendCardViewLayout(holder);
            else
                resetCardViewLayout(holder, name);
        }
        else
            resetCardViewLayout(holder, name);

    }


    public void resetCardViewLayout(FriendAdapter.ViewHolder holder, String name){

        ViewGroup.LayoutParams layoutParams = holder.mCardView.getLayoutParams();
        layoutParams.height = StickerApp.getNormalCardViewSize();
        holder.mCardView.setLayoutParams(layoutParams);
        holder.mButtonPost.setVisibility(View.GONE);
        holder.mButtonCancel.setVisibility(View.GONE);
        imageLoader.displayImage(StickerConfig.PARAM_URL + "/sticker/" + name, holder.mImageView);
    }

    public void extendCardViewLayout(FriendAdapter.ViewHolder holder){
        ViewGroup.LayoutParams layoutParams = holder.mCardView.getLayoutParams();
        layoutParams.height = StickerApp.getExpandedCardViewSize();
        holder.mCardView.setLayoutParams(layoutParams);
        holder.mButtonPost.setVisibility(View.VISIBLE);
        holder.mButtonCancel.setVisibility(View.VISIBLE);
        holder.mImageView.setImageURI(MainActivity.fileUri);
    }


    @Override
    public int getItemCount() {

        return cursorAdapter.getCount();
    }

    public void chosePicture(Context context){

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
        galleryIntent.setType("image/*");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            fileUri = StickerUtil.getOutputMediaFileUri(StickerUtil.MEDIA_TYPE_IMAGE);
//            //application.setCameraPath(fileUri.getPath());
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT,galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE,"Choose a picture");
        Intent[] intentArray = {cameraIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentArray);
        //((MainActivity)context).setPosition(position);
        ((Activity)context).startActivityForResult(chooser, 1);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public boolean isPosting = false;
        public CardView mCardView;
        public TextView mTextView;
        public Button mButtonChoosePicture;
        public Button mButtonPost;
        public Button mButtonCancel;
        public ImageView mImageView;
        public View v1;
        int position;
        String nameTmp;
        //private ImageLoader imageLoader = ImageLoader.getInstance();



        public ViewHolder(View v) {

            super(v);
            v1 = v.findViewById(R.id.card_view);
            mImageView = (ImageView)v1.findViewById(R.id.friendListIV);
            mTextView = (TextView)v1.findViewById(R.id.friendStickerNameTV);
            mButtonPost = (Button)v1.findViewById(R.id.sendB);
            mButtonCancel = (Button)v1.findViewById(R.id.cancelB);
            mButtonChoosePicture = (Button) v1.findViewById(R.id.newPostB);
            mCardView = (CardView) v1.findViewById(R.id.card_view);

        }

        public void setPosition(int pos){
            this.position = pos;
        }



    }

}
