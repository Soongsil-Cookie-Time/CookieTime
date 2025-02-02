package com.ssuclass.cookietime.presentation.bottomnavigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.ActivityMainBinding;
import com.ssuclass.cookietime.presentation.badgemanager.BadgeManagerFragment;
import com.ssuclass.cookietime.presentation.community.main.CommunitiesMainFragment;
import com.ssuclass.cookietime.presentation.cookieinfo.CookieInfoFragment;
import com.ssuclass.cookietime.presentation.home.HomeFragment;
import com.ssuclass.cookietime.presentation.mypage.MyPageFragment;

public class MainActivity extends AppCompatActivity {
    // Fragment 인스턴스들을 저장
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment communitiesFragment = new CommunitiesMainFragment();
    private final Fragment badgeManagerFragment = new BadgeManagerFragment();
    private final Fragment myPageFragment = new MyPageFragment();
    private ActivityMainBinding binding;
    // 현재 표시중인 Fragment를 추적
    private Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getBooleanExtra("show_cookie_info", false)) {
            int movieId = getIntent().getIntExtra("movie_id", -1);
            String movieTitle = getIntent().getStringExtra("movie_title");

            if (movieId != -1) {
                // 쿠키 정보 Fragment 생성 및 표시
                CookieInfoFragment cookieInfoFragment =
                        CookieInfoFragment.newInstance(movieId, movieTitle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, cookieInfoFragment)
                        .commit();
            }
        } else {
            // FIXME: 2024. 12. 6. 사용자 정보 가져오는 문제 해결
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, myPageFragment).hide(myPageFragment)
                    .add(R.id.fragment_container, badgeManagerFragment).hide(badgeManagerFragment)
                    .add(R.id.fragment_container, communitiesFragment).hide(communitiesFragment)
                    .add(R.id.fragment_container, homeFragment)
                    .commit();
        }

        setBottomNavigationView();
    }

    private void setBottomNavigationView() {
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            int itemId = item.getItemId();

            if (itemId == R.id.tab_home) {
                selectedFragment = homeFragment;
            } else if (itemId == R.id.tab_community) {
                selectedFragment = communitiesFragment;
            } else if (itemId == R.id.tab_badges) {
                selectedFragment = badgeManagerFragment;
            } else if (itemId == R.id.tab_mypage) {
                selectedFragment = myPageFragment;
            } else {
                return false;
            }

            // 현재 Fragment를 숨기고 선택된 Fragment를 보여줌
            getSupportFragmentManager().beginTransaction()
                    .hide(activeFragment)
                    .show(selectedFragment)
                    .commit();

            activeFragment = selectedFragment;
            return true;
        });
    }
}