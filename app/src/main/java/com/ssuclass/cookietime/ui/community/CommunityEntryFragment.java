package com.ssuclass.cookietime.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.FragmentCommunityEntryBinding;
import com.ssuclass.cookietime.util.SpaceingItemDecoration;

import java.util.List;

public class CommunityEntryFragment extends Fragment {

    // Fields
    private FragmentCommunityEntryBinding binding;

    // Static Methods
    public static CommunityEntryFragment newInstance() {

        return new CommunityEntryFragment();
    }

    // Life Cycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCommunityEntryBinding.inflate(getLayoutInflater(), container, false);
        setCommunityRecyclerView();
        return binding.getRoot();
    }

    // Class Internal Field
    private void setCommunityRecyclerView() {
        RecyclerView communityRecyclerView = binding.communityRecyclerview;
        communityRecyclerView.addItemDecoration(new SpaceingItemDecoration(14));
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        communityRecyclerView.setAdapter(new CommunityEntryAdapter(
                List.of("[Movie1]",
                        "[Movie2]",
                        "[Movie3]",
                        "[Movie4]",
                        "[Movie5]",
                        "[Movie6]")));
    }
}