package com.aapbd.smarttaxidriver_new.ui.fragment.incoming_request;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface IncomingRequestIPresenter<V extends IncomingRequestIView> extends MvpPresenter<V> {

    void accept(Integer id);
    void cancel(Integer id);
}
