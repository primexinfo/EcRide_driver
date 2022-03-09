package com.aapbd.smarttaxidriver_new.ui.activity.past_detail;


import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface PastTripDetailIPresenter<V extends PastTripDetailIView> extends MvpPresenter<V> {

    void getPastTripDetail(String request_id);
}
