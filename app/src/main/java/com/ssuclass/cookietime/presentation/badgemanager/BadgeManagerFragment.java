package com.ssuclass.cookietime.presentation.badgemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.FragmentBadgeManagerBinding;

public class BadgeManagerFragment extends Fragment {

    private FragmentBadgeManagerBinding binding;

    public BadgeManagerFragment() {
        // Required empty public constructor
    }

    public static BadgeManagerFragment newInstance() {
        return new BadgeManagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBadgeManagerBinding.inflate(getLayoutInflater());
        setRecyclerView();
        return binding.getRoot();
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = binding.monthlyBadgesRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new FragmentBadgeManagerAdapter(this.getContext()));
    }
}