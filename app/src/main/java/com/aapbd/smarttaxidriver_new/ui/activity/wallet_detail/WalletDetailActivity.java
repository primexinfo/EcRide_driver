package com.aapbd.smarttaxidriver_new.ui.activity.wallet_detail;

import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.data.network.model.Transaction;
import com.aapbd.smarttaxidriver_new.ui.adapter.WalletDetailAdapter;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletDetailActivity extends BaseActivity implements WalletDetailIView {


    @BindView(R.id.rvWalletDetailData)
    RecyclerView rvWalletDetailData;
    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;


    WalletDetailPresenter<WalletDetailActivity> presenter = new WalletDetailPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(R.string.wallet_detais);
        presenter.attachView(this);
        ArrayList<Transaction> mTransactionArrayList = (ArrayList<Transaction>) getIntent().getSerializableExtra("transaction_list");
        String alias = getIntent().getStringExtra("alias");
        Objects.requireNonNull(getSupportActionBar()).setTitle(alias);
        rvWalletDetailData.setLayoutManager(new LinearLayoutManager(activity(), LinearLayoutManager.VERTICAL, false));
        rvWalletDetailData.setItemAnimator(new DefaultItemAnimator());
        rvWalletDetailData.setHasFixedSize(true);
        presenter.setAdapter(mTransactionArrayList);
    }

    @Override
    public void setAdapter(ArrayList<Transaction> myList) {
        rvWalletDetailData.setAdapter(new WalletDetailAdapter(myList));
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
}
