package com.aapbd.smarttaxidriver_new.ui.activity.splash;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

import java.util.HashMap;

public interface SplashIPresenter<V extends SplashIView> extends MvpPresenter<V> {

    void handlerCall();

    //void getPlaces();

    void checkVersion(HashMap<String, Object> map);

}
