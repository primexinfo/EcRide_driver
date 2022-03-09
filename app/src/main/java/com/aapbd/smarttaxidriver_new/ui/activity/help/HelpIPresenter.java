package com.aapbd.smarttaxidriver_new.ui.activity.help;


import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface HelpIPresenter<V extends HelpIView> extends MvpPresenter<V> {

    void getHelp();
}
