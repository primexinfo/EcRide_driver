package com.aapbd.smarttaxidriver_new.ui.activity.phone_verification;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.common.Utilities;
import com.aapbd.smarttaxidriver_new.data.network.APIClient;
import com.aapbd.smarttaxidriver_new.data.network.model.PhoneisAvaiable;
import com.aapbd.smarttaxidriver_new.ui.countrypicker.Country;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPhoneActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText editText;
    private String countryCode = "+880";
    private String phoneNumber="1717653445";

    List<Country> allCountries;

    List<String> allCountryNames;
 //   private String countryCode="+1";
    private ImageView countryImageView;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        allCountries = Country.getAllCountries();
        allCountryNames = Country.getAllCountryNames();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.com_facebook_loading));
        countryImageView = findViewById(R.id.countryImage);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allCountryNames));

        editText = findViewById(R.id.editTextPhone);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                countryImageView.setImageResource(allCountries.get(spinner.getSelectedItemPosition()).getFlag());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String code = allCountries.get(spinner.getSelectedItemPosition()).getDialCode();
                countryCode = allCountries.get(spinner.getSelectedItemPosition()).getDialCode();
                String number = editText.getText().toString().trim();
                Log.e("phone_number",number);

                if (number.startsWith("0")){
                    number = number.substring(1);
                    Log.e("phone_number2",number);
                }

                if (number.isEmpty() || number.length() < 8) {
                    editText.setError(getString(R.string.valid_number));
                    editText.requestFocus();
                    return;
                    
                } else {
                    dialog.show();
                    checkNumber(number,code);
                }
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString(AppConstant.SharedPref.PHONENUMBER);
            countryCode=extras.getString(AppConstant.SharedPref.COUNTRY_CODE);


            // set value to UI

            editText.setText(phoneNumber);

            // set current country
            spinner.setSelection(Country.getCountryPositionByCountryCode(countryCode));



        }
    }

    private void Next(String phonenumber,String countrycode) {


        Log.e("PhoneNumber and code ", countrycode+phoneNumber);
        Intent intent = new Intent(EnterPhoneActivity.this, VerifyPhoneActivity.class);
        intent.putExtra(AppConstant.PHONENUMBER, phonenumber);
        intent.putExtra(AppConstant.COUNTRYCODE, countrycode);

        startActivity(intent);
        EnterPhoneActivity.this.finish();
    }

    private void checkNumber(String number,String code) {
        Call<PhoneisAvaiable> listCall = APIClient.getAPIClient().isAvaiable(number);
        listCall.enqueue(new Callback<PhoneisAvaiable>() {
            @Override
            public void onResponse(Call<PhoneisAvaiable> call, Response<PhoneisAvaiable> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("isAvailableAccount=>", response.body().toString());
                    PhoneisAvaiable phone = response.body();

                    if (AppConstant.isFromForgot) {

                        if (!phone.getIsAvaiable()) {
                            Next(number,code);
                        } else
                            Toasty.error(EnterPhoneActivity.this, getString(R.string.phone_number_not_found), Toast.LENGTH_SHORT).show();
                        return;

                    }
                    // for Register

                    if (phone.getIsAvaiable()) {
                        Log.d("isAvailableAccount=>", "in register");

                        Next(number,code);
                    } else
                        Toasty.error(EnterPhoneActivity.this, getString(R.string.phone_number_already_exists), Toast.LENGTH_SHORT).show();
                    return;
                } else
                    Toasty.error(EnterPhoneActivity.this, getString(R.string.some_thing_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PhoneisAvaiable> call, Throwable t) {
                dialog.dismiss();
                Toasty.error(EnterPhoneActivity.this, getString(R.string.some_thing_wrong), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}
