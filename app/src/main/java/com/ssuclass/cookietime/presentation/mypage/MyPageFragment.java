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
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuclass.cookietime.databinding.FragmentMyPageBinding;
import com.ssuclass.cookietime.presentation.authentication.AuthentiacationActivity;
import com.ssuclass.cookietime.presentation.mypage.change.nickname.ChangeNicknameActivity;
import com.ssuclass.cookietime.presentation.mypage.change.password.ChangePasswordActivity;
import com.ssuclass.cookietime.presentation.mypage.friendlist.FriendListActivity;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

public class MyPageFragment extends Fragment {
    private final MutableLiveData<UserData> userData = new MutableLiveData<>();
    private FragmentMyPageBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance() {
        return new MyPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFirebaseInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyPageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 먼저 옵저버 설정
        setupObservers();

        // 그 다음 데이터 로드
        fetchUserData();
        addButtonListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupObservers() {
        userData.observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                binding.profileNickname.setText(data.nickname + "님 안녕하세요!");
                binding.profileUsername.setText(data.username);
            } else {
                binding.profileNickname.setText("");
                binding.profileUsername.setText("");
            }
        });
    }

    private void setFirebaseInstance() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            ToastHelper.showToast(getContext(), "사용자 정보를 확인할 수 없습니다.");
            return;
        }

        String uid = currentUser.getUid();
        db.collection(FirebaseConstants.USERS_COLLECTION)
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String nickname = document.getString("nickname");
                        String username = document.getString("username");
                        requireActivity().runOnUiThread(() -> {
                            userData.setValue(new UserData(nickname, username));
                        });
                    } else {
                        ToastHelper.showToast(getContext(), "사용자 데이터가 존재하지 않습니다.");
                    }
                })
                .addOnFailureListener(e -> {
                    ToastHelper.showToast(getContext(), "데이터를 불러오는데 실패했습니다.");
                });

        db.collection((FirebaseConstants.USERS_COLLECTION))
                .document(uid)
                .collection(FirebaseConstants.WATCHED_MOVIE_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int watchedMoviesCount = queryDocumentSnapshots.size();
                    binding.badgeCountTextviewFront.setText("지금까지 총");
                    binding.badgeCountTextviewNumber.setText(Integer.toString(watchedMoviesCount));
                    binding.badgeCountTextviewBack.setText("개의 쿠키 인증 뱃지를 획득했어요!");
                })
                .addOnFailureListener(e -> {
                    ToastHelper.showToast(getContext(), "데이터를 불러오는데 실패했습니다.");
                });
    }

    private void addButtonListener() {
        binding.friendListViewgroup.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), FriendListActivity.class);
            startActivity(intent);
        });

        binding.changeNicknameViewgroup.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ChangeNicknameActivity.class);
            startActivity(intent);
        });

        binding.changePasswordViewgroup.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(intent);
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
        mAuth.signOut();
        ToastHelper.showToast(requireContext(), "로그아웃 되었습니다.");

        Intent loginIntent = new Intent(requireContext(), AuthentiacationActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private static class UserData {
        String nickname;
        String username;

        UserData(String nickname, String username) {
            this.nickname = nickname;
            this.username = username;
        }
    }


}