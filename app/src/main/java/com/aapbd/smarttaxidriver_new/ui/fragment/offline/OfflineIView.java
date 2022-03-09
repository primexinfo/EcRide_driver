package com.aapbd.smarttaxidriver_new.ui.fragment.offline;

import com.aapbd.smarttaxidriver_new.base.MvpView;

public interface OfflineIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
