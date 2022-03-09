package com.aapbd.smarttaxidriver_new.ui.fragment.past;


import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface PastTripIPresenter<V extends PastTripIView> extends MvpPresenter<V> {

    void getHistory();

}
