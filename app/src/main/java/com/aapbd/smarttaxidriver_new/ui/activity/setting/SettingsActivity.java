package com.aapbd.smarttaxidriver_new.ui.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.aapbd.smarttaxidriver_new.common.LocaleHelper;
import com.aapbd.smarttaxidriver_new.ui.activity.document.DocumentActivity;
import com.aapbd.smarttaxidriver_new.ui.activity.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingsActivity extends BaseActivity implements SettingsIView {

    @BindView(R.id.english)
    RadioButton english;
    @BindView(R.id.arabic)
    RadioButton arabic;
    @BindView(R.id.bangla)
    RadioButton bangla;
   /* @BindView(R.id.bangla)
    TextView txtBangla;*/
    @BindView(R.id.choose_language)
    RadioGroup chooseLanguage;
    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private String setting;

    private SettingsPresenter presenter = new SettingsPresenter();
    private String language;
    private Context con;
    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        con=this;
        presenter.attachView(this);
        setting = getIntent().getStringExtra("setting");

        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.settings));

        languageReset();

        chooseLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            showLoading();
            switch (checkedId) {
                case R.id.english:
                    language = AppConstant.LANGUAGE_ENGLISH;
                    AppConstant.SELECTED_LANGUAGE = AppConstant.LANGUAGE_ENGLISH;
                    break;
                case R.id.arabic:
                    language = AppConstant.LANGUAGE_ARABIC;
                    AppConstant.SELECTED_LANGUAGE = AppConstant.LANGUAGE_ARABIC;
                    break;
                case R.id.bangla:
                    language = AppConstant.LANGUAGE_BANGLA;
                    AppConstant.SELECTED_LANGUAGE = AppConstant.LANGUAGE_BANGLA;
                    break;
                default:
                  /*  language = AppConstant.LANGUAGE_ARABIC;
                   AppConstant.SELECTED_LANGUAGE = AppConstant.LANGUAGE_ARABIC;*/
                    break;
            }
            AppConstant.setCurrentLanguage(con, language);
            presenter.changeLanguage(language);
        });
    }

    private void languageReset() {
        String dd = LocaleHelper.getLanguage(this);
        switch (dd) {
            case AppConstant.LANGUAGE_ENGLISH:
                english.setChecked(true);
                break;
            case AppConstant.LANGUAGE_ARABIC:
                arabic.setChecked(true);
                break;
            case AppConstant.LANGUAGE_BANGLA:
                bangla.setChecked(true);
                break;
            default:
                english.setChecked(true);
                break;
        }
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

    @Override
    public void onSuccess(Object o) {
        hideLoading();

        AppConstant.setCurrentLanguage(getApplicationContext(), language);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK));
        this.overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null) {
            onErrorBase(e);
        }

        LocaleHelper.setLocale(getApplicationContext(), language);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK));
        this.overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
    }

    @OnClick(R.id.tvChangeDoc)
    public void onViewClicked() {
        Intent intent = new Intent(this, DocumentActivity.class);
        intent.putExtra("isFromSettings", true);
        intent.putExtra("setting", setting);
        startActivity(intent);
    }
}
