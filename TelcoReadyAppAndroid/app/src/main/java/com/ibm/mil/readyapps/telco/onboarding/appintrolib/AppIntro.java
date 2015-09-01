/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.onboarding.appintrolib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ibm.mil.readyapps.telco.R;

import java.util.List;
import java.util.Vector;

public abstract class AppIntro extends AppCompatActivity {
    private PagerAdapter mPagerAdapter;
    private ViewPager pager;
    private final List<Fragment> fragments = new Vector<>();
    private int slidesNumber;
    private IndicatorController mController;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.intro_layout);

        final TextView skipButton = (TextView) findViewById(R.id.skip);
        final TextView nextButton = (TextView) findViewById(R.id.next);
        final TextView doneButton = (TextView) findViewById(R.id.done);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onSkipPressed();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onDonePressed();
            }
        });

        mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        pager = (ViewPager) findViewById(R.id.view_pager);

        pager.setAdapter(this.mPagerAdapter);
        /**
         *  ViewPager.setOnPageChangeListener is now deprecated. Use addOnPageChangeListener() instead of it.
         */
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (slidesNumber > 1)
                    mController.selectPosition(position);
                if (position == slidesNumber - 1) {
                    skipButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.GONE);
                    doneButton.setVisibility(View.VISIBLE);
                } else {
                    skipButton.setVisibility(View.VISIBLE);
                    doneButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        init(savedInstanceState);
        slidesNumber = fragments.size();

        if (slidesNumber == 1) {
            nextButton.setVisibility(View.GONE);
            doneButton.setVisibility(View.VISIBLE);
        } else {
            initController();
        }
    }

    private void initController() {
        if (mController == null)
            mController = new DefaultIndicatorController();

        FrameLayout indicatorContainer = (FrameLayout) findViewById(R.id.indicator_container);
        indicatorContainer.addView(mController.newInstance(this));

        mController.initialize(slidesNumber);
    }

    protected void addSlide(@NonNull Fragment fragment) {
        fragments.add(fragment);
        mPagerAdapter.notifyDataSetChanged();
    }

    protected abstract void init(@Nullable Bundle savedInstanceState);

    protected abstract void onSkipPressed();

    protected abstract void onDonePressed();

    @Override
    public boolean onKeyDown(int code, KeyEvent kvent) {
        if (code == KeyEvent.KEYCODE_ENTER || code == KeyEvent.KEYCODE_BUTTON_A) {
            ViewPager vp = (ViewPager) this.findViewById(R.id.view_pager);
            if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1) {
                onDonePressed();
            } else {
                vp.setCurrentItem(vp.getCurrentItem() + 1);
            }
            return false;
        }
        return super.onKeyDown(code, kvent);
    }
}