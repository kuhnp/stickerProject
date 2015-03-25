package com.example.rrhg5930.stickerproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.rrhg5930.stickerproject.R;

/**
 * Created by pierre on 19/03/2015.
 */
public class MainStickerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_sticker_layout, container, false);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.mainStickerIV);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.abc_slide_in_bottom);
                imageView.startAnimation(animation);
            }
        });

        return rootView;

    }
}
