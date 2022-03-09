package com.aapbd.smarttaxidriver_new.ui.activity.add_card;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface AddCardIPresenter<V extends AddCardIView> extends MvpPresenter<V> {

    void addCard(String stripeToken);
}
