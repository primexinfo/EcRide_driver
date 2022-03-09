package com.aapbd.smarttaxidriver_new.ui.activity.liftetime_earning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aapbd.smarttaxidriver_new.MvpApplication;
import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.ui.activity.sslcommerz.SslCommerzActivity;
import com.aapbd.smarttaxidriver_new.ui.activity.wallet.WalletActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LifetimeEarningStatistics extends AppCompatActivity {
    WebView webview;
    ProgressBar progressBar;
    String lifetimeearningulr="";
    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifetime_earning_statistics);
        ButterKnife.bind(this);
        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.earnings_statictics));
        String sslcokenkey =  SharedHelper.getKey(MvpApplication.getInstance(), AppConstant.SharedPref.ACCESS_TOKEN);
        lifetimeearningulr="https://smarttaxi.aapbd.com/api/provider/statistick?token=";
        String finalurl=lifetimeearningulr+sslcokenkey;
        webview = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        webview.setWebViewClient(new LifetimeEarningStatistics.WebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        Log.d("finalurlw",finalurl);
        webview.loadUrl(finalurl);
    }
    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

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