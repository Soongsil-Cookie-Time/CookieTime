package com.ssuclass.cookietime.presentation.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssuclass.cookietime.databinding.ActivityCommunityDetailBinding;
import com.ssuclass.cookietime.domain.CommunityDetailModel;

import java.util.ArrayList;
import java.util.List;

public class CommunityDetailActivity extends AppCompatActivity {

    private final List<CommunityDetailModel> dataList;
    private ActivityCommunityDetailBinding binding;
    private FirebaseFirestore db;
    private CommunityDetailAdapter adapter;

    private String movieId;

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
        setCommunityId();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchCommunityData(this.movieId);
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void setCommunityId() {
        Intent intent = getIntent();
        this.movieId = intent.getStringExtra("movieId"); // FIXME: communityId 하드코딩에서 추출
    }

    private void setCommunityDetailRecyclerView() {
        RecyclerView communityDetailRecyclerView = binding.communityDetailRecyclerview;
        communityDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommunityDetailAdapter(this.dataList);
        communityDetailRecyclerView.setAdapter(adapter);
    }

    private void setFloatingButtonOnClickListener() {
        ExtendedFloatingActionButton button = binding.communityWriteButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityDetailActivity.this, CommunityWriteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchCommunityData(String movieId) {
        db.collection("Movie") // FIXME: collectionPath 하드코딩에서 추출
                .document(movieId)
                .collection("Community")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("Title");
                                ArrayList<String> comment = (ArrayList<String>) document.get("Comment");
                                CommunityDetailModel model = new CommunityDetailModel(title, comment);
                                dataList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("FirebaseError", "Error fetching data: ", task.getException());
                        }
                    }
                });
    }
}