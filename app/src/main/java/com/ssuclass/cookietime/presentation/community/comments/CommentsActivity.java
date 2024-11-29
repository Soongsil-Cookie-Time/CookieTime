package com.ssuclass.cookietime.presentation.community.comments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ssuclass.cookietime.databinding.ActivityPostBinding;
import com.ssuclass.cookietime.util.ToastHelper;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    private final ArrayList<CommentsModel> dataList;
    private ActivityPostBinding binding;
    private FirebaseFirestore db;
    private String movieId;
    private String title;
    private String content;
    private String nickname;
    private String postId;
    private CommentsAdapter adapter;

    public CommentsActivity() {
        this.dataList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getDatabaseData();
        setFirebaseInstance();
        showPostData();
        implementRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchCommentsData();
    }

    private void implementRecyclerView() {
        binding.commentRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsAdapter(this.dataList);
        binding.commentRecyclerview.setAdapter(adapter);
    }

    private void getDatabaseData() {
        Intent intent = getIntent();
        this.movieId = intent.getStringExtra("movieId");
        this.title = intent.getStringExtra("title");
        this.content = intent.getStringExtra("content");
        this.postId = intent.getStringExtra("postId");
        this.nickname = intent.getStringExtra("nickname");
    }

    private void showPostData() {
        binding.titleTextview.setText(this.title);
        binding.contentTextview.setText(this.content);
        binding.nicknameTextview.setText(this.nickname);
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void fetchCommentsData() {
        try {
            db.collection("Communities")
                    .document(movieId)
                    .collection("Posts")
                    .document(postId)
                    .collection("Comments")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String timestamp = document.getString("timestamp");
                                String nickname = document.getString("nickname");

                                Log.d("Comment", title);
                                Log.d("Comment", timestamp);
                                Log.d("Comment", nickname);

                                CommentsModel model = new CommentsModel();
                                model.setTitle(title);
                                model.setNickname(nickname);
                                model.setTimestamp(timestamp);
                                dataList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("FirebaseError", "Error fetching data: ", task.getException());
                        }
                    });
        } catch (RuntimeException e) {
            ToastHelper.showToast(this, "데이터를 불러오지 못했습니다.");
        }

    }
}