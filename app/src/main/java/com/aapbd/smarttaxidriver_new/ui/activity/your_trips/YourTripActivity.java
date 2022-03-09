package com.aapbd.smarttaxidriver_new.ui.activity.your_trips;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.view.MenuItem;
import android.widget.TextView;

import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.ui.fragment.past.PastTripFragment;
import com.aapbd.smarttaxidriver_new.ui.fragment.upcoming.UpcomingTripFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YourTripActivity extends BaseActivity implements YourTripIView {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.container)
    ViewPager container;

    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;


    private TabPagerAdapter adapter;
    private YourTripPresenter presenter = new YourTripPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_your_trip;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        presenter.attachView(this);

        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.your_trips));

        tabs.addTab(tabs.newTab().setText(getString(R.string.past)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.upcoming)));
        tabs.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));

        adapter = new TabPagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        container.setAdapter(adapter);
        container.canScrollHorizontally(0);
        container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                container.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    public class TabPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new PastTripFragment();
                case 1:
                    return new UpcomingTripFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
