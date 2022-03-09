package com.aapbd.smarttaxidriver_new.ui.activity.help;

import com.aapbd.smarttaxidriver_new.base.MvpView;
import com.aapbd.smarttaxidriver_new.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help object);

    void onError(Throwable e);
}
