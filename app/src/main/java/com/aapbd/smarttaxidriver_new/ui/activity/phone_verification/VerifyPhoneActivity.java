package com.aapbd.smarttaxidriver_new.ui.activity.phone_verification;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.ui.activity.regsiter.RegisterActivity;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.aapbd.smarttaxidriver_new.ui.activity.reset_password.ResetActivity;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private Button buttonSignIn;
    private TextView resent_code;

    int starttime = 60;

    String phonenumber = "", countrycode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        resent_code = findViewById(R.id.resent_code);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        phonenumber = getIntent().getStringExtra(AppConstant.PHONENUMBER);
        countrycode = getIntent().getStringExtra(AppConstant.COUNTRYCODE);
        if (BaseActivity.is_profile_update) {
            buttonSignIn.setText(getString(R.string.enter));
        }


        Log.e("Entered number is ", phonenumber + " and code " + countrycode);

        sendVerificationCode(countrycode + phonenumber);

        if (BaseActivity.is_profile_update) {
            buttonSignIn.setText(getString(R.string.enter));
        }

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void startTimer() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (starttime >= 0) {
                    resent_code.setText(getString(R.string.resend_OTP) + " " + getString(R.string.In) + " 0 : " + starttime);
                    if (starttime == 0) {
                        resent_code.setText(getString(R.string.resend_OTP));
                    } else {
                        starttime--;
                    }
                    startTimer();
                }
            }
        }, 1000);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        if (AppConstant.isFromForgot) {
                            Intent intent = new Intent(VerifyPhoneActivity.this, ResetActivity.class);
                            intent.putExtra(AppConstant.PHONENUMBER, phonenumber);
                            intent.putExtra(AppConstant.COUNTRYCODE, countrycode);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(VerifyPhoneActivity.this, RegisterActivity.class);
                            intent.putExtra(AppConstant.PHONENUMBER, phonenumber);
                            intent.putExtra(AppConstant.COUNTRYCODE, countrycode);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        // delete current users

                        try {
                            task.getResult().getUser().delete();

                        } catch (Exception e) {

                        }


                    } else {
                        Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendVerificationCode(String number) {
        //progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                10,
                TimeUnit.SECONDS,
               this,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resent_code.setVisibility(View.VISIBLE);
            starttime = 60;
            startTimer();
            Toast.makeText(VerifyPhoneActivity.this, getString(R.string.Code_send_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public void Resend_OTP(View view) {
        if (starttime == 0) {
            sendVerificationCode(countrycode + phonenumber);
        }
    }

}

