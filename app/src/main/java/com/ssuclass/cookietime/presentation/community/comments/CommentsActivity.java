package com.ssuclass.cookietime.presentation.community.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ssuclass.cookietime.databinding.ActivityCommentsBinding;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private final ArrayList<CommentsModel> dataList;
    private ActivityCommentsBinding binding;
    private FirebaseFirestore db;
    private String movieId;
    private String title;
    private String content;
    private String nickname;
    private String postId;
    private String currentUserNickname;
    private CommentsAdapter adapter;

    public CommentsActivity() {
        this.dataList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDatabaseData();
        setFirebaseInstance();
        showPostData();
        implementRecyclerView();
        setupCommentInput();
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

    private void setupCommentInput() {
        // 전송 버튼 클릭 이벤트 처리
        binding.sendButton.setOnClickListener(v -> {
            String commentText = binding.commentInputEdittext.getText().toString().trim();
            if (!commentText.isEmpty()) {
                saveComment(commentText);
            } else {
                ToastHelper.showToast(this, "댓글을 입력해주세요.");
            }
        });
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void saveComment(String commentText) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // 닉네임을 먼저 가져온 후, 성공했을 때 댓글을 저장하도록 수정
        db.collection(FirebaseConstants.USERS_COLLECTION)
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String userNickname = documentSnapshot.getString(FirebaseConstants.NICKNAME_FIELD);

                    // 현재 시간을 타임스탬프로 사용
                    String timestamp = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
                            .format(new Date());

                    // 댓글 데이터 구성
                    Map<String, Object> comment = new HashMap<>();
                    comment.put("title", commentText);
                    comment.put("timestamp", timestamp);
                    comment.put("nickname", userNickname);

                    // Firestore에 댓글 저장
                    db.collection(FirebaseConstants.COMMUNITIES_COLLECTION)
                            .document(movieId)
                            .collection(FirebaseConstants.POSTS_COLLECTION)
                            .document(postId)
                            .collection(FirebaseConstants.COMMENTS_COLLECTION)
                            .add(comment)
                            .addOnSuccessListener(documentReference -> {
                                binding.commentInputEdittext.setText("");
                                fetchCommentsData();

                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.commentInputEdittext.getWindowToken(), 0);

                                ToastHelper.showToast(CommentsActivity.this, "댓글이 등록되었습니다.");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FirebaseError", "Error saving comment: ", e);
                                ToastHelper.showToast(CommentsActivity.this, "댓글 등록에 실패했습니다.");
                            });
                })
                .addOnFailureListener(e -> ToastHelper.showToast(CommentsActivity.this, "사용자 정보를 가져오지 못했습니다."));
    }

    // 기존의 fetchCommentsData() 메소드는 약간의 최적화가 필요합니다
    private void fetchCommentsData() {
        try {
            db.collection("Communities")
                    .document(movieId)
                    .collection("Posts")
                    .document(postId)
                    .collection("Comments")
                    .orderBy("timestamp", Query.Direction.DESCENDING) // 최신 댓글이 위로 오도록 정렬
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CommentsModel model = document.toObject(CommentsModel.class);
                                dataList.add(model);
                            }
                            adapter.notifyDataSetChanged();

                            // 새 댓글이 추가되면 RecyclerView를 최상단으로 스크롤
                            if (!dataList.isEmpty()) {
                                binding.commentRecyclerview.smoothScrollToPosition(0);
                            }
                        } else {
                            Log.e("FirebaseError", "Error fetching data: ", task.getException());
                        }
                    });
        } catch (RuntimeException e) {
            ToastHelper.showToast(this, "데이터를 불러오지 못했습니다.");
        }
    }
}