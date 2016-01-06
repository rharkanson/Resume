package com.harkanson.android.resume.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harkanson.android.resume.databinding.FragmentEducationBinding;

public class EducationFragment extends Fragment {
    
    FragmentEducationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEducationBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
