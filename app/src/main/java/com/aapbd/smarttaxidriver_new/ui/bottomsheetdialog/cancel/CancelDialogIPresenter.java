package com.aapbd.smarttaxidriver_new.ui.bottomsheetdialog.cancel;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

import java.util.HashMap;

public interface CancelDialogIPresenter<V extends CancelDialogIView> extends MvpPresenter<V> {

    void cancelRequest(HashMap<String, Object> obj);
    void getReasons();
}
