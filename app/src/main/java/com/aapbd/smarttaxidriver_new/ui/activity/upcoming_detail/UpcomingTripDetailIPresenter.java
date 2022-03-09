package com.aapbd.smarttaxidriver_new.ui.activity.upcoming_detail;


import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface UpcomingTripDetailIPresenter<V extends UpcomingTripDetailIView> extends MvpPresenter<V> {

    void getUpcomingDetail(String request_id);

}
