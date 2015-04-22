package com.example.rrhg5930.stickerproject.fragment;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.rrhg5930.stickerproject.R;
import com.example.rrhg5930.stickerproject.StickerApp;
import com.example.rrhg5930.stickerproject.asynctask.LogoutTask;

/**
 * Created by pierre on 19/03/2015.
 */
public class MainStickerFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private Context context;
    private Button logoutB;

    public MainStickerFragment(Context context){
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_sticker_layout, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        TextView textView = (TextView) rootView.findViewById(R.id.usernameTV);
        textView.setText(sharedPreferences.getString("username",""));
        logoutB = (Button) rootView.findViewById(R.id.logout_button);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.mainStickerIV);
        if(!sharedPreferences.getString("imagePath", "").isEmpty())
            imageView.setImageURI(Uri.parse(sharedPreferences.getString("imagePath","")));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.abc_slide_in_bottom);
//                imageView.startAnimation(animation);
            }
        });

        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure that you want to logout?")
                        .setTitle("Logout")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //logout
                                LogoutTask logoutTask = new LogoutTask((StickerApp)getActivity().getApplicationContext(), context, PreferenceManager.getDefaultSharedPreferences(context));
                                logoutTask.execute();
                            }

                        } )
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // cancel
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return rootView;

    }
}
