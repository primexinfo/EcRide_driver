package com.aapbd.smarttaxidriver_new.ui.activity.sociallogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.aapbd.smarttaxidriver_new.BuildConfig;
import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.data.network.model.Token;
import com.aapbd.smarttaxidriver_new.ui.activity.main.MainActivity;
import com.aapbd.smarttaxidriver_new.ui.countrypicker.Country;
import com.facebook.CallbackManager;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocialLoginActivity extends BaseActivity implements SocialLoginIView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.lblFacebook)
    TextView lblFacebook;
    @BindView(R.id.lblGoogle)
    TextView lblGoogle;

    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    private String country_dial_code = AppConstant.DEFAULT_COUNTRY_DIAL_CODE;
    private String country_code = AppConstant.DEFAULT_COUNTRY_CODE;

    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private SocialLoginPresenter<SocialLoginActivity> presenter = new SocialLoginPresenter<>();
    private CallbackManager callbackManager;
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_social_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);



        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(R.string.social_login);

        callbackManager = CallbackManager.Factory.create();
        presenter.attachView(this);
        map.put("device_token", SharedHelper.getKeyFCM(this, AppConstant.SharedPref.DEVICE_TOKEN));
        map.put("device_id", SharedHelper.getKeyFCM(this, AppConstant.SharedPref.DEVICE_ID));
        map.put("device_type", BuildConfig.DEVICE_TYPE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.google_signin_server_client_id)).
                requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        getUserCountryInfo();

    }

    @OnClick({R.id.lblFacebook, R.id.lblGoogle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lblFacebook:

                break;
            case R.id.lblGoogle:
                showLoading();
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
                break;
        }
    }

    private void getUserCountryInfo() {
        Country country = getDeviceCountry(SocialLoginActivity.this);
        country_dial_code = country.getDialCode();
        country_code = country.getCode();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            hideLoading();
            String TAG = "Google";
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                map.put("login_by", AppConstant.Login.GOOGLE);
                Runnable runnable = () -> {
                    try {
                        String scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE;
                        String accessToken = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount(), scope, new Bundle());
                        Log.d(TAG, "accessToken:" + accessToken);
                        map.put("accessToken", accessToken);

                    } catch (IOException | GoogleAuthException e) {
                        e.printStackTrace();
                    }
                };
                AsyncTask.execute(runnable);

            } catch (ApiException e) {
                Log.w(TAG, "signInResult : failed code = " + e.getStatusCode());
            }
        }
    }

    private void register() {
        map.put("mobile", SharedHelper.getKey(this, AppConstant.SharedPref.MOBILE));
        map.put("country_code", SharedHelper.getKey(this, AppConstant.SharedPref.DIAL_CODE));
        if (map.get("login_by").equals("google")) presenter.loginGoogle(map);
        else if (map.get("login_by").equals("facebook")) presenter.loginFacebook(map);
        System.out.println("RRR map = " + map);
        showLoading();
    }

    @Override
    public void onSuccess(Token token) {
        hideLoading();
        String accessToken = token.getTokenType() + " " + token.getAccessToken();
        SharedHelper.putKey(this, AppConstant.SharedPref.ACCESS_TOKEN, accessToken);
        SharedHelper.putKey(this, AppConstant.SharedPref.LOGGGED_IN, "true");
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

}
