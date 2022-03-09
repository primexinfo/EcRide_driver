package com.aapbd.smarttaxidriver_new.ui.activity.setting;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface SettingsIPresenter<V extends SettingsIView> extends MvpPresenter<V> {
    void changeLanguage(String languageID);
}
