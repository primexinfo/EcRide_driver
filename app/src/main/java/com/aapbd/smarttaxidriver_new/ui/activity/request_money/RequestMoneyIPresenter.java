package com.aapbd.smarttaxidriver_new.ui.activity.request_money;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface RequestMoneyIPresenter<V extends RequestMoneyIView> extends MvpPresenter<V> {

    void getRequestedData();
    void requestMoney(Double requestedAmt);
    void removeRequestMoney(int id);

}
