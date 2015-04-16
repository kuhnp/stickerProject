package com.example.rrhg5930.stickerproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rrhg5930.stickerproject.R;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.asynctask.AcceptFriendTask;
import com.example.rrhg5930.stickerproject.util.StickerUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by pierre on 30/03/2015.
 */
public class PendingFriendAdapter extends RecyclerView.Adapter<PendingFriendAdapter.PendingViewHolder>  {

    CursorAdapter cursorAdapter;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private StickerApp application;
    private SharedPreferences sharedPreferences;
    private Context context;
    private boolean isPending;
    private String friendSelected;
    private ArrayList<String> mDataset;

    private Uri fileUri;
    public static String mImagePath;
    private String name;
    private boolean isPicturechosen = false;



    public static class PendingViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public Button mButtonAccept;
        public Button mButtonDecline;
        public TextView waitingConfirmationTV;
        public ImageView mImageView;
        public View v1;
        public int position;



        public PendingViewHolder(View v) {
            super(v);
            v1 = itemView.findViewById(R.id.pending_card_view);
            mButtonAccept = (Button) v1.findViewById(R.id.accept_btn);
            mButtonDecline = (Button) v1.findViewById(R.id.decline_btn);
            mTextView = (TextView) v1.findViewById(R.id.pending_friend_name);
            waitingConfirmationTV = (TextView) v1.findViewById(R.id.wainting_conFirmation_tv);

            mButtonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AcceptFriendTask acceptFriendTask = new AcceptFriendTask((StickerApp)v.getContext().getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(v.getContext()),v.getContext(), mTextView.getText().toString(), position);
                    acceptFriendTask.execute();
                }
            });
//            mTextView = (TextView) v.findViewById(R.id.list_item_string);
//            mButtonAccept = (Button) v.findViewById(R.id.add_btn);
//            mImageView = (ImageView) v.findViewById(R.id.friendListIV);
        }

        public void setPosition(int pos){
            this.position = pos;
        }



    }
        public PendingFriendAdapter(ArrayList<String> myDataset, final boolean isPending, final StickerApp application, final SharedPreferences sharedPreferences, Context context, Cursor c) {

        //mDataset = myDataset;
        this.isPending = isPending;
        this.application = application;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.cursorAdapter = new CursorAdapter(this.context, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.pendingfriendlistview, parent, false);
            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {

                PendingViewHolder holder = (PendingViewHolder) view.getTag();
                if (holder == null)
                    holder = new PendingViewHolder(view);

                holder.setPosition(cursor.getPosition());
                holder.mTextView = (TextView) view.findViewById(R.id.pending_friend_name);
                holder.mButtonAccept = (Button) view.findViewById(R.id.accept_btn);
                holder.mButtonDecline = (Button) view.findViewById(R.id.decline_btn);
                holder.waitingConfirmationTV = (TextView) view.findViewById(R.id.wainting_conFirmation_tv);
                name = cursor.getString(cursor.getColumnIndex("name"));
                holder.mTextView.setText(Html.fromHtml(name));
                if(cursor.getString(cursor.getColumnIndex("fromuser")).equalsIgnoreCase("true")){
                    holder.mButtonAccept.setVisibility(View.GONE);
                    holder.mButtonDecline.setVisibility(View.GONE);
                    holder.waitingConfirmationTV.setVisibility(View.VISIBLE);
                }


                //imageLoader.displayImage(StickerConfig.PARAM_URL + "/sticker/" + name, holder.mImageView);

            }

        };
    }


    @Override
    public PendingFriendAdapter.PendingViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pendingfriendlistview, parent, false);
        return new PendingViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final PendingFriendAdapter.PendingViewHolder holder, int i) {

        Cursor cursor = cursorAdapter.getCursor();
        cursor.moveToPosition(i);
        cursorAdapter.bindView(holder.itemView, context, cursor);









//        holder.mTextView.setText(mDataset.get(i));
//        friendSelected = mDataset.get(i);
//
//        if(isPending)
//            holder.mButtonAccept.setText("Accepts");
//        else {
//            holder.mButtonAccept.setText("Post");
//            imageLoader.displayImage(StickerConfig.PARAM_URL+"/sticker",holder.mImageView);
//        }
//        holder.mButtonAccept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isPending){      // accept friend request
//                    AcceptFriendTask acceptFriendTask = new AcceptFriendTask(application, sharedPreferences, context, friendSelected);
//                    acceptFriendTask.execute();
//                }
//                else{               // post a sticker
//                    if(!isPicturechosen) {
//                        chosePicture();
//                        holder.mButtonAccept.setText("Post now");
//                        isPicturechosen = true;
//                    }
//                    else{
//
//                        PostStickerTask postStickerTask = new PostStickerTask(friendSelected, mImagePath, application, sharedPreferences, context);
//                        postStickerTask.execute();
//                    }
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {

        int b = cursorAdapter.getCount();
        return cursorAdapter.getCount();
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
