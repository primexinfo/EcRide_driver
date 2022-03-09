package com.aapbd.smarttaxidriver_new.ui.activity.PhoneModule;

import android.content.Intent;

import com.aapbd.smarttaxidriver_new.ui.activity.phone_verification.EnterPhoneActivity;
import com.aapbd.smarttaxidriver_new.ui.countrypicker.Country;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.ui.activity.password.PasswordActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.BuildConfig;
import es.dmoral.toasty.Toasty;

public class PhoneNumberActivity extends BaseActivity implements EmailIView {

    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.next)
    FloatingActionButton next;

    EmailIPresenter presenter = new EmailPresenter();
    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private Spinner spinner;

    private String countryCode = "+880";
    List<Country> allCountries;

    List<String> allCountryNames;
    //   private String countryCode="+1";
    private ImageView countryImageView;
    @Override
    public int getLayoutId() {
        return R.layout.activity_email;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        allCountries = Country.getAllCountries();
        allCountryNames = Country.getAllCountryNames();
        countryImageView = findViewById(R.id.countryImage);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allCountryNames));

        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.mobile_number));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                countryImageView.setImageResource(allCountries.get(spinner.getSelectedItemPosition()).getFlag());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        if (BuildConfig.DEBUG) email.setText("1717653445");
    }

    @OnClick({ R.id.sign_up, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                startActivity(new Intent(this, EnterPhoneActivity.class));
                break;
            case R.id.next:
                countryCode = allCountries.get(spinner.getSelectedItemPosition()).getDialCode();
                String phoneNumber = email.getText().toString();
                String phonewithcountrycode =countryCode +phoneNumber;
                Log.d("fullphonenumber",phonewithcountrycode);
                if (phoneNumber.isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
                    return;

                }
                Intent i = new Intent(this, PasswordActivity.class);
                i.putExtra(AppConstant.SharedPref.PHONENUMBER, phoneNumber);
                i.putExtra(AppConstant.SharedPref.COUNTRY_CODE, countryCode);
                SharedHelper.putKey(this, AppConstant.SharedPref.PHONEWITHCCODE, phonewithcountrycode);
                startActivity(i);
                break;


        }
    }
    // validating PhoneModule id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
