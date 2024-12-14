package com.ssuclass.cookietime.presentation.authentication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuclass.cookietime.databinding.ActivitySignupBinding;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addFirebaseInstance();
        addButtonListener();
    }

    private void addFirebaseInstance() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void addButtonListener() {
        binding.signupButton.setOnClickListener(view -> {
            String email = binding.inputEmailEdittext.getText().toString();
            String password = binding.inputPasswordEdittext.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                makeSignupSuccessDialog();
                            } else {
                                makeSignupFailureDialog();
                            }
                        }
                    });
        });
    }

    private void makeSignupSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("알림")
                .setMessage("회원가입에 성공했습니다!")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = binding.inputNameEditText.getText().toString();
                        String nickname = binding.inputNicknameEditText.getText().toString();

                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("nickname", nickname);

                        String uid = mAuth.getCurrentUser().getUid();
                        db.collection(FirebaseConstants.USERS_COLLECTION)
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        makeSigninRequestDialog();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ToastHelper.showToast(SignupActivity.this, "사용자 정보 저장에 실패했습니다.");
                                    }
                                });
                    }
                })
                .show();
    }

    private void makeSignupFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder
                .setTitle("알림")
                .setMessage("회원가입에 실패했습니다.\n다시 시도하세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: 확인버튼 이후 액션이 필요없는데도, 꼭 작성해야 하는 코드인지?
                    }
                })
                .show();
    }

    private void makeSigninRequestDialog() {
        AlertDialog.Builder signinDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
        signinDialogBuilder
                .setTitle("알림")
                .setMessage("로그인 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SignupActivity.this, AuthentiacationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }
}