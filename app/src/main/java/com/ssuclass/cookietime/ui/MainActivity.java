package com.ssuclass.cookietime.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.ActivityMainBinding;
import com.ssuclass.cookietime.ui.badgemanager.BadgeManagerFragment;
import com.ssuclass.cookietime.ui.community.CommunityEntryFragment;
import com.ssuclass.cookietime.ui.home.HomeFragment;
import com.ssuclass.cookietime.ui.mypage.MyPageFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBottomNavigationView();
    }

    private void setBottomNavigationView() {
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.tab_home) {
                    replaceFragment(new HomeFragment());
                    return true;
                } else if (itemId == R.id.tab_community) {
                    replaceFragment(CommunityEntryFragment.newInstance());
                    return true;
                } else if (itemId == R.id.tab_badges) {
                    replaceFragment(new BadgeManagerFragment());
                    return true;
                } else if (itemId == R.id.tab_mypage) {
                    replaceFragment(new MyPageFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}