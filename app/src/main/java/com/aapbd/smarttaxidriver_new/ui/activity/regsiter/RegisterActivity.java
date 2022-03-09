package com.aapbd.smarttaxidriver_new.ui.activity.regsiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;

import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.aapbd.smarttaxidriver_new.common.AppConstant;

import com.aapbd.smarttaxidriver_new.BuildConfig;
import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.common.Utilities;
import com.aapbd.smarttaxidriver_new.data.network.model.ServiceType;
import com.aapbd.smarttaxidriver_new.data.network.model.SettingsResponse;
import com.aapbd.smarttaxidriver_new.data.network.model.User;
import com.aapbd.smarttaxidriver_new.ui.activity.main.MainActivity;
import com.aapbd.smarttaxidriver_new.ui.activity.profile.ProfileActivity;
import com.aapbd.smarttaxidriver_new.ui.countrypicker.Country;
import com.aapbd.smarttaxidriver_new.ui.countrypicker.CountryPicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aapbd.smarttaxidriver_new.common.AppConstant.MULTIPLE_PERMISSION;
import static com.aapbd.smarttaxidriver_new.common.AppConstant.RC_MULTIPLE_PERMISSION_CODE;

public class RegisterActivity extends BaseActivity implements RegisterIView {

//    @BindView(R.id.title)
//    TextView title;
 @BindView(R.id.imgProfileReg)
 CircleImageView imgProfile;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;
    @BindView(R.id.chkTerms)
    CheckBox chkTerms;
    @BindView(R.id.spinnerServiceType)
    AppCompatSpinner spinnerServiceType;
    @BindView(R.id.txtVehicleModel)
    EditText txtVehicleModel;
    @BindView(R.id.txtVehicleNumber)
    EditText txtVehicleNumber;
    @BindView(R.id.lnrReferralCode)
    LinearLayout lnrReferralCode;
    @BindView(R.id.txtReferalCode)
    EditText txtReferalCode;
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;
    @BindView(R.id.phoneNumber)
    EditText phoneNumber;

    @BindView(R.id.lblTerms)
    TextView termandconditionandpircay;
    boolean isImageAdded = false;

    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private File imgFile = null;


    private String countryDialCode = "+880";
    private String countryCode = "BD";
    private CountryPicker mCountryPicker;

    private RegisterPresenter presenter;
    private int selected_pos = -1;
    private List<ServiceType> lstServiceTypes = new ArrayList<>();

    private boolean isEmailAvailable = true;
    private boolean isPhoneNumberAvailable = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter = new RegisterPresenter();
        presenter.attachView(this);
        setupSpinner(null);
        presenter.getSettings();


        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.sign_up));
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiplePermissionTask();
            }
        });
        spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clickFunctions();


        try {

            //  phoneNumberExceptCode=getIntent().getStringExtra(AppConstant.PHONENUMBER);
            countryDialCode = getIntent().getStringExtra(AppConstant.COUNTRYCODE);

            //  phoneNumberView.setText(phoneNumberExceptCode);
            countryNumber.setText(countryDialCode);

            countryImage.setImageResource(Country.getCountryImageByDialCode(countryDialCode));
            phoneNumber.setText(getIntent().getStringExtra(AppConstant.PHONENUMBER));
            phoneNumber.setEnabled(false);
            countryNumber.setEnabled(false);

            System.out.println("phoneNumber: Register" + getIntent().getStringExtra(AppConstant.PHONENUMBER));

        } catch (Exception e) {

        }



        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DEVICE_ID: ", deviceId);
        SharedHelper.putKeyFCM(RegisterActivity.this, AppConstant.SharedPref.DEVICE_ID, deviceId);

      //  termandconditionandpircay

        Spannable span = Spannable.Factory.getInstance().newSpannable(getResources().getString(R.string.i_have_read_and_agreed_the_n_terms_and_conditions));
        int length=span.length();
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                Log.d("termsandco", "terms con clicked");
               // Toast.makeText(RegisterActivity.this, "link clicked", Toast.LENGTH_SHORT).show();
                showTermsConditionsDialog();

            } }, 29, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// All the rest will have the same spannable.
        ClickableSpan cs = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                Log.d("main", "textview clicked");
               // Toast.makeText(RegisterActivity.this, "textview clicked", Toast.LENGTH_SHORT).show();
               showPrivacipolicy();

            } };

// set the "test " spannable.
        span.setSpan(cs, 0, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// set the " span" spannable
        span.setSpan(cs, 27, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termandconditionandpircay.setText(span);

        termandconditionandpircay.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private boolean validation() {
        String Email = txtEmail.getText().toString();
        if (txtEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtFirstName.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_first_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtLastName.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (selected_pos == -1) {
            Toasty.error(this, getString(R.string.invalid_service_type), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtVehicleModel.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_model), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtVehicleNumber.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_number), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (phoneNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtPassword.getText().toString().length() < 6) {
            Toasty.error(this, getString(R.string.invalid_password_length), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtConfirmPassword.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_confirm_password), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!chkTerms.isChecked()) {
            Toasty.error(this, getString(R.string.please_accept_terms_conditions), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (isEmailAvailable) {
            showErrorMessage(txtEmail, getString(R.string.email_already_exist));
            return false;
        }else if (isImageAdded ==false) {
            Toasty.error(this,getString(R.string.profilepicvali), Toast.LENGTH_SHORT, true).show();
            return false;
        } else return true;
    }

    private void showErrorMessage(EditText view, String message) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show();
        view.requestFocus();
        view.setText(null);
    }

    // issue in registration
    private void register() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("first_name", toRequestBody(txtFirstName.getText().toString()));
        map.put("last_name", toRequestBody(txtLastName.getText().toString()));
        map.put("email", toRequestBody(txtEmail.getText().toString()));
        map.put("mobile", toRequestBody(getIntent().getStringExtra(AppConstant.PHONENUMBER)));
        map.put("country_code", toRequestBody(countryDialCode));
        map.put("password", toRequestBody(txtPassword.getText().toString()));
        map.put("password_confirmation", toRequestBody(txtConfirmPassword.getText().toString()));
        map.put("device_token", toRequestBody(SharedHelper.getKeyFCM(this, AppConstant.SharedPref.DEVICE_TOKEN)));
        map.put("device_id", toRequestBody(SharedHelper.getKeyFCM(this, AppConstant.SharedPref.DEVICE_ID)));
        map.put("service_type", toRequestBody(lstServiceTypes.get(selected_pos).getId() + ""));
        map.put("service_model", toRequestBody(txtVehicleModel.getText().toString()));
        map.put("service_number", toRequestBody(txtVehicleNumber.getText().toString()));
        map.put("device_type", toRequestBody(BuildConfig.DEVICE_TYPE));
        map.put("referral_code", toRequestBody(txtReferalCode.getText().toString()));
        Log.d("referelid", txtReferalCode.getText().toString());
        //todo:In api it is list of maltipart
       // List<MultipartBody.Part> parts = new ArrayList<>();
        //todo:for profile pic upload
        //todo:In profile update it is just single multipart file
        MultipartBody.Part filePart = null;
        if (imgFile != null)
            try {
                File compressedImageFile = new Compressor(this).compressToFile(imgFile);
                filePart = MultipartBody.Part.createFormData("avatar", compressedImageFile.getName(),
                        RequestBody.create(MediaType.parse("image/*"), compressedImageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

        Utilities.printV("Params ===> 2", map.toString());

        showLoading();
        System.out.println("mapdata" + map);
        presenter.register(map, filePart);
    }

    @OnClick({R.id.next, R.id.lblTerms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:
                if (validation()) if (Utilities.isConnected())

                    register();
                    //    fbPhoneLogin(countryCode, countryDialCode, phoneNumber.getText().toString());
                else showAToast(getString(R.string.no_internet_connection));
                break;
          /*  case R.id.lblTerms:
                showTermsConditionsDialog();
                break;*/
        }
    }

    private void clickFunctions() {
        txtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            isEmailAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(txtEmail.getText().toString()))
                if (Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches())
                    presenter.verifyEmail(txtEmail.getText().toString().trim());
        });

        phoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            isPhoneNumberAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(phoneNumber.getText().toString()))
                presenter.verifyCredentials(countryDialCode, phoneNumber.getText().toString());
        });
    }

    private void showTermsConditionsDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.terms_and_conditions));
        WebView wv = new WebView(this);
        wv.loadUrl(BuildConfig.TERMS_CONDITIONS);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Close", (dialog, id) -> dialog.dismiss());
        alert.show();
    }
    private void showPrivacipolicy(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getText(R.string.privacy_policy));
        WebView wv = new WebView(this);
        wv.loadUrl(BuildConfig.BASE_URL+"privacy");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Close", (dialog, id) -> dialog.dismiss());
        alert.show();
    }

    @Override
    public void onSuccess(User user) {
        hideLoading();
        SharedHelper.putKey(this, AppConstant.SharedPref.USER_ID, String.valueOf(user.getId()));
        SharedHelper.putKey(this, AppConstant.SharedPref.ACCESS_TOKEN, user.getAccessToken());
        SharedHelper.putKey(this, AppConstant.COUNTRY_CODE, user.getCountryCode());
        SharedHelper.putKey(this, AppConstant.PHONENUMBER, user.getMobile());


        SharedHelper.putKey(this, AppConstant.SharedPref.LOGGGED_IN, "true");
        Toasty.success(this, getString(R.string.register_success), Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSuccess(Object verifyEmail) {
        hideLoading();
        isEmailAvailable = false;
    }

    @Override
    public void onVerifyEmailError(Throwable e) {
        isEmailAvailable = true;
        showErrorMessage(txtEmail, getString(R.string.email_already_exist));
    }

    @Override
    public void onSuccess(SettingsResponse response) {
        lstServiceTypes = response.getServiceTypes();
        lnrReferralCode.setVisibility(response.getReferral().getReferral().equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);
        setupSpinner(response);
    }

    private void setupSpinner(@Nullable SettingsResponse response) {
        ArrayList<String> lstNames = new ArrayList<>(response != null ? response.getServiceTypes().size() : 0);
        if (response != null) for (ServiceType serviceType : response.getServiceTypes())
            lstNames.add(serviceType.getName());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lstNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(dataAdapter);
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, RegisterActivity.this,
                new DefaultCallback() {
                    @Override
                    public void onImagesPicked(@NonNull List<File> imageFiles,
                                               EasyImage.ImageSource source, int type) {
                        imgFile = imageFiles.get(0);
                        isImageAdded = true;
                        Glide.with(activity())
                                .load(Uri.fromFile(imgFile))
                                .apply(RequestOptions
                                        .placeholderOf(R.drawable.ic_user_placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.ic_user_placeholder))
                                .into(imgProfile);
                    }
                });
    }
    private boolean hasMultiplePermission() {
        return EasyPermissions.hasPermissions(this, MULTIPLE_PERMISSION);
    }

    @AfterPermissionGranted(RC_MULTIPLE_PERMISSION_CODE)
    void MultiplePermissionTask() {
        if (hasMultiplePermission()) pickImage();
        else EasyPermissions.requestPermissions(
                this, getString(R.string.please_accept_permission),
                RC_MULTIPLE_PERMISSION_CODE,
                MULTIPLE_PERMISSION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onSuccessPhoneNumber(Object object) {
        isPhoneNumberAvailable = false;
    }

    @Override
    public void onVerifyPhoneNumberError(Throwable e) {
        isPhoneNumberAvailable = true;
        showErrorMessage(phoneNumber, getString(R.string.mobile_number_already_exist));
    }
}
