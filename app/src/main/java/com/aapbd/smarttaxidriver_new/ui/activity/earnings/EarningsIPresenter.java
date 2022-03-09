package com.aapbd.smarttaxidriver_new.ui.activity.earnings;


import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface EarningsIPresenter<V extends EarningsIView> extends MvpPresenter<V> {

    void getEarnings();
}
