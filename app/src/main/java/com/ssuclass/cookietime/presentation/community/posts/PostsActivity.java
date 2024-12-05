package com.ssuclass.cookietime.presentation.community.posts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssuclass.cookietime.databinding.ActivityCommunityDetailBinding;
import com.ssuclass.cookietime.presentation.community.write.PostsWriteActivity;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

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
            intent.putExtra(FirebaseConstants.MOVIEID_DOCUMENT, this.movieId);
            startActivity(intent);
        });
    }

    private void getMovieId() {
        Intent intent = getIntent();
        movieId = intent.getStringExtra(FirebaseConstants.MOVIEID_DOCUMENT);
    }

    private void fetchCommunityData() {
        db.collection(FirebaseConstants.COMMUNITIES_COLLECTION)
                .document(movieId)
                .collection(FirebaseConstants.POSTS_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        try {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String postId = document.getId();
                                String title = document.getString("title");
                                String nickname = document.getString("nickname");
                                String content = document.getString("content");

                                Log.d("Movie", postId);
                                Log.d("Movie", title);
                                Log.d("Movie", nickname);
                                Log.d("Movie", content);

                                PostsModel model = new PostsModel(title, postId, content, nickname);

                                dataList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (RuntimeException e) {
                            ToastHelper.showToast(PostsActivity.this, "데이터를 가져오지 못했습니다.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToastHelper.showToast(PostsActivity.this, "데이터를 가져오지 못했습니다.");
                    }
                });
    }
}