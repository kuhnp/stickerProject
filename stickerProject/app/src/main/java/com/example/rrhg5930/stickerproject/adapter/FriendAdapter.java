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
import android.util.Log;
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

/**
 * Created by pierre on 18/03/2015.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>  {

    CursorAdapter cursorAdapter;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private StickerApp application;
    private SharedPreferences sharedPreferences;
    private Context context;
    private boolean isPending;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public Button mButtonChoosePicture;
        public Button mButtonPost;
        public Button mButtonCancel;
        public ImageView mImageView;
        public View v1;
        int position;
        private ImageLoader imageLoader = ImageLoader.getInstance();


        public ViewHolder(View v) {
            super(v);
            v1 = itemView.findViewById(R.id.card_view);
            mImageView = (ImageView)v1.findViewById(R.id.friendListIV);
            mTextView = (TextView)v1.findViewById(R.id.friendStickerNameTV);
            mButtonPost = (Button)v1.findViewById(R.id.sendB);
            mButtonCancel = (Button)v1.findViewById(R.id.cancelB);
            mButtonChoosePicture = (Button) v1.findViewById(R.id.newPostB);
            mButtonChoosePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("FriendAdapter", "selected:"+mTextView.getText());
                    chosePicture(v.getContext());
                }
            });
            mButtonPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostStickerTask postStickerTask = new PostStickerTask(mTextView.getText().toString(), MainActivity.mImagePath,
                            (StickerApp)v.getContext().getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(v.getContext()), v.getContext(), position);
                    postStickerTask.execute();
                }
            });

            mButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources r = v.getContext().getResources();
                    CardView cardView = (CardView) v1.findViewById(R.id.card_view);
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    int newCardSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 420, r.getDisplayMetrics());
                    layoutParams.height = newCardSize;
                    cardView.setLayoutParams(layoutParams);
                    mButtonPost.setVisibility(View.GONE);
                    mButtonCancel.setVisibility(View.GONE);
                    imageLoader.displayImage(StickerConfig.PARAM_URL + "/sticker/" + mTextView.getText().toString(), mImageView);
                }
            });
        }

        public void setPosition(int pos){
            this.position = pos;
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
            ((MainActivity)context).setPosition(position);
            ((Activity)context).startActivityForResult(chooser, 1);

        }
    }
    public FriendAdapter(final boolean isPending, StickerApp application, SharedPreferences sharedPreferences, Context context, Cursor c ) {

        this.isPending = isPending;
        this.application = application;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.cursorAdapter = new CursorAdapter(this.context, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.friendlistview, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                ViewHolder holder = (ViewHolder) view.getTag();
                if (holder == null)
                    holder = new ViewHolder(view);

                holder.setPosition(cursor.getPosition());
                holder.mTextView = (TextView) view.findViewById(R.id.friendStickerNameTV);
                holder.mImageView = (ImageView) view.findViewById(R.id.friendListIV);
                holder.mButtonPost = (Button) view.findViewById(R.id.sendB);
                holder.mButtonCancel = (Button) view.findViewById(R.id.cancelB);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                holder.mTextView.setText(Html.fromHtml(name));
                holder.mButtonPost.setVisibility(View.GONE);
                holder.mButtonCancel.setVisibility(View.GONE);

                imageLoader.displayImage(StickerConfig.PARAM_URL + "/sticker/" + name, holder.mImageView);
            }
        };
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlistview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FriendAdapter.ViewHolder holder, int i) {
        Cursor cursor = cursorAdapter.getCursor();
        cursor.moveToPosition(i);
        cursorAdapter.bindView(holder.itemView, context, cursor);

    }


    @Override
    public int getItemCount() {

        return cursorAdapter.getCount();
    }

}
