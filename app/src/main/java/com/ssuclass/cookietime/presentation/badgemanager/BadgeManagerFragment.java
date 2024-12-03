package com.ssuclass.cookietime.presentation.badgemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssuclass.cookietime.databinding.FragmentBadgeManagerBinding;
import com.ssuclass.cookietime.presentation.badgemanager.model.BadgeModel;
import com.ssuclass.cookietime.presentation.badgemanager.model.MonthGroup;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class BadgeManagerFragment extends Fragment {

    private FragmentBadgeManagerBinding binding;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FragmentBadgeManagerAdapter adapter;

    public BadgeManagerFragment() {

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
        adapter = new FragmentBadgeManagerAdapter(getContext(), new ArrayList<>());
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
                        Map<String, MonthGroup> monthGroupMap = new TreeMap<>(Collections.reverseOrder());

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            try {
                                String title = documentSnapshot.getString(FirebaseConstants.TITLE_FIELD);
                                Timestamp timestamp = documentSnapshot.getTimestamp(FirebaseConstants.TIMESTAMP_FIELD);
                                LocalDate localDate = timestamp.toDate()
                                        .toInstant()
                                        .atZone(ZoneId.of("Asia/Seoul"))
                                        .toLocalDate();

                                String year = String.valueOf(localDate.getYear());
                                String month = String.valueOf(localDate.getMonthValue());

                                // 월을 두 자리 숫자로 포맷팅하여 키 생성
                                String key = String.format("%s-%02d", year, Integer.parseInt(month));

                                if (!monthGroupMap.containsKey(key)) {
                                    monthGroupMap.put(key, new MonthGroup(year, month));
                                }

                                BadgeModel movie = new BadgeModel();
                                movie.setTitle(title);
                                movie.setYear(year);
                                movie.setMonth(month);

                                monthGroupMap.get(key).addMovie(movie);
                            } catch (Exception e) {
                                ToastHelper.showToast(getContext(), "정보를 불러오는데 실패했습니다.");
                            }
                        }

                        // 어댑터 설정
                        adapter = new FragmentBadgeManagerAdapter(getContext(),
                                new ArrayList<>(monthGroupMap.values())
                        );
                        binding.monthlyBadgesRecyclerview.setAdapter(adapter);
                    }
                });
    }
}