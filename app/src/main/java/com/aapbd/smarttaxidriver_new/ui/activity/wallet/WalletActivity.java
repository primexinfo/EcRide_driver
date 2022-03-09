package com.aapbd.smarttaxidriver_new.ui.activity.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapbd.smarttaxidriver_new.MvpApplication;
import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.data.network.model.WalletMoneyAddedResponse;
import com.aapbd.smarttaxidriver_new.data.network.model.WalletResponse;
import com.aapbd.smarttaxidriver_new.ui.activity.payment.PaymentActivity;
import com.aapbd.smarttaxidriver_new.ui.activity.request_money.RequestMoneyActivity;
import com.aapbd.smarttaxidriver_new.ui.adapter.WalletAdapter;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aapbd.smarttaxidriver_new.ui.activity.payment.PaymentActivity.PICK_PAYMENT_METHOD;

public class WalletActivity extends BaseActivity implements WalletIView {

    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.tvWalletAmt)
    TextView tvWalletAmt;
    @BindView(R.id.rvWalletData)
    RecyclerView rvWalletData;
    @BindView(R.id.tvWalletPlaceholder)
    TextView tvWalletPlaceholder;
    @BindView(R.id.llWalletHistory)
    LinearLayout llWalletHistory;
    @BindView(R.id.ivRequestMoney)
    ImageView ivRequestMoney;
    @BindView(R.id.addAmount)
    Button addAmount;
    @BindView(R.id.etRequestAmt)
    EditText etRequestAmt;
    private WalletPresenter mPresenter = new WalletPresenter();
    private double walletAmt;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        etRequestAmt.setKeyListener(new DigitsInputFilter());
        mPresenter.attachView(this);

        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.wallet));

        showLoading();

        if (SharedHelper.getIntKey(this, "card") == 0)
        {
            ivRequestMoney.setVisibility(View.GONE);
        }
        else {
            ivRequestMoney.setVisibility(View.VISIBLE);
        }
        mPresenter.getWalletData();
        rvWalletData.setLayoutManager(new LinearLayoutManager(activity(), LinearLayoutManager.VERTICAL, false));
        rvWalletData.setItemAnimator(new DefaultItemAnimator());
        rvWalletData.setHasFixedSize(true);

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

    @OnClick(R.id.addAmount)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addAmount:
                if (etRequestAmt.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity(), getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
                    return;
                } else if (Float.parseFloat(etRequestAmt.getText().toString().trim()) == 0) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.valid_amount), Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else {
                    String enteredamount = etRequestAmt.getText().toString().trim();
                    Intent intent = new Intent(activity(), PaymentActivity.class);
                    intent.putExtra("hideCash", true);
                    intent.putExtra("enteramount", enteredamount);
                    startActivityForResult(intent, PICK_PAYMENT_METHOD);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onSuccess(WalletResponse response) {
        hideLoading();
        walletAmt = response.getWalletBalance();
        tvWalletAmt.setText(String.format("%s %s", AppConstant.Currency, walletAmt));
        if (response.getWalletTransation() != null && response.getWalletTransation().size() > 0) {
            rvWalletData.setAdapter(new WalletAdapter(response.getWalletTransation()));
            llWalletHistory.setVisibility(View.VISIBLE);
            tvWalletPlaceholder.setVisibility(View.GONE);
        } else {
            llWalletHistory.setVisibility(View.GONE);
            tvWalletPlaceholder.setVisibility(View.VISIBLE);
        }


        etRequestAmt.setHint(AppConstant.Currency + "50");
    }

    @Override
    public void onSuccess(WalletMoneyAddedResponse response) {
        showLoading();
        etRequestAmt.setText(null);
        mPresenter.getWalletData();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @OnClick(R.id.ivRequestMoney)
    public void onViewClicked() {
        startActivity(new Intent(this, RequestMoneyActivity.class).putExtra("WalletAmt", walletAmt));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK && data != null)
            if (data.getStringExtra("payment_mode").equals("CARD")) {
                HashMap<String, Object> map = new HashMap<>();
                String cardId = data.getStringExtra("card_id");
                map.put("amount", etRequestAmt.getText().toString());
                map.put("card_id", cardId);
                map.put("payment_mode", "CARD");
                map.put("user_type", "provider");
                showLoading();
                mPresenter.addMoney(map);
            }
      else  if (data.getStringExtra("payment_mode") != null) {
            if (data.getStringExtra("payment_mode").equals("SSL")) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("payment_mode", "SSL");
                map.put("user_type", "provider");
                map.put("amount", etRequestAmt.getText().toString().trim());
                map.put("token", SharedHelper.getKey(MvpApplication.getInstance(),AppConstant.SharedPref.ACCESS_TOKEN));
                showLoading();
                mPresenter.addSSLMoney(map);
            }
        }


    }

    public class DigitsInputFilter extends DigitsKeyListener {

        private int decimalPlaces = 2;

        DigitsInputFilter() {
            super(false, true);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            CharSequence out = super.filter(source, start, end, dest, dstart, dend);
            if (out != null) {
                source = out;
                start = 0;
                end = out.length();
            }

            int length = end - start;
            if (length == 0)
                return source;

            if (dend == 0 && source.toString().equals("."))
                return "";

            int destLength = dest.length();
            for (int i = 0; i < dstart; i++) {
                if (dest.charAt(i) == '.')
                    return (destLength - (i + 1) + length > decimalPlaces) ?
                            "" : new SpannableStringBuilder(source, start, end);
            }

            for (int i = start; i < end; ++i) {
                if (source.charAt(i) == '.') {
                    if ((destLength - dend) + (end - (i + 1)) > decimalPlaces)
                        return "";
                    else
                        break;
                }
            }

            return new SpannableStringBuilder(source, start, end);
        }
    }
}
