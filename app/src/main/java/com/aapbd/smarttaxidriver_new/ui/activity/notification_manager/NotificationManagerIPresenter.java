package com.aapbd.smarttaxidriver_new.ui.activity.notification_manager;

import com.aapbd.smarttaxidriver_new.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
