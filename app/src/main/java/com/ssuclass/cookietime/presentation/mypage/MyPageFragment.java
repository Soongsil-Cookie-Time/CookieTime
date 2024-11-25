package com.ssuclass.cookietime.presentation.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.ssuclass.cookietime.databinding.FragmentMyPageBinding;
import com.ssuclass.cookietime.presentation.Login.AuthentiacationActivity;
import com.ssuclass.cookietime.util.ToastHelper;

public class MyPageFragment extends Fragment {
    private FragmentMyPageBinding binding;
    private FirebaseAuth firebaseAuth;

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance(String param1, String param2) {
        return new MyPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyPageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addButtonListener();
    }

    private void addButtonListener() {
        binding.friendListViewgroup.setOnClickListener(view -> {
            // Friend list implementation
        });

        binding.changeNicknameViewgroup.setOnClickListener(view -> {
            // Change nickname implementation
        });

        binding.changePasswordViewgroup.setOnClickListener(view -> {
            // Change password implementation
        });

        binding.logoutViewgroup.setOnClickListener(view -> {
            showLogoutConfirmationDialog();
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton("확인", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("취소", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void performLogout() {
        firebaseAuth.signOut();
        ToastHelper.showToast(requireContext(), "로그아웃 되었습니다.");

        Intent loginIntent = new Intent(requireContext(), AuthentiacationActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}