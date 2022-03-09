package com.aapbd.smarttaxidriver_new.ui.activity.change_password;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

import java.util.HashMap;

public interface ChangePasswordIPresenter<V extends ChangePasswordIView> extends MvpPresenter<V> {

    void changePassword(HashMap<String, Object> obj);
}
