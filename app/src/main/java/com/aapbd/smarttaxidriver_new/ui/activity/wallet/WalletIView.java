package com.aapbd.smarttaxidriver_new.ui.activity.wallet;

import com.aapbd.smarttaxidriver_new.base.MvpView;
import com.aapbd.smarttaxidriver_new.data.network.model.WalletMoneyAddedResponse;
import com.aapbd.smarttaxidriver_new.data.network.model.WalletResponse;

public interface WalletIView extends MvpView {

    void onSuccess(WalletResponse response);

    void onSuccess(WalletMoneyAddedResponse response);

    void onError(Throwable e);
}
