package com.ssuclass.cookietime.presentation.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ssuclass.cookietime.R;
import com.ssuclass.cookietime.databinding.FragmentSurveyBinding;
import com.ssuclass.cookietime.domain.CookieKeywordModel;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SurveyFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "movie_id"; // Argument Key
    private static final String ARG_MOVIE_TITLE = "movie_title"; // Argument Key for movie title
    private FragmentSurveyBinding binding; // 뷰 바인딩 객체
    private View selectedCountButton = null; // 현재 선택된 버튼을 저장
    private View selectedImportantceButton = null; // 현재 선택된 버튼을 저장
    private View selectedLengthButton = null; // 현재 선택된 버튼을 저장
    private SurveyKeywordAdapter surveyKeywordAdapter;
    private FirebaseFirestore db;
    private List<CookieKeywordModel> dataModels;

    public static SurveyFragment newInstance(int movieId, String movieTitle) {
        SurveyFragment fragment = new SurveyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        args.putString(ARG_MOVIE_TITLE, movieTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setUpButtonListeners() {
        binding.zeroButton.setOnClickListener(this::onCountButtonClicked);
        binding.oneButton.setOnClickListener(this::onCountButtonClicked);
        binding.twoButton.setOnClickListener(this::onCountButtonClicked);
        binding.threeButton.setOnClickListener(this::onCountButtonClicked);

        binding.longButton.setOnClickListener(this::onLengthButtonClicked);
        binding.shortButton.setOnClickListener(this::onLengthButtonClicked);

        binding.importantButton.setOnClickListener(this::onImportantButtonClicked);
        binding.notImportantButton.setOnClickListener(this::onImportantButtonClicked);

        binding.cookieSurveyButton.setOnClickListener(view -> {
            if (getArguments() != null) {
                int movieId = getArguments().getInt(ARG_MOVIE_ID);
                String movieTitle = getArguments().getString(ARG_MOVIE_TITLE);
                updateAllSelectedData(movieId);
                saveKeywordDataToFirestore(movieId);
                saveWatchedMovie(movieId, movieTitle); // 사용자가 본 영화 추가
            }
        });
    }

    private void saveWatchedMovie(int movieId, String movieTitle) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            System.err.println("User ID is null. Cannot save watched movie.");
            return;
        }

        DocumentReference watchedMovieRef = db.collection("User")
                .document(userId)
                .collection("WatchedMovie")
                .document(String.valueOf(movieId));

        watchedMovieRef.set(new WatchedMovie(movieTitle))
                .addOnSuccessListener(aVoid -> System.out.println("Watched movie saved successfully: " + movieTitle))
                .addOnFailureListener(e -> System.err.println("Failed to save watched movie: " + e.getMessage()));
    }

    private void onCountButtonClicked(View clickedButton) {
        onButtonClicked(clickedButton, selectedCountButton, button -> selectedCountButton = button);
    }

    private void onLengthButtonClicked(View clickedButton) {
        onButtonClicked(clickedButton, selectedLengthButton, button -> selectedLengthButton = button);
    }

    private void onImportantButtonClicked(View clickedButton) {
        onButtonClicked(clickedButton, selectedImportantceButton, button -> selectedImportantceButton = button);
    }

    private void onButtonClicked(View clickedButton, View previousSelectedButton, Consumer<View> updateState) {
        if (previousSelectedButton != null) {
            resetButtonState(previousSelectedButton);
        }
        updateState.accept(clickedButton);
        setSelectedButtonState(clickedButton);
    }

    private void resetButtonState(View button) {
        button.setBackgroundTintList(null);
        button.setBackgroundResource(R.drawable.gray_button);
    }

    private void setSelectedButtonState(View button) {
        button.setBackgroundTintList(null);
        button.setBackgroundResource(R.drawable.green_button);
    }

    private void resetAllButtonsBackground() {
        resetButtonBackground(binding.zeroButton);
        resetButtonBackground(binding.oneButton);
        resetButtonBackground(binding.twoButton);
        resetButtonBackground(binding.threeButton);

        resetButtonBackground(binding.longButton);
        resetButtonBackground(binding.shortButton);

        resetButtonBackground(binding.importantButton);
        resetButtonBackground(binding.notImportantButton);
    }

    private void resetButtonBackground(View button) {
        button.setBackgroundResource(R.drawable.gray_button);
        button.setBackgroundTintList(null);
    }

    private void setAdapter() {
        dataModels = Arrays.asList(
                new CookieKeywordModel("엔딩 크레딧 이후 오래 기다렸어요", 0, false),
                new CookieKeywordModel("보길 잘했어요", 0, true),
                new CookieKeywordModel("다음 시리즈 떡밥이 포함되어 있어요", 0, true),
                new CookieKeywordModel("괜히 봤어요", 0, false),
                new CookieKeywordModel("엔딩 크레딧 직후 바로 나왔어요", 0, true),
                new CookieKeywordModel("쿠키 내용이 재밌어요", 0, true),
                new CookieKeywordModel("이스터에그가 포함되어 있어요", 0, true)
        );

        surveyKeywordAdapter = new SurveyKeywordAdapter(dataModels, fieldToUpdate -> {
            for (CookieKeywordModel model : dataModels) {
                if (model.getKeyword().equals(fieldToUpdate)) {
                    model.setCount(model.getCount() + 1);
                    break;
                }
            }
        });

        binding.keywordRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.keywordRecyclerView.setAdapter(surveyKeywordAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        binding = FragmentSurveyBinding.inflate(inflater, container, false);
        setUpButtonListeners();
        resetAllButtonsBackground();
        setAdapter();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addCookieSurveyData(Integer movieId, String fieldToUpdate) {
        db.runTransaction(transaction -> {
            DocumentReference docRef = db.collection("Cookie").document(movieId.toString());
            transaction.update(docRef, fieldToUpdate, FieldValue.increment(1));
            return null;
        });
    }

    private void updateAllSelectedData(Integer movieId) {
        if (movieId == null) return;

        if (selectedCountButton != null) {
            String countField = getFieldNameForCountButton(selectedCountButton);
            if (countField != null) {
                addCookieSurveyData(movieId, countField);
            }
        }

        if (selectedLengthButton != null) {
            String lengthField = getFieldNameForLengthButton(selectedLengthButton);
            if (lengthField != null) {
                addCookieSurveyData(movieId, lengthField);
            }
        }

        if (selectedImportantceButton != null) {
            String importanceField = getFieldNameForImportanceButton(selectedImportantceButton);
            if (importanceField != null) {
                addCookieSurveyData(movieId, importanceField);
            }
        }
    }

    private String getFieldNameForCountButton(View button) {
        if (button == binding.zeroButton) return "cookieZeroCount";
        if (button == binding.oneButton) return "cookieOneCount";
        if (button == binding.twoButton) return "cookieTwoCount";
        if (button == binding.threeButton) return "cookieThreeCount";
        return null;
    }

    private String getFieldNameForLengthButton(View button) {
        if (button == binding.longButton) return "cookieLongCount";
        if (button == binding.shortButton) return "cookieShortCount";
        return null;
    }

    private String getFieldNameForImportanceButton(View button) {
        if (button == binding.importantButton) return "cookieImportantCount";
        if (button == binding.notImportantButton) return "cookieNotImportantCount";
        return null;
    }

    private void saveKeywordDataToFirestore(Integer movieId) {
        if (movieId == null) return;

        for (CookieKeywordModel model : dataModels) {
            String documentId = getDocumentIdForKeyword(model.getKeyword());
            if (documentId != null && model.getCount() > 0) {
                db.collection("Cookie")
                        .document(movieId.toString())
                        .collection("Keyword")
                        .document(documentId)
                        .update("count", FieldValue.increment(model.getCount()));
            }
        }
    }

    private String getDocumentIdForKeyword(String keyword) {
        switch (keyword) {
            case "엔딩 크레딧 이후 오래 기다렸어요":
                return "cookieWaitLong";
            case "보길 잘했어요":
                return "cookieWorthWatching";
            case "다음 시리즈 떡밥이 포함되어 있어요":
                return "cookieNextSeries";
            case "괜히 봤어요":
                return "cookieRegret";
            case "엔딩 크레딧 직후 바로 나왔어요":
                return "cookieQuickExit";
            case "쿠키 내용이 재밌어요":
                return "cookieContentFun";
            case "이스터에그가 포함되어 있어요":
                return "cookieEasterEgg";
            default:
                return null;
        }
    }

    private static class WatchedMovie {
        private String title;

        public WatchedMovie(String title) {
            this.title = title;
        }

        public WatchedMovie() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
