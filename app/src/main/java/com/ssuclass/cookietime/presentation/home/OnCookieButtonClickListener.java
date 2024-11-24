package com.ssuclass.cookietime.presentation.home;

import com.ssuclass.cookietime.network.response.KOBISBoxOfficeResponse;

public interface OnCookieButtonClickListener {
    void onCookieButtonClick(KOBISBoxOfficeResponse.DailyBoxOffice dataModel);
}
