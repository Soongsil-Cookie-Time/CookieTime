package com.ssuclass.cookietime.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ssuclass.cookietime.databinding.ActivityAuthenticationBinding;
import com.ssuclass.cookietime.presentation.bottomnavigation.MainActivity;

public class AuthentiacationActivity extends AppCompatActivity {

    private ActivityAuthenticationBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // 이미 로그인된 사용자가 있다면 MainActivity로 이동
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addButtonListener();
    }


    private void addButtonListener() {
        binding.signinButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        });

        binding.signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });
    }
}