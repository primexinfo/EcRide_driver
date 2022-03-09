package com.aapbd.smarttaxidriver_new.ui.activity.card;

import com.aapbd.smarttaxidriver_new.base.MvpView;
import com.aapbd.smarttaxidriver_new.data.network.model.Card;

import java.util.List;

public interface CardIView extends MvpView {

    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onError(Throwable e);

    void onSuccessChangeCard(Object card);
}
