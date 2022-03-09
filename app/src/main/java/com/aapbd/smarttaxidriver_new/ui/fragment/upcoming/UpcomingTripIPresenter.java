package com.aapbd.smarttaxidriver_new.ui.fragment.upcoming;


import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface UpcomingTripIPresenter<V extends UpcomingTripIView> extends MvpPresenter<V> {

    void getUpcoming();

}
