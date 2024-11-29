package com.ssuclass.cookietime.presentation.mypage.friendlist;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ssuclass.cookietime.databinding.ActivityFriendListBinding;
import com.ssuclass.cookietime.util.FirebaseConstants;
import com.ssuclass.cookietime.util.ToastHelper;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {

    private final ArrayList<FriendListModel> dataList;
    private ActivityFriendListBinding binding;
    private FriendListAdapter adapter;
    private FirebaseFirestore db;

    public FriendListActivity() {
        this.dataList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFriendListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setFirebaseInstance();
        fetchFriendData();
        implementRecyclerView();
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void implementRecyclerView() {
        binding.friendListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendListAdapter(dataList);
        binding.friendListRecyclerview.setAdapter(adapter);
    }

    private void fetchFriendData() {
        try {
            db.collection(FirebaseConstants.USERS_COLLECTION)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            dataList.clear();
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String friendName = documentSnapshot.getString("username");
                                String nickname = documentSnapshot.getString("nickname");

                                FriendListModel model = new FriendListModel();
                                model.setFriendName(friendName);
                                model.setNickname(nickname);

                                dataList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ToastHelper.showToast(FriendListActivity.this, e.getLocalizedMessage());
                        }
                    });
        } catch (RuntimeException e) {
            ToastHelper.showToast(this, e.getLocalizedMessage());
        }

    }
}