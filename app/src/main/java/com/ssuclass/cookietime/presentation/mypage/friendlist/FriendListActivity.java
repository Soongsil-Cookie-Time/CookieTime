package com.ssuclass.cookietime.presentation.mypage.friendlist;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ssuclass.cookietime.databinding.ActivityFriendListBinding;

public class FriendListActivity extends AppCompatActivity {

    private ActivityFriendListBinding binding;
    private FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFriendListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        implementRecyclerView();
    }

    private void implementRecyclerView() {
        binding.friendListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendListAdapter();
        binding.friendListRecyclerview.setAdapter(adapter);
    }
}