package com.aapbd.smarttaxidriver_new.ui.activity.summary;


import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface SummaryIPresenter<V extends SummaryIView> extends MvpPresenter<V> {

    void getSummary();
}
