package com.aapbd.smarttaxidriver_new.ui.activity.password;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.aapbd.smarttaxidriver_new.ui.activity.phone_verification.EnterPhoneActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.aapbd.smarttaxidriver_new.BuildConfig;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.aapbd.smarttaxidriver_new.common.AppConstant;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.common.Utilities;
import com.aapbd.smarttaxidriver_new.data.network.model.ForgotResponse;
import com.aapbd.smarttaxidriver_new.data.network.model.User;
import com.aapbd.smarttaxidriver_new.ui.activity.main.MainActivity;
import com.aapbd.smarttaxidriver_new.ui.activity.reset_password.ResetActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PasswordActivity extends BaseActivity implements PasswordIView {

    PasswordPresenter presenter = new PasswordPresenter();

    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.next)
    FloatingActionButton next;
    String phoneNumber = "";
   String countrycode = "";
    public static String TAG = "";

    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;


    @Override
    public int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.password));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString(AppConstant.SharedPref.PHONENUMBER);
            countrycode=extras.getString(AppConstant.SharedPref.COUNTRY_CODE);
            Utilities.printV("phoneNumber===>", phoneNumber);

        }
        if (BuildConfig.DEBUG) password.setText("123456");




        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DEVICE_ID: ", deviceId);
        SharedHelper.putKeyFCM(this, AppConstant.SharedPref.DEVICE_ID, deviceId);


    }

    @OnClick({R.id.sign_up, R.id.forgot_password, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up:

                navigateToPhone(false);

                break;
            case R.id.forgot_password:
                navigateToPhone(true);

                break;
            case R.id.next:
                login();
                break;
            default:
                break;
        }
    }



    private void navigateToPhone(boolean fromForgotPass) {

        AppConstant.isFromForgot = fromForgotPass;

        Intent i = new Intent(this, EnterPhoneActivity.class);
        i.putExtra(AppConstant.SharedPref.PHONENUMBER, phoneNumber);
        i.putExtra(AppConstant.SharedPref.COUNTRY_CODE, countrycode);
        startActivity(i);



    }


    private void login() {
        if (password.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (phoneNumber.isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return;
        }

        deviceToken = SharedHelper.getKeyFCM(this, AppConstant.SharedPref.DEVICE_TOKEN);
        deviceId = SharedHelper.getKeyFCM(this, AppConstant.SharedPref.DEVICE_ID);



        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SharedHelper.putKeyFCM(this, AppConstant.SharedPref.DEVICE_ID, deviceId);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", phoneNumber);
        map.put("password", password.getText().toString());
        map.put("device_id", deviceId);
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("device_token", deviceToken);
        map.put("country_code", countrycode);
        System.out.println("login_data" + map);
        presenter.login(map);
        showLoading();
    }


    /*

    not using it
     */
    @Override
    public void onSuccess(ForgotResponse forgotResponse) {

        hideLoading();

    }

    @Override
    public void onSuccess(User user) {


        hideLoading();
        if (!TextUtils.isEmpty(user.getError())) {
            Toasty.success(activity(), user.getError(), Toast.LENGTH_SHORT).show();
        } else {
            SharedHelper.putKey(this, AppConstant.SharedPref.ACCESS_TOKEN, user.getAccessToken());
            SharedHelper.putKey(this, AppConstant.COUNTRY_CODE, user.getCountryCode());
            SharedHelper.putKey(this, AppConstant.PHONENUMBER, user.getMobile());

            SharedHelper.putKey(this, AppConstant.SharedPref.USER_ID,
                    String.valueOf(user.getId()));
            SharedHelper.putKey(this, AppConstant.SharedPref.LOGGGED_IN, "true");

            Toasty.success(activity(), getString(R.string.login_out_success), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            TAG = "PasswordActivity";
        onErrorBase(e);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


}
