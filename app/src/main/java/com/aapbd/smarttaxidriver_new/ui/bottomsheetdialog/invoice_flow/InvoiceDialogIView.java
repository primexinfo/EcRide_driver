package com.aapbd.smarttaxidriver_new.ui.bottomsheetdialog.invoice_flow;

import com.aapbd.smarttaxidriver_new.base.MvpView;

public interface InvoiceDialogIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
