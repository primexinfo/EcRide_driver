package com.aapbd.smarttaxidriver_new.base;

import android.app.Activity;

import com.aapbd.smarttaxidriver_new.data.network.model.User;

public interface MvpView {
    Activity activity();

    void showLoading();

    void hideLoading();

    void onErrorRefreshToken(Throwable throwable);

    void onSuccessRefreshToken(User user);

    void onSuccessLogout(Object object);

    void onError(Throwable throwable);
}
