package com.ssuclass.cookietime.presentation.badgemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.ssuclass.cookietime.databinding.ActivityInstagramSharingBinding;
import com.ssuclass.cookietime.util.ToastHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InstagramSharingActivity extends AppCompatActivity {
    private ActivityInstagramSharingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInstagramSharingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sharingButton.setOnClickListener(v -> shareCardViewToInstagram());
    }

    private void shareCardViewToInstagram() {
        CardView cardView = binding.cardview; // XML에서 CardView 참조
        Bitmap bitmap = getBitmapFromView(cardView);
        if (bitmap != null) {
            Uri imageUri = saveBitmapToFile(bitmap);
            if (imageUri != null) {
                shareToInstagram(imageUri);
            }
        }
    }

    private Bitmap getBitmapFromView(CardView view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Uri saveBitmapToFile(Bitmap bitmap) {
        File imagesFolder = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared");
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        File imageFile = new File(imagesFolder, "shared_image.png");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return FileProvider.getUriForFile(this, "com.ssuclass.cookietime.fileprovider", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void shareToInstagram(Uri imageUri) {
        // 인스타그램 스토리 공유 인텐트 생성
        Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
        intent.putExtra("source_application", "@string/facebook_app_id");

        intent.setDataAndType(imageUri, "image/*");

        grantUriPermission("com.instagram.android", imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // 인스타그램 앱이 설치되어 있는지 확인 후 인텐트 실행
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // 인스타그램 앱이 설치되어 있지 않은 경우 처리
            ToastHelper.showToast(this, "인스타그램이 없습니다.");
        }
    }
}