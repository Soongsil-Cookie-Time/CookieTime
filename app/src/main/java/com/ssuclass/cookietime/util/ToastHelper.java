package com.ssuclass.cookietime.util;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    private static Toast toast;

    /**
     * 토스트 메시지를 띄워주는 헬퍼 클래스입니다.
     *
     * @param context 어떤 context에서 실행하는 것인지 명시
     * @param message 토스트 메시지
     */
    public static void showToast(Context context, String message) {
        if (toast != null) {
            toast.cancel(); // 기존 Toast 제거
        }
        toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
