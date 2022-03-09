package com.aapbd.smarttaxidriver_new.ui.activity.instant_ride;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

import java.util.Map;

public interface InstantRideIPresenter<V extends InstantRideIView> extends MvpPresenter<V> {

    void estimateFare(Map<String, Object> params);

    void requestInstantRide(Map<String, Object> params);

}
