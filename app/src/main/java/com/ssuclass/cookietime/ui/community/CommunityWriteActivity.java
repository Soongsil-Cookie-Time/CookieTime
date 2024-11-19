package com.ssuclass.cookietime.ui.community;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuclass.cookietime.databinding.ActivityCommunityWriteBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityWriteActivity extends AppCompatActivity {

    ActivityCommunityWriteBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFirebaseInstance();
        setOnCLickListener();
    }

    private void setFirebaseInstance() {
        db = FirebaseFirestore.getInstance();
    }

    private void setOnCLickListener() {
        binding.toolbarDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> communities = new HashMap<>();

                String uuid = UUID.randomUUID().toString();
                String title = binding.titleEditext.getText().toString();

                communities.put(uuid, title);

                db.collection("Communities")
                        .add(communities)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("FirebasePushDebug", documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FirebasePushDebug", e.toString());
                            }
                        });

            }
        });
    }
}