package com.ssuclass.cookietime.presentation.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssuclass.cookietime.databinding.FragmentCommunityEntryBinding;
import com.ssuclass.cookietime.domain.CommunityEntryModel;
import com.ssuclass.cookietime.util.SpaceingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CommunityEntryFragment extends Fragment {
    private FragmentCommunityEntryBinding binding;
    private FirebaseFirestore db;
    private List<CommunityEntryModel> dataList;
    private CommunityEntryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList<>();
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
    public void onStart() {
        super.onStart();
        fetchCommunitiesList();
    }

    private void setCommunityRecyclerView() {
        RecyclerView communityRecyclerView = binding.communityRecyclerview;
        communityRecyclerView.addItemDecoration(new SpaceingItemDecoration(14));
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CommunityEntryAdapter(dataList);
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
                            dataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CommunityEntryModel model = new CommunityEntryModel();
                                String id = document.getId();
                                String title = document.getString("title");
                                String posterPath = document.getString("poster_path");
                                model.setId(id);
                                model.setTitle(title);
                                model.setMoviePosterPath(posterPath);
                                dataList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e("FirebaseError", "Error fetching data: ", task.getException());
                        }
                    }
                });
    }
}
