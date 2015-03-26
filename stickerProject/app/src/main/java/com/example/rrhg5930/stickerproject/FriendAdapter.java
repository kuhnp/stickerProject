package com.example.rrhg5930.stickerproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rrhg5930.stickerproject.asynctask.AcceptFriendTask;
import com.example.rrhg5930.stickerproject.asynctask.PostStickerTask;
import com.example.rrhg5930.stickerproject.util.StickerUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by pierre on 18/03/2015.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>  {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private StickerApp application;
    private SharedPreferences sharedPreferences;
    private Context context;
    private boolean isPending;
    private String friendSelected;
    private ArrayList<String> mDataset;

    private Uri fileUri;
    public static String mImagePath;
    private boolean isPicturechosen = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public Button mButton;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.list_item_string);
            mButton = (Button) v.findViewById(R.id.add_btn);
            mImageView = (ImageView) v.findViewById(R.id.friendListIV);
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
    public void onBindViewHolder(final FriendAdapter.ViewHolder holder, int i) {
        holder.mTextView.setText(mDataset.get(i));
        friendSelected = mDataset.get(i);

        if(isPending)
            holder.mButton.setText("Accepts");
        else {
            holder.mButton.setText("Post");
            imageLoader.displayImage(StickerConfig.PARAM_URL+"/sticker",holder.mImageView);
        }
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPending){      // accept friend request
                    AcceptFriendTask acceptFriendTask = new AcceptFriendTask(application, sharedPreferences, context, friendSelected);
                    acceptFriendTask.execute();
                }
                else{               // post a sticker
                    if(!isPicturechosen) {
                        chosePicture();
                        holder.mButton.setText("Post now");
                        isPicturechosen = true;
                    }
                    else{

                        PostStickerTask postStickerTask = new PostStickerTask(friendSelected, mImagePath, application, sharedPreferences, context);
                        postStickerTask.execute();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void chosePicture(){

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
        galleryIntent.setType("image/*");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = StickerUtil.getOutputMediaFileUri(StickerUtil.MEDIA_TYPE_IMAGE);
        //application.setCameraPath(fileUri.getPath());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT,galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE,"Choose a picture");
        Intent[] intentArray = {cameraIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentArray);

        ((Activity)context).startActivityForResult(chooser, 1);

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent result)
    {
        if((requestCode == 1) && (resultCode == ((Activity)context).RESULT_OK))
        {
            Uri imageUri = result.getData();
            mImagePath = StickerUtil.getRealPathFromURI(imageUri,context);
        }
    }
}
