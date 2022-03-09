package com.aapbd.smarttaxidriver_new.ui.activity.main;

import com.aapbd.smarttaxidriver_new.base.MvpView;
import com.aapbd.smarttaxidriver_new.data.network.model.Provider;
import com.aapbd.smarttaxidriver_new.data.network.model.SettingsResponse;
import com.aapbd.smarttaxidriver_new.data.network.model.TripResponse;
import com.aapbd.smarttaxidriver_new.data.network.model.UserResponse;

import java.util.List;

public interface MainIView extends MvpView {
    void onSuccess(UserResponse user);

    void onError(Throwable e);

    void onSuccessLogout(Object object);

    void onSuccess(TripResponse tripResponse);

    void onSuccess(SettingsResponse response);
    void onSuccessProvider(List<Provider> objects);
    void onErrorProvider(Throwable e);
    void onSettingError(Throwable e);

    void onSuccessProviderAvailable(Object object);

    void onSuccessFCM(Object object);

    void onSuccessLocationUpdate(TripResponse tripResponse);

}
