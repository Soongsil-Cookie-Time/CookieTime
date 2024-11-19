package com.ssuclass.cookietime.presentation.community;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuclass.cookietime.databinding.ActivityCommunityWriteBinding;

public class CommunityWriteActivity extends AppCompatActivity {

    ActivityCommunityWriteBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFirebaseInstance();
        setOnCLickListener();
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void setOnCLickListener() {
        binding.toolbarDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 사용자가 별 작성한 글을 어떻게 분간할 것인지 확인, DB 구성을 어떻게 가져가야 하는지 연구
            }
        });
    }
}