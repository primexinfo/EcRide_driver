package com.aapbd.smarttaxidriver_new.ui.activity.invite_friend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.common.SharedHelper;
import com.aapbd.smarttaxidriver_new.data.network.model.UserResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendActivity extends BaseActivity implements InviteFriendIView {

    private InviteFriendPresenter<InviteFriendActivity> inviteFriendPresenter = new InviteFriendPresenter<>();

    @BindView(R.id.invite_friend)
    TextView invite_friend;
    @BindView(R.id.referral_code)
    TextView referral_code;
    @BindView(R.id.referral_amount)
    TextView referral_amount;

    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        inviteFriendPresenter.attachView(this);

        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.invite_friends));


        if (!SharedHelper.getKey(this, AppConstant.ReferalKey.REFERRAL_CODE).equalsIgnoreCase(""))
            updateUI();
        else inviteFriendPresenter.profile();
    }

    private void updateUI() {
        referral_code.setText(SharedHelper.getKey(this, AppConstant.ReferalKey.REFERRAL_CODE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, AppConstant.ReferalKey.REFERRAL_TEXT), Html.FROM_HTML_MODE_COMPACT));
            referral_amount.setText("Referrral Count :"+Html.fromHtml(SharedHelper.getKey(this, AppConstant.ReferalKey.REFERRAL_COUNT), Html.FROM_HTML_MODE_COMPACT));

        } else {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, AppConstant.ReferalKey.REFERRAL_TEXT)));
            referral_amount.setText("Referrral Count :"+Html.fromHtml(SharedHelper.getKey(this, AppConstant.ReferalKey.REFERRAL_COUNT)));
        }
    }

    @Override
    public void onSuccess(UserResponse response) {

        SharedHelper.putKey(this, AppConstant.ReferalKey.REFERRAL_CODE, response.getReferral_unique_id());
        SharedHelper.putKey(this, AppConstant.ReferalKey.REFERRAL_COUNT, response.getReferral_count());
        SharedHelper.putKey(this, AppConstant.ReferalKey.REFERRAL_TEXT, response.getReferral_text());
        SharedHelper.putKey(this,AppConstant.ReferalKey.REFERRAL_AMOUNT,response.getReferral_amount());
        SharedHelper.putKey(this, AppConstant.ReferalKey.REFERRAL_TOTAL_TEXT, response.getReferral_total_text());
        updateUI();
    }

    @Override
    public void onError(Throwable throwable) {
        onErrorBase(throwable);
    }

    @SuppressLint("StringFormatInvalid")
    @OnClick({R.id.share})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.share:
                try {
                    String appName = getString(R.string.app_name);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, appName);
                    i.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content_referral, appName, SharedHelper.getKey(this, AppConstant.ReferalKey.REFERRAL_CODE)));
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
