package com.aapbd.smarttaxidriver_new.ui.activity.wallet_detail;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;
import com.aapbd.smarttaxidriver_new.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIPresenter<V extends WalletDetailIView> extends MvpPresenter<V> {
    void setAdapter(ArrayList<Transaction> myList);
}
