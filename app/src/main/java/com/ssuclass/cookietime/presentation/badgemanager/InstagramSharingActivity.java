package com.ssuclass.cookietime.presentation.badgemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.ActivityInstagramSharingBinding;
import com.ssuclass.cookietime.databinding.SnapshotInstagramBinding;
import com.ssuclass.cookietime.network.MovieAPI;
import com.ssuclass.cookietime.network.response.TMDBMovieDetailResponse;
import com.ssuclass.cookietime.util.ToastHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstagramSharingActivity extends AppCompatActivity {

    // 뷰 바인딩 객체들을 선언합니다
    private ActivityInstagramSharingBinding binding;          // 메인 화면용 바인딩
    private SnapshotInstagramBinding snapshotBinding;        // 인스타그램 공유용 바인딩
    private String movieId;                                  // 영화 ID 저장
    private String movieTitle;                               // 영화 제목 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 두 개의 레이아웃을 위한 바인딩 초기화
        binding = ActivityInstagramSharingBinding.inflate(getLayoutInflater());
        snapshotBinding = SnapshotInstagramBinding.inflate(getLayoutInflater());

        // 메인 화면 설정 (activity_instagram_sharing.xml)
        setContentView(binding.getRoot());

        // 초기 설정 메서드들 호출
        getDataFromIntent();           // Intent에서 데이터 가져오기
        setMovieDataIntoView();        // 화면에 영화 데이터 표시
        addButtonListener();           // 버튼 리스너 설정
        fetchMovieDetail();            // 영화 상세 정보 가져오기
    }

    // Intent에서 영화 정보 가져오기
    private void getDataFromIntent() {
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movie_id");
        movieTitle = intent.getStringExtra("movie_title");
    }

    // 화면에 영화 데이터 표시
    private void setMovieDataIntoView() {
        binding.movieTitleTextview.setText(movieTitle);
        snapshotBinding.movieTitleTextview.setText(movieTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        String formattedDate = sdf.format(new Date());
        binding.dateTextview.setText(formattedDate);
        snapshotBinding.dateTextview.setText(formattedDate);
    }

    // 공유하기 버튼에 클릭 리스너 추가
    private void addButtonListener() {
        binding.sharingButton.setOnClickListener(v -> shareSnapshotToInstagram());
    }

    // TMDB API를 통해 영화 상세 정보 가져오기
    private void fetchMovieDetail() {
        int movieIdInt = Integer.parseInt(movieId);

        MovieAPI.fetchMovieDetail(
                movieIdInt,
                getString(R.string.TMDB_api_key),
                "ko-KR",
                new Callback<TMDBMovieDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TMDBMovieDetailResponse> call,
                                           @NonNull Response<TMDBMovieDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            loadMoviePoster(response.body().getPosterPath());
                        } else {
                            ToastHelper.showToast(InstagramSharingActivity.this,
                                    "영화 정보를 불러오는데 실패했습니다.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TMDBMovieDetailResponse> call,
                                          @NonNull Throwable t) {
                        ToastHelper.showToast(InstagramSharingActivity.this,
                                "네트워크 오류가 발생했습니다.");
                    }
                }
        );
    }

    // 영화 포스터 이미지 로드
    private void loadMoviePoster(String posterPath) {
        if (posterPath != null) {
            String fullPosterPath = "https://image.tmdb.org/t/p/w500" + posterPath;

            // 메인 화면의 포스터 이미지 로드
            Glide.with(this)
                    .load(fullPosterPath)
                    .into(binding.moviePosterImageview);

            // 스냅샷용 포스터 이미지 로드
            Glide.with(this)
                    .load(fullPosterPath)
                    .override(800, 1200)  // 이미지 크기 지정
                    .centerCrop()         // 이미지가 ImageView에 꽉 차도록 설정
                    .into(snapshotBinding.moviePosterImageview);
        }
    }

    // 스냅샷 이미지를 인스타그램으로 공유
    private void shareSnapshotToInstagram() {
        // snapshot_instagram.xml 레이아웃 가져오기
        View snapshotView = snapshotBinding.getRoot();

        // 레이아웃 크기 측정을 위한 설정
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // 레이아웃 측정 및 배치
        snapshotView.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        );
        snapshotView.layout(0, 0, snapshotView.getMeasuredWidth(), snapshotView.getMeasuredHeight());

        // 비트맵 생성 및 공유
        Bitmap bitmap = getBitmapFromView(snapshotView);
        if (bitmap != null) {
            Uri imageUri = saveBitmapToFile(bitmap);
            if (imageUri != null) {
                shareToInstagram(imageUri);
            }
        }
    }

    // View를 Bitmap으로 변환
    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(
                view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    // Bitmap을 파일로 저장하고 Uri 반환
    private Uri saveBitmapToFile(Bitmap bitmap) {
        // 앱 전용 외부 저장소에 shared 폴더 생성
        File imagesFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared");
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        // 비트맵을 PNG 파일로 저장
        File imageFile = new File(imagesFolder, "shared_image.png");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return FileProvider.getUriForFile(this,
                    "com.ssuclass.cookietime.fileprovider",
                    imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 인스타그램으로 이미지 공유
    private void shareToInstagram(Uri imageUri) {
        // 인스타그램 스토리 공유 인텐트 생성
        Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
        intent.putExtra("source_application", getString(R.string.facebook_app_id));
        intent.setDataAndType(imageUri, "image/*");

        // 인스타그램 앱에 URI 접근 권한 부여
        grantUriPermission(
                "com.instagram.android",
                imageUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        );

        // 인스타그램 앱 실행 가능 여부 확인 후 실행
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            ToastHelper.showToast(this, "인스타그램이 설치되어 있지 않습니다.");
        }
    }
}