package com.aapbd.smarttaxidriver_new.ui.fragment.incoming_request;

import com.aapbd.smarttaxidriver_new.base.MvpView;

public interface IncomingRequestIView extends MvpView {

    void onSuccessAccept(Object responseBody);
    void onSuccessCancel(Object object);
    void onError(Throwable e);
}
