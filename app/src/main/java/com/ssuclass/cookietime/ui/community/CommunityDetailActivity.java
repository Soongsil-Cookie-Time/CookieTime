package com.ssuclass.cookietime.ui.community;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ssuclass.cookietime.databinding.ActivityCommunityDetailBinding;

public class CommunityDetailActivity extends AppCompatActivity {

    ActivityCommunityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}