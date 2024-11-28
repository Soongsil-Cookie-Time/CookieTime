package com.ssuclass.cookietime.presentation.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ssuclass.cookietime.databinding.ActivityCommunityDetailBinding;
import com.ssuclass.cookietime.domain.CommunityDetailModel;

import java.util.ArrayList;
import java.util.List;

public class CommunityDetailActivity extends AppCompatActivity {

    private final List<CommunityDetailModel> dataList;
    private ActivityCommunityDetailBinding binding;
    private FirebaseFirestore db;
    private CommunityDetailAdapter adapter;

    public CommunityDetailActivity() {
        dataList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFirebaseInstance();
        setCommunityDetailRecyclerView();
        setFloatingButtonOnClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchCommunityData();
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void setCommunityDetailRecyclerView() {
        RecyclerView communityDetailRecyclerView = binding.communityDetailRecyclerview;
        communityDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommunityDetailAdapter(this.dataList);
        communityDetailRecyclerView.setAdapter(adapter);
    }

    private void setFloatingButtonOnClickListener() {
        ExtendedFloatingActionButton button = binding.communityWriteButton;
        button.setOnClickListener(view -> {
            Intent intent = new Intent(CommunityDetailActivity.this, CommunityWriteActivity.class);
            startActivity(intent);
        });
    }

    private void fetchCommunityData() {
        Intent intent = getIntent();
        String movieId = intent.getStringExtra("movieId");

        db.collection("Communities")
                .document(movieId)
                .collection("Posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dataList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            CommunityDetailModel model = new CommunityDetailModel();
                            model.setTitle(title);
                            dataList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirebaseError", "Error fetching data: ", task.getException());
                    }
                });
    }
}