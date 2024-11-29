package com.ssuclass.cookietime.presentation.community.posts;

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
import com.ssuclass.cookietime.presentation.community.write.PostsWriteActivity;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private final List<PostsModel> dataList;
    private ActivityCommunityDetailBinding binding;
    private FirebaseFirestore db;
    private PostsAdapter adapter;
    private String movieId;
    private String postId;

    public PostsActivity() {
        dataList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getMovieId();
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
        adapter = new PostsAdapter(this.dataList, this.movieId);
        communityDetailRecyclerView.setAdapter(adapter);
    }

    private void setFloatingButtonOnClickListener() {
        ExtendedFloatingActionButton button = binding.communityWriteButton;
        button.setOnClickListener(view -> {
            Intent intent = new Intent(PostsActivity.this, PostsWriteActivity.class);
            startActivity(intent);
        });
    }

    private void getMovieId() {
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");
    }

    private void fetchCommunityData() {
        db.collection("Communities")
                .document(movieId)
                .collection("Posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dataList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String postId = document.getId();
                            String title = document.getString("title");

                            Log.d("Movie", postId);
                            Log.d("Movie", title);

                            PostsModel model = new PostsModel();
                            model.setPostId(postId);
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