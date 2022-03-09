package com.aapbd.smarttaxidriver_new.ui.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.data.network.model.Card;
import com.aapbd.smarttaxidriver_new.ui.activity.add_card.AddCardActivity;
import com.aapbd.smarttaxidriver_new.ui.activity.sslcommerz.SslCommerzActivity;
import com.aapbd.smarttaxidriver_new.MvpApplication;
import com.tranxit.stripepayment.activity.add_card.StripeAddCardActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends BaseActivity implements PaymentIView {

    public static final int PICK_PAYMENT_METHOD = 12;
    private static final int STRIPE_PAYMENT_REQUEST_CODE = 100;
    private static final int BRAINTREE_PAYMENT_REQUEST_CODE = 101;
    private static final int PAYTM_PAYMENT_REQUEST_CODE = 102;
    private static final int PAYUMONEY_PAYMENT_REQUEST_CODE = 103;
    private static final int SSL_PAYMENT_REQUEST_CODE = 104;
    public static boolean isInvoiceCashToCard = false;
    @BindView(R.id.add_card)
    TextView addCard;
    @BindView(R.id.cash)
    TextView tvCash;
    @BindView(R.id.sslcommerz)
    TextView tvSslCommerz;
    @BindView(R.id.cards_rv)
    RecyclerView cardsRv;
    @BindView(R.id.llCardContainer)
    LinearLayout llCardContainer;
    @BindView(R.id.llCashContainer)
    LinearLayout llCashContainer;
    @BindView(R.id.braintree)
    TextView braintree;
    @BindView(R.id.payumoney)
    TextView payumoney;
    @BindView(R.id.paytm)
    TextView paytm;
    private List<Card> cardsList = new ArrayList<>();
    private PaymentPresenter<PaymentActivity> presenter = new PaymentPresenter<>();
    String enteredValue;
    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setTitle(getString(R.string.payment));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean hideCash = extras.getBoolean("hideCash", false);
             enteredValue=extras.getString("enteramount","0");
            tvCash.setVisibility(hideCash ? View.GONE : View.VISIBLE);
        }

        if (MvpApplication.isPayumoney) {
            payumoney.setVisibility(View.VISIBLE);
        } else {
            payumoney.setVisibility(View.GONE);
        }
        if(MvpApplication.isSsl){
            tvSslCommerz.setVisibility(View.VISIBLE);
        }else {
            tvSslCommerz.setVisibility(View.GONE);
        }

        if (MvpApplication.isPaytm) {
            paytm.setVisibility(View.VISIBLE);
        } else {
            paytm.setVisibility(View.GONE);
        }

        if (MvpApplication.isBraintree) {
            braintree.setVisibility(View.VISIBLE);
        } else {
            braintree.setVisibility(View.GONE);
        }
        getCardsDetails();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void getCardsDetails() {
        showLoading();
        new Handler().postDelayed(() -> {
            if (MvpApplication.isCard) {
                cardsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                cardsRv.setItemAnimator(new DefaultItemAnimator());
                presenter.card();
                llCardContainer.setVisibility(View.VISIBLE);
            } else {
                try {
                    hideLoading();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                llCardContainer.setVisibility(View.GONE);
            }

            if (MvpApplication.isCash && !isInvoiceCashToCard)
                llCashContainer.setVisibility(View.VISIBLE);
            else llCashContainer.setVisibility(View.GONE);
        }, 3000);
    }


    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @OnClick({R.id.add_card,R.id.cash,R.id.sslcommerz, R.id.braintree, R.id.paytm, R.id.payumoney})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_card:
                Intent intent = new Intent(this, StripeAddCardActivity.class);
                intent.putExtra("stripe_token", SharedHelper.getKey(this, "stripe_publishable_key"));
                Log.d("teststripeKey",SharedHelper.getKey(this, "stripe_publishable_key"));
                startActivityForResult(intent, STRIPE_PAYMENT_REQUEST_CODE);
                //Toast.makeText(this, "card payment", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sslcommerz:
              //  startActivity(new Intent(PaymentActivity.this, SslCommerzActivity.class));
                Intent intentssl = new Intent(PaymentActivity.this, SslCommerzActivity.class);
                intentssl.putExtra("sslamount", enteredValue);
                startActivityForResult(intentssl, SSL_PAYMENT_REQUEST_CODE);
               /// startActivity(intentssl);
                break;
            case R.id.cash:
                finishResult("CASH");
                break;

            default:
                break;
        }
    }

    public void finishResult(String mode) {
        Intent intent = new Intent();
        intent.putExtra("payment_mode", mode);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void showCardChangeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.are_sure_you_want_to_change_default_card))
                .setPositiveButton(getResources().getString(R.string.change_card),
                        (dialog, which) -> {
                            startActivity(new Intent(this, AddCardActivity.class));
                        })
                .setNegativeButton(getResources().getString(R.string.no),
                        (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onSuccess(Object card) {
        Toast.makeText(activity(), R.string.card_deleted_successfully, Toast.LENGTH_SHORT).show();
        presenter.card();
    }

    @Override
    public void onSuccess(List<Card> cards) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        cardsList.clear();
        cardsList.addAll(cards);
        cardsRv.setAdapter(new CardAdapter(cardsList));
    }

    @Override
    public void onError(Throwable e) {
        onErrorBase(e);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) if (requestCode == STRIPE_PAYMENT_REQUEST_CODE) {
            Log.d("_D", "onActivityResult: " + data.getStringExtra("stripetoken"));
            getCardsDetails();
            presenter.addCard(data.getStringExtra("stripetoken"));
        }/* else if (requestCode == PAYTM_PAYMENT_REQUEST_CODE) {
            Log.d("_D", "onActivityResult: " + data.getStringExtra("order_id"));
        } else if (requestCode == PAYUMONEY_PAYMENT_REQUEST_CODE) {
            Log.d("_D", "onActivityResult: " + data.getStringExtra("status"));
            //                presenter.addCard(data.getStringExtra("stripetoken"));
        } else if (requestCode == BRAINTREE_PAYMENT_REQUEST_CODE) {
            String paymentNonce = data.getStringExtra(BrainTreePaymentActivity.PAYMENT_NONCE);
            Log.v(TAG, "braintree payment nonce " + paymentNonce);
            Toasty.success(PaymentActivity.this, "Payment nonce " + paymentNonce, Toast.LENGTH_SHORT).show();
        }*/
    }

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

        private List<Card> list;

        private CardAdapter(List<Card> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Card item = list.get(position);
            holder.card.setText(getString(R.string.card_) + item.getLastFour());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private RelativeLayout itemView;
            private TextView card;
            private TextView changeText;

            MyViewHolder(View view) {
                super(view);
                itemView = view.findViewById(R.id.item_view);
                itemView.setOnClickListener(this);
                card = view.findViewById(R.id.card);
                changeText = view.findViewById(R.id.text_change);
                changeText.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Card card = list.get(getAdapterPosition());
                switch (view.getId()) {
                    case R.id.item_view:
                        Intent intent = new Intent();
                        intent.putExtra("payment_mode", "CARD");
                        intent.putExtra("card_id", card.getCardId());
                        intent.putExtra("card_last_four", card.getLastFour());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        break;

                    case R.id.text_change:
                        showCardChangeAlert();
                        break;

                    default:
                        break;
                }
            }
        }
    }
}
