package com.ssuclass.cookietime.presentation.mypage.change.password;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ssuclass.cookietime.databinding.ActivityChangePasswordBinding;
import com.ssuclass.cookietime.util.ToastHelper;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
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
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                ToastHelper.showToast(this, "사용자 정보를 찾지 못했습니다.");
                return;
            }

            // 현재 비밀번호로 재인증
            String email = user.getEmail();
            String currentPassword = binding.inputCurrentPasswordEditext.getText().toString();
            AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

            user.reauthenticate(credential)
                    .addOnSuccessListener(unused -> {
                        // 재인증 성공 후 새 비밀번호로 업데이트
                        String newPassword = binding.inputChangePasswordEditext.getText().toString();
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(unused2 -> {
                                    ToastHelper.showToast(ChangePasswordActivity.this,
                                            "비밀번호 변경에 성공했습니다.");
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        ToastHelper.showToast(ChangePasswordActivity.this,
                                                "비밀번호 변경에 실패했습니다."));
                    })
                    .addOnFailureListener(e ->
                            ToastHelper.showToast(ChangePasswordActivity.this,
                                    "현재 비밀번호가 일치하지 않습니다."));
        });
    }
}