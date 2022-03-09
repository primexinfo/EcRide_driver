package com.aapbd.smarttaxidriver_new.ui.activity.reset_password;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.ui.activity.PhoneModule.PhoneNumberActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ResetActivity extends BaseActivity implements ResetIView {

    @BindView(R.id.txtNewPassword)
    EditText txtNewPassword;
    @BindView(R.id.txtPassword)
    EditText txtPassword;

    ResetPresenter presenter;

    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;


    @Override
    public int getLayoutId() {
        return R.layout.activity_reset;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter = new ResetPresenter();
        presenter.attachView(this);

        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.reset_password));

    }

    @OnClick({R.id.btnDone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                if (txtPassword.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_old_password), Toast.LENGTH_SHORT, true).show();
                } else if (txtNewPassword.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_new_password), Toast.LENGTH_SHORT, true).show();
                } else if (!txtPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
                    Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
                } else {
                    showLoading();

                    HashMap<String, Object> map = new HashMap<>();

                    map.put("mobile", getIntent().getStringExtra(AppConstant.PHONENUMBER) + "");
                    map.put("country_code", getIntent().getStringExtra(AppConstant.COUNTRYCODE) + "");
                    map.put("password", txtNewPassword.getText().toString());
                    map.put("password_confirmation", txtNewPassword.getText().toString());
                    presenter.reset(map);
                }
                break;
        }
    }


    @Override
    public void onSuccess(Object object) {
        hideLoading();
        Toasty.success(this, getString(R.string.password_updated), Toast.LENGTH_SHORT, true).show();
        Intent goToLogin = new Intent(activity(), PhoneNumberActivity.class);
        goToLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity().startActivity(goToLogin);
        activity().finish();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }
}
