package com.aapbd.smarttaxidriver_new.ui.activity.payment;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface PaymentIPresenter<V extends PaymentIView> extends MvpPresenter<V> {
    void deleteCard(String cardId);

    void card();
    void addCard(String cardId);
}
