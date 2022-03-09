package com.aapbd.smarttaxidriver_new.ui.activity.add_card;

import com.aapbd.smarttaxidriver_new.base.MvpView;

public interface AddCardIView extends MvpView {

    void onSuccess(Object card);

    void onError(Throwable e);
}
