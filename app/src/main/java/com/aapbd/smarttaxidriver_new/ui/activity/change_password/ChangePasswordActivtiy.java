package com.aapbd.smarttaxidriver_new.ui.activity.change_password;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.common.Utilities;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ChangePasswordActivtiy extends BaseActivity
        implements ChangePasswordIView {

    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    ChangePasswordPresenter presenter = new ChangePasswordPresenter();
    @BindView(R.id.txtCurrentPassword)
    EditText txtCurrentPassword;
    @BindView(R.id.txtNewPassword)
    EditText txtNewPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;
    @BindView(R.id.btnChangePassword)
    Button btnChangePassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.change_password));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.btnChangePassword)
    public void onViewClicked() {

        if (txtCurrentPassword.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.curr_pwd_val), Toast.LENGTH_SHORT, true).show();
        } else if (txtNewPassword.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_new_password), Toast.LENGTH_SHORT, true).show();
        } else if (txtConfirmPassword.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.confirm_pwd_val), Toast.LENGTH_SHORT, true).show();
        } else if (!txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
        } else {
            showLoading();
            HashMap<String, Object> map = new HashMap<>();
            map.put("mobile", SharedHelper.getKey(this, AppConstant.PHONENUMBER));
            map.put("country_code", SharedHelper.getKey(this, AppConstant.COUNTRY_CODE));
            map.put("password_old", txtCurrentPassword.getText().toString());
            map.put("password", txtNewPassword.getText().toString());
            map.put("password_confirmation", txtConfirmPassword.getText().toString());
            presenter.changePassword(map);
        }
    }

    @Override
    public void onSuccess(Object object) {
        hideLoading();
        Toasty.success(this, getString(R.string.pass_updated), Toast.LENGTH_SHORT, true).show();
        Utilities.LogoutApp(activity());
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if(e!= null)
        onErrorBase(e);
    }
}
