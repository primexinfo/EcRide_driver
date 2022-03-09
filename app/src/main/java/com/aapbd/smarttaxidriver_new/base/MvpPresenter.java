package com.aapbd.smarttaxidriver_new.base;

import android.app.Activity;

import com.aapbd.smarttaxidriver_new.MvpApplication;

import java.util.HashMap;

public interface MvpPresenter<V extends MvpView> {

    Activity activity();

    MvpApplication appContext();

    void attachView(V mvpView);

    void detachView();

    void refreshToken();

    void logout(HashMap<String, Object> obj);

}
