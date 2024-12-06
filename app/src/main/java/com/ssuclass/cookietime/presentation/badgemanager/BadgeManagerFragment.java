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
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentBadgeManagerBinding;
import com.ssuclass.cookietime.presentation.badgemanager.model.BadgeModel;
import com.ssuclass.cookietime.presentation.badgemanager.model.MonthGroup;
import com.ssuclass.cookietime.util.FirebaseConstants;

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

        int verticalSpaceInPixels = getResources().getDimensionPixelSize(R.dimen.month_spacing);
        recyclerView.addItemDecoration(new FragmentBadgeManagerAdapter.VerticalSpaceItemDecoration(verticalSpaceInPixels));
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
                                String movieId = documentSnapshot.getString(FirebaseConstants.MOVIEID_TITLE);
                                Timestamp timestamp = documentSnapshot.getTimestamp(FirebaseConstants.TIMESTAMP_FIELD);
                                LocalDate localDate = timestamp.toDate()
                                        .toInstant()
                                        .atZone(ZoneId.of("Asia/Seoul"))
                                        .toLocalDate();
                                String year = String.valueOf(localDate.getYear());
                                String month = String.valueOf(localDate.getMonthValue());
                                String key = String.format("%s-%02d", year, Integer.parseInt(month));

                                if (!monthGroupMap.containsKey(key)) {
                                    monthGroupMap.put(key, new MonthGroup(year, month));
                                }

                                BadgeModel movie = new BadgeModel();
                                movie.setMovieId(movieId);
                                movie.setTitle(title);
                                movie.setYear(year);
                                movie.setMonth(month);

                                monthGroupMap.get(key).addMovie(movie);
                            } catch (Exception e) {
                                // FIXME: 12/6/24 실패했을 때 피드백 조치
                                return;
                            }
                        }

                        adapter = new FragmentBadgeManagerAdapter(getContext(),
                                new ArrayList<>(monthGroupMap.values())
                        );
                        binding.monthlyBadgesRecyclerview.setAdapter(adapter);
                    }
                });
    }
}