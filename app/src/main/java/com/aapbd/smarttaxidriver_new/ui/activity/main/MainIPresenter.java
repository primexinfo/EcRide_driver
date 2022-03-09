package com.aapbd.smarttaxidriver_new.ui.activity.main;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

import java.util.HashMap;

public interface MainIPresenter<V extends MainIView> extends MvpPresenter<V> {

    void getProfile();

    void logout(HashMap<String, Object> obj);

    void getTrip(HashMap<String, Object> params);

    void providerAvailable(HashMap<String, Object> obj);

//    void sendFCM(JsonObject jsonObject);
    void getProviders(HashMap<String, Object> params);

    void getTripLocationUpdate(HashMap<String, Object> params);

    void getSettings();


}
