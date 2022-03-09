package com.aapbd.smarttaxidriver_new.ui.activity.past_detail;


import com.aapbd.smarttaxidriver_new.base.MvpView;
import com.aapbd.smarttaxidriver_new.data.network.model.HistoryDetail;

public interface PastTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);
    void onError(Throwable e);
}
