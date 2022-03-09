package com.aapbd.smarttaxidriver_new.ui.activity.wallet;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

import java.util.HashMap;

public interface WalletIPresenter<V extends WalletIView> extends MvpPresenter<V> {

    void getWalletData();
    void addMoney(HashMap<String, Object> obj);
    void addSSLMoney(HashMap<String, Object> obj);
}
