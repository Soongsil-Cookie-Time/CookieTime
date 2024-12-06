package com.ssuclass.cookietime.presentation.community.write;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuclass.cookietime.databinding.ActivityCommunityWriteBinding;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

import java.util.HashMap;
import java.util.Map;

public class PostsWriteActivity extends AppCompatActivity {

    private ActivityCommunityWriteBinding binding;
    private FirebaseFirestore db;
    private String movieId;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setMovieIdData();
        setFirebaseInstance();
        fetchUserNickname();
        setOnClickListener();
    }

    private void setMovieIdData() {
        Intent intent = getIntent();
        this.movieId = intent.getStringExtra(FirebaseConstants.MOVIEID_DOCUMENT);
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void setOnClickListener() {
        binding.toolbarDoneButton.setOnClickListener(view -> {
            String title = binding.titleEditext.getText().toString();
            String content = binding.contentEdittext.getText().toString();

            Map<String, Object> post = new HashMap<>();
            post.put("title", title);
            post.put("content", content);
            post.put("nickname", nickname);

            db.collection(FirebaseConstants.COMMUNITIES_COLLECTION)
                    .document(this.movieId)
                    .collection(FirebaseConstants.POSTS_COLLECTION)
                    .add(post)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            ToastHelper.showToast(PostsWriteActivity.this, "게시글 작성에 성공했습니다.");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ToastHelper.showToast(PostsWriteActivity.this, "게시글 작성에 실패했습니다.");
                        }
                    });
        });
    }

    private void fetchUserNickname() {
        db.collection(FirebaseConstants.USERS_COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        nickname = documentSnapshot.getString("nickname");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // FIXME: 12/6/24 실패했을 때 피드백 조치
                    }
                });
    }
}