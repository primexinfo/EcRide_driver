package com.aapbd.smarttaxidriver_new.ui.activity.card;

import com.aapbd.smarttaxidriver_new.base.BasePresenter;
import com.aapbd.smarttaxidriver_new.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CardPresenter<V extends CardIView> extends BasePresenter<V> implements CardIPresenter<V> {

    @Override
    public void deleteCard(String cardId) {

        getCompositeDisposable().add(APIClient.getAPIClient().deleteCard(cardId, "DELETE")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(deleteResponse -> getMvpView().onSuccess(deleteResponse),
                        throwable -> getMvpView().onError(throwable)));
    }

    @Override
    public void card() {

        getCompositeDisposable().add(APIClient.getAPIClient().card()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(cards -> CardPresenter.this.getMvpView().onSuccess(cards),
                        throwable -> CardPresenter.this.getMvpView().onError(throwable)));
    }

    @Override
    public void changeCard(String cardId) {

        getCompositeDisposable().add(APIClient.getAPIClient().changeCard(cardId, "PUT")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(changeCard -> getMvpView().onSuccessChangeCard(changeCard),
                        throwable -> getMvpView().onError(throwable)));
    }

}
