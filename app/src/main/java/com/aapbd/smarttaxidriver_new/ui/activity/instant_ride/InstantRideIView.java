package com.aapbd.smarttaxidriver_new.ui.activity.instant_ride;

import com.aapbd.smarttaxidriver_new.base.MvpView;
import com.aapbd.smarttaxidriver_new.data.network.model.EstimateFare;
import com.aapbd.smarttaxidriver_new.data.network.model.TripResponse;

public interface InstantRideIView extends MvpView {

    void onSuccess(EstimateFare estimateFare);

    void onSuccess(TripResponse response);

    void onError(Throwable e);

}
