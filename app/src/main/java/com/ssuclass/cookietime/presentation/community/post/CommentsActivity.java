package com.ssuclass.cookietime.presentation.community.post;

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
import com.ssuclass.cookietime.domain.community.CommentsModel;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    private final ArrayList<CommentsModel> dataList;
    private ActivityPostBinding binding;
    private FirebaseFirestore db;
    private String movieId;
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

        getMovieIdData();
        setFirebaseInstance();
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

    private void getMovieIdData() {
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");
        postId = intent.getStringExtra("postId");
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void fetchCommentsData() {
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
                            CommentsModel model = new CommentsModel();
                            model.setTitle(title);
                            model.setTimestamp(timestamp);
                            dataList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirebaseError", "Error fetching data: ", task.getException());
                    }
                });
    }
}