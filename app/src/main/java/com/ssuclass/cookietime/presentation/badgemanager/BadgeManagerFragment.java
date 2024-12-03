package com.ssuclass.cookietime.presentation.badgemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssuclass.cookietime.databinding.FragmentBadgeManagerBinding;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BadgeManagerFragment extends Fragment {

    private final ArrayList<BadgeModel> dataList;
    private FragmentBadgeManagerBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FragmentBadgeManagerAdapter adapter;

    public BadgeManagerFragment() {
        dataList = new ArrayList<>();
    }

    public static BadgeManagerFragment newInstance() {
        return new BadgeManagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFirebaseInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchWatchedMovie();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBadgeManagerBinding.inflate(getLayoutInflater());
        setRecyclerView();
        return binding.getRoot();
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = binding.monthlyBadgesRecyclerview;
        adapter = new FragmentBadgeManagerAdapter(getContext(), dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fetchWatchedMovie() {
        db.collection(FirebaseConstants.USERS_COLLECTION)
                .document(user.getUid())
                .collection(FirebaseConstants.WATCHED_MOVIE_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // 기존 데이터 초기화
                        dataList.clear();

                        // 월별로 데이터를 저장할 임시 Map
                        // Key: "2024-3"와 같은 형태로 년도-월을 저장
                        // Value: 해당 월의 BadgeModel 리스트
                        Map<String, List<BadgeModel>> monthlyMovies = new TreeMap<>(Collections.reverseOrder());

                        // 데이터 파싱 및 월별 그룹화
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            try {
                                String title = documentSnapshot.getString(FirebaseConstants.TITLE_FIELD);
                                Timestamp timestamp = documentSnapshot.getTimestamp(FirebaseConstants.TIMESTAMP_FIELD);

                                // Timestamp를 LocalDate로 변환
                                LocalDate localDate = timestamp.toDate()
                                        .toInstant()
                                        .atZone(ZoneId.of("Asia/Seoul"))
                                        .toLocalDate();

                                int year = localDate.getYear();
                                int month = localDate.getMonthValue();

                                // 년도-월 키 생성
                                String yearMonthKey = String.format("%d-%d", year, month);

                                // BadgeModel 생성
                                BadgeModel model = new BadgeModel();
                                model.setTitle(title);
                                model.setYear(String.valueOf(year));
                                model.setMonth(String.valueOf(month));

                                // 해당 월의 리스트가 없으면 새로 생성
                                monthlyMovies.computeIfAbsent(yearMonthKey, k -> new ArrayList<>()).add(model);

                            } catch (Exception e) {
                                ToastHelper.showToast(getContext(), "정보를 불러오는데 실패했습니다.");
                            }
                        }

                        // 그룹화된 데이터를 dataList에 추가
                        // TreeMap을 사용했으므로 자동으로 년도-월 순으로 정렬됨
                        for (List<BadgeModel> monthGroup : monthlyMovies.values()) {
                            dataList.addAll(monthGroup);
                        }

                        // 어댑터에 데이터 변경 알림
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToastHelper.showToast(getContext(), "뱃지 정보를 불러오는데 실패했습니다.");
                    }
                });
    }
}