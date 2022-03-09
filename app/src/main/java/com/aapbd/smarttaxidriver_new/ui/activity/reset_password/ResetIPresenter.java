package com.aapbd.smarttaxidriver_new.ui.activity.reset_password;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

import java.util.HashMap;

public interface ResetIPresenter<V extends ResetIView> extends MvpPresenter<V> {

    void reset(HashMap<String, Object> obj);

}
