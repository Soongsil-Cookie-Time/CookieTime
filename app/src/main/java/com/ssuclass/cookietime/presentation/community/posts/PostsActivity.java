package com.ssuclass.cookietime.presentation.community.posts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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
    private String movieTitle;

    public PostsActivity() {
        dataList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getMovieData();  // getMovieId() 대신 새로운 메서드
        setToolbarTitle();  // 새로 추가
        setButtonTitle();   // 새로 추가
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


    private void getMovieData() {
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");
        movieTitle = intent.getStringExtra("movieTitle");
    }

    private void setToolbarTitle() {
        TextView toolbarTitle = binding.communityDetailToolbarTitleTextview;
        toolbarTitle.setText("[" + movieTitle + "] 쿠키 커뮤니티");
    }

    private void setButtonTitle() {
        Button cookieButton = binding.goToCookieDetailButton;
        cookieButton.setText("[" + movieTitle + "] 쿠키 정보 보러가기");
    }

    private void setFloatingButtonOnClickListener() {
        ExtendedFloatingActionButton button = binding.communityWriteButton;
        button.setOnClickListener(view -> {
            Intent intent = new Intent(PostsActivity.this, PostsWriteActivity.class);
            intent.putExtra(FirebaseConstants.MOVIEID_DOCUMENT, this.movieId);
            startActivity(intent);
        });
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