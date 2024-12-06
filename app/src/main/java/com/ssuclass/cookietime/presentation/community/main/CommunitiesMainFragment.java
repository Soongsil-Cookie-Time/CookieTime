package com.ssuclass.cookietime.presentation.community.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssuclass.cookietime.databinding.FragmentCommunityEntryBinding;
import com.ssuclass.cookietime.presentation.community.main.model.CommunitiesModel;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.SpaceingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CommunitiesMainFragment extends Fragment {
    private FragmentCommunityEntryBinding binding;
    private FirebaseFirestore db;
    private List<CommunitiesModel> dataList;
    private List<String> watchedMovieList;
    private CommunitiesAdapter adapter;
    private List<CommunitiesModel> allCommunities; // 모든 커뮤니티 저장용 리스트 추가

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList<>();
        watchedMovieList = new ArrayList<>(); // watchedMovieList 초기화
        allCommunities = new ArrayList<>(); // allCommunities 초기화
        setFirebaseFirestore();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommunityEntryBinding.inflate(getLayoutInflater(), container, false);
        setCommunityRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchCommunitiesList();
        fetchUserWatchedMovie();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchCommunitiesList();
        fetchUserWatchedMovie();
    }

    private void setCommunityRecyclerView() {
        RecyclerView communityRecyclerView = binding.communityRecyclerview;
        communityRecyclerView.addItemDecoration(new SpaceingItemDecoration(14));
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CommunitiesAdapter(dataList);
        communityRecyclerView.setAdapter(adapter);
    }

    private void setFirebaseFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void fetchCommunitiesList() {
        db.collection("Communities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allCommunities.clear(); // 전체 커뮤니티 리스트 초기화
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CommunitiesModel model = new CommunitiesModel();
                                String id = document.getId();
                                String title = document.getString("title");
                                String posterPath = document.getString("poster_path");
                                model.setId(id);
                                model.setTitle(title);
                                model.setMoviePosterPath(posterPath);
                                allCommunities.add(model); // 전체 커뮤니티 리스트에 추가
                            }
                            filterWatchedMovieCommunities(); // 시청한 영화 커뮤니티 필터링
                        } else {
                            Log.e("FirebaseError", "Error fetching data: ", task.getException());
                        }
                    }
                });
    }

    private void fetchUserWatchedMovie() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        db.collection(FirebaseConstants.USERS_COLLECTION)
                .document(user.getUid())
                .collection(FirebaseConstants.WATCHED_MOVIE_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        watchedMovieList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String watchedMovieId = document.getId();
                            watchedMovieList.add(watchedMovieId);
                        }
                        filterWatchedMovieCommunities(); // 시청한 영화 커뮤니티 필터링
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseError", "Error fetching watched movies: ", e);
                    }
                });
    }

    // 시청한 영화의 커뮤니티만 필터링하는 메소드
    private void filterWatchedMovieCommunities() {
        // watchedMovieList와 allCommunities가 모두 로드되었을 때만 필터링
        if (watchedMovieList.isEmpty() || allCommunities.isEmpty()) {
            return;
        }
        dataList.clear(); // 현재 표시 중인 리스트 초기화
        // 시청한 영화의 커뮤니티만 필터링하여 추가
        for (CommunitiesModel community : allCommunities) {
            if (watchedMovieList.contains(community.getId())) {
                dataList.add(community);
            }
        }
        adapter.notifyDataSetChanged(); // 리사이클러뷰 갱신
    }
}
