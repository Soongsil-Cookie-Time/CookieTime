package com.ssuclass.cookietime.presentation.mypage.change.nickname;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuclass.cookietime.databinding.ActivityChangeNicknameBinding;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

public class ChangeNicknameActivity extends AppCompatActivity {

    ActivityChangeNicknameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChangeNicknameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addButtonListener();
    }

    private void addButtonListener() {
        binding.changeButton.setOnClickListener(view -> {
            FirebaseUser user;
            String uid = null;

            try {
                user = FirebaseAuth.getInstance().getCurrentUser();
                uid = user.getUid();
            } catch (RuntimeException e) {
                ToastHelper.showToast(ChangeNicknameActivity.this, "사용자 정보를 찾지 못했습니다.");
                return;
            }

            // 입력값 가져오기
            String newNickname = binding.inputChangeNicknameEdittext.getText().toString();

            // 입력값 검증
            if (newNickname.trim().isEmpty()) {
                ToastHelper.showToast(ChangeNicknameActivity.this, "닉네임을 입력해주세요.");
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(FirebaseConstants.USERS_COLLECTION)
                    .document(uid)
                    .update("nickname", newNickname)
                    .addOnSuccessListener(unused -> {
                        ToastHelper.showToast(ChangeNicknameActivity.this, "닉네임 변경에 성공했습니다.");
                        setResult(RESULT_OK); // 결과 코드 설정
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        ToastHelper.showToast(ChangeNicknameActivity.this, "닉네임 변경에 실패했습니다.");
                    });
        });
    }
}