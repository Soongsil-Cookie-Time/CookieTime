package com.ssuclass.cookietime.presentation.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.ActivitySigninBinding;
import com.ssuclass.cookietime.presentation.bottomnavigation.MainActivity;
import com.ssuclass.cookietime.util.ToastHelper;

public class SigninActivity extends AppCompatActivity {

    ActivitySigninBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addFirebaseAuthInstance();
        addButtonListener();
    }

    private void addFirebaseAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void addButtonListener() {
        binding.signinButton.setOnClickListener(view -> {
            String email = binding.inputEmailEdittext.getText().toString();
            String password = binding.inputPasswordEdittext.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                ToastHelper.showToast(SigninActivity.this, "환영합니다." + user.getEmail());

                                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastHelper.showToast(SigninActivity.this, "이메일, 비밀번호를 다시 확인해주세요.");
                            }
                        }
                    });
        });
    }
}