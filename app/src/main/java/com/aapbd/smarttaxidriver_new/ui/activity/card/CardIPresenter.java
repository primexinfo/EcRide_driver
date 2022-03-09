package com.aapbd.smarttaxidriver_new.ui.activity.card;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface CardIPresenter<V extends CardIView> extends MvpPresenter<V> {

    void deleteCard(String cardId);

    void card();

    void changeCard(String cardId);
}
