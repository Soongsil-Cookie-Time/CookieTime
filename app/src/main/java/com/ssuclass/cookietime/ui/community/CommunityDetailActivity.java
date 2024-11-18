package com.ssuclass.cookietime.ui.community;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssuclass.cookietime.databinding.ActivityCommunityDetailBinding;

import java.util.List;

public class CommunityDetailActivity extends AppCompatActivity {

    ActivityCommunityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setCommunityDetailRecyclerView();
    }

    private void setCommunityDetailRecyclerView() {
        RecyclerView communityDetailRecyclerView = binding.communityDetailRecyclerview;
        communityDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        communityDetailRecyclerView.setAdapter(new CommunityDetailAdapter(List.of("Sample 1", "Sample2", "Sample3")));
    }
}