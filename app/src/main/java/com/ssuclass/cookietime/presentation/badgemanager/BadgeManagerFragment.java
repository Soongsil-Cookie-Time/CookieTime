package com.ssuclass.cookietime.presentation.badgemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ssuclass.cookietime.databinding.FragmentBadgeManagerBinding;

public class BadgeManagerFragment extends Fragment {

    private FragmentBadgeManagerBinding binding;

    public BadgeManagerFragment() {
        // Required empty public constructor
    }


    public static BadgeManagerFragment newInstance(String param1, String param2) {
        BadgeManagerFragment fragment = new BadgeManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBadgeManagerBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}