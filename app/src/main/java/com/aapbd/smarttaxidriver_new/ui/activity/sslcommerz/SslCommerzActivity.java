package com.aapbd.smarttaxidriver_new.ui.activity.sslcommerz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aapbd.smarttaxidriver_new.MvpApplication;
import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.ui.activity.wallet.WalletActivity;

public class SslCommerzActivity extends AppCompatActivity {
    WebView webview;
    ProgressBar progressBar;

    /*@BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssl_commerz);
        Intent intent = getIntent();
        String enteraboutval = intent.getStringExtra("sslamount");
        Log.d("sslaccesstoken", SharedHelper.getKey(MvpApplication.getInstance(), AppConstant.SharedPref.ACCESS_TOKEN));
        String sslBase = "https://smarttaxi.aapbd.com/api/provider/add/money/ssl?payment_mode=SSL&user_type=provider&amount=";
        String sslcokenkey = "&token=" + SharedHelper.getKey(MvpApplication.getInstance(), AppConstant.SharedPref.ACCESS_TOKEN);
        String finalsslurl = sslBase + enteraboutval + sslcokenkey;
        Log.d("stringfinalurl", finalsslurl);
        // String sslurl="https://smarttaxi.aapbd.com/api/provider/add/money?payment_mode=SSL&user_type=provider&amount=820&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIxOSwiaXNzIjoiaHR0cHM6Ly9zbWFydHRheGkuYWFwYmQuY29tL2FwaS9wcm92aWRlci9vYXV0aC90b2tlbiIsImlhdCI6MTU5MjMwNTA0NSwiZXhwIjoxNTkyNjY1MDQ1LCJuYmYiOjE1OTIzMDUwNDUsImp0aSI6ImhLVExLZWJJSkVmdWQ0WnoifQ.vemkoWoRLP0RxLL3MILFQHNygI27W5hW98frrII09Sw";
       /* toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.settings));*/
        webview = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        webview.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl(finalsslurl);
        // webview.loadUrl("https://smarttaxi.aapbd.com/api/provider/add/money?payment_mode=SSL&user_type=provider&amount=820&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIxOSwiaXNzIjoiaHR0cHM6Ly9zbWFydHRheGkuYWFwYmQuY29tL2FwaS9wcm92aWRlci9vYXV0aC90b2tlbiIsImlhdCI6MTU5MjMwNTA0NSwiZXhwIjoxNTkyNjY1MDQ1LCJuYmYiOjE1OTIzMDUwNDUsImp0aSI6ImhLVExLZWJJSkVmdWQ0WnoifQ.vemkoWoRLP0RxLL3MILFQHNygI27W5hW98frrII09Sw");
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if ("https://smarttaxi.aapbd.com/ssl/success".equals(url)) {
                 Toast.makeText(SslCommerzActivity.this, "wallet recharge successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SslCommerzActivity.this, WalletActivity.class);
                startActivity(intent);
                finish();

            }
            if ("https://smarttaxi.aapbd.com/ssl/fail".equals(url)) {
                Toast.makeText(SslCommerzActivity.this, "please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}