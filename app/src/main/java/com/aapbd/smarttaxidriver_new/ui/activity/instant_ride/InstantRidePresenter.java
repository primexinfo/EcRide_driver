package com.aapbd.smarttaxidriver_new.ui.activity.instant_ride;

import com.aapbd.smarttaxidriver_new.base.BasePresenter;
import com.aapbd.smarttaxidriver_new.data.network.APIClient;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InstantRidePresenter<V extends InstantRideIView> extends BasePresenter<V> implements InstantRideIPresenter<V> {

    @Override
    public void estimateFare(Map<String, Object> params) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .estimateFare(params)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void requestInstantRide(Map<String, Object> params) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .requestInstantRide(params)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

}
