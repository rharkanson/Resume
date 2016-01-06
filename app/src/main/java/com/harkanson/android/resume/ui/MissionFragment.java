package com.harkanson.android.resume.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harkanson.android.resume.R;
import com.harkanson.android.resume.databinding.FragmentMissionBinding;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

public class MissionFragment extends Fragment {
    FragmentMissionBinding binding;
    OnContactsClickedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMissionBinding.inflate(inflater, container, false);
        listener = (OnContactsClickedListener) getActivity();

        Picasso.with(getContext())
                .load(R.drawable.russell)
                .resize(128, 128)
                .transform(new GrayscaleTransformation())
                .transform(new CropCircleTransformation())
                .into(binding.ivPicture);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactsClicked();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        binding.contact.setOnClickListener(null);
    }

    public interface OnContactsClickedListener {
        void onContactsClicked();
    }
}
