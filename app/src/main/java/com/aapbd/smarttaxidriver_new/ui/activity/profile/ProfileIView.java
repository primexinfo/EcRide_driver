package com.aapbd.smarttaxidriver_new.ui.activity.profile;

import com.aapbd.smarttaxidriver_new.base.MvpView;
import com.aapbd.smarttaxidriver_new.data.network.model.UserResponse;

public interface ProfileIView extends MvpView {

    void onSuccess(UserResponse user);

    void onSuccessUpdate(UserResponse object);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);

}
