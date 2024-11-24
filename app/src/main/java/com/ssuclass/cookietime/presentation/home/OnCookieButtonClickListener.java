package com.ssuclass.cookietime.presentation.home;

import com.ssuclass.cookietime.network.response.TMDBNowPlayingResponse;

public interface OnCookieButtonClickListener {
    void onCookieButtonClick(TMDBNowPlayingResponse.Movie dataModel);
}
