/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibm.mil.cafejava.CafeJava;
import com.ibm.mil.readyapps.telco.BuildConfig;
import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.analytics.AnalyticsCnsts;
import com.ibm.mil.readyapps.telco.analytics.GestureListener;
import com.ibm.mil.readyapps.telco.analytics.NavTracker;
import com.ibm.mil.readyapps.telco.baseplan.BasePlan;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanModelImpl;
import com.ibm.mil.readyapps.telco.hotspots.HotSpotActivity;
import com.ibm.mil.readyapps.telco.mydata.DataFragment;
import com.ibm.mil.readyapps.telco.myplan.MyPlanFragment;
import com.ibm.mil.readyapps.telco.myplan.MyPlanPresenter;
import com.ibm.mil.readyapps.telco.mytalk.TalkFragment;
import com.ibm.mil.readyapps.telco.mytext.TextFragment;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.onboarding.OnboardingActivity;
import com.ibm.mil.readyapps.telco.recharge.RechargeActivity;
import com.ibm.mil.readyapps.telco.utils.Currency;
import com.ibm.mil.readyapps.telco.utils.FontCache;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;
import com.ibm.mil.readyapps.telco.utils.TelcoChallengeHandler;
import com.ibm.mil.readyapps.telco.utils.TwitterHelper;
import com.ibm.mqa.MQA;
import com.ibm.mqa.config.Configuration;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.worklight.common.WLAnalytics;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Main landing Activity of app that contains all the tab bar fragments.
 */
@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    public static Context context;
    public static final int MY_PLAN = 0;
    private static final String TAG = MainActivity.class.getName();
    private static final int DATA = 1;
    private static final int TALK = 2;
    private static final int TEXT = 3;
    private GestureDetectorCompat detector;
    public static final PublishSubject<Integer> tabChangeStream = PublishSubject.create();
    List<ImageView> tabImages;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.view_pager) ViewPager viewPager;
    @Bind(R.id.main_activity_fab) FloatingActionButton floatingActionButton;
    @Bind(R.id.current_plan_total) TextView currentPlanTotal;
    @Bind(R.id.plan_tab_image) ImageView planTabImage;
    @Bind(R.id.data_tab_image) ImageView dataTabImage;
    @Bind(R.id.talk_tab_image) ImageView talkTabImage;
    @Bind(R.id.text_tab_image) ImageView textTabImage;
    private MyPlanPresenter myPlanPresenter;
    private Intent rechargeIntent;
    private TwitterAuthClient twitterAuthClient;
    private ViewPagerAdapter pagerAdapter;

    /**
     * Responsible for setting up tab layout with fragments
     * and other various setup tasks.
     *
     * @param savedInstanceState previous state of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detector = new GestureDetectorCompat(this, new GestureListener());
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        tabImages = Arrays.asList(planTabImage, dataTabImage, talkTabImage, textTabImage);
        rechargeIntent = new Intent(MainActivity.this, RechargeActivity.class);
        twitterAuthClient = new TwitterAuthClient();

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addTabFragment(new TabFragment(getString(R.string.my_plan), new MyPlanFragment()));
        pagerAdapter.addTabFragment(new TabFragment(getString(R.string.data), new DataFragment()));
        pagerAdapter.addTabFragment(new TabFragment(getString(R.string.talk), new TalkFragment()));
        pagerAdapter.addTabFragment(new TabFragment(getString(R.string.text), new TextFragment()));
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);

        updateTabText(pagerAdapter);

        floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.orange));
        context = this;

        updateFABForMyPlanTab();
        setImageHighlight(planTabImage);
        subscribeForToolbarCostUpdates();

        // configure and start MQA
        String MQA_KEY = getString(R.string.mqaKey);
        Configuration configuration = new Configuration.Builder(this)
                .withAPIKey(MQA_KEY)
                .withMode(MQA.Mode.MARKET)
                .withReportOnShakeEnabled(true)
                .build();

        // Only want to start an MQA session when sending out a signed release build
        Boolean startMQA = !BuildConfig.DEBUG;
        if (startMQA) {
            MQA.startNewSession(this, configuration);
        }

        // connect to MFP and register ChallengeHandler
        CafeJava.connect(this)
                .subscribe(new Action1<WLResponse>() {
                    @Override public void call(WLResponse wlResponse) {
                        Log.d(TAG, "Connection succeeded: " + wlResponse.getResponseText());
                        WLClient.getInstance().registerChallengeHandler(new TelcoChallengeHandler());
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Log.d(TAG, "Connection failed: " + throwable.getMessage());
                    }
                });

        WLAnalytics.setContext(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        updateTabText(pagerAdapter);
    }


    /**
     * Programmatically take away all caps for tab bar titles.
     *
     * @param pagerAdapter needed so we know how many tabs to loop through
     */
    private void updateTabText(ViewPagerAdapter pagerAdapter) {
        Typeface font = FontCache.get("Roboto-Regular.ttf", context);

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            TextView tv = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i)).getChildAt(0));
            tv.setAllCaps(false);
            tv.setTypeface(font);
        }
    }

    /**
     * Listen for any updates to Base Plan so the
     * ToolBar cost can be updated accordingly.
     */
    private void subscribeForToolbarCostUpdates() {
        new BasePlanModelImpl().getBasePlanStream()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BasePlan>() {
                    @Override
                    public void call(BasePlan basePlan) {
                        setToolbarText(basePlan);
                    }
                });
    }

    /**
     * Actually update the cost TextView with updated BasePlan info.
     *
     * @param basePlan the updated BasePlan object with new cost info
     */
    private void setToolbarText(BasePlan basePlan) {
        double totalCost = basePlan.getTotalCost();
        String localizedTotalCost = Currency.localize(totalCost, false);
        currentPlanTotal.setText(localizedTotalCost);
    }

    /**
     * Called every time a tab is selected. Useful for updating FAB
     * to respond appropriately depending on which tab is active,
     * and updating view of tabs to show active tab.
     *
     * @param tab the tab that was selected
     */
    @Override
    public void onTabSelected(final TabLayout.Tab tab) {
        int tabPosition = tab.getPosition();
        viewPager.setCurrentItem(tabPosition, true);
        tabChangeStream.onNext(tabPosition);

        @AnalyticsCnsts.Page String page = null;

        switch (tabPosition) {
            case MY_PLAN:
                page = AnalyticsCnsts.MYPLAN;
                updateFABForMyPlanTab();
                setImageHighlight(planTabImage);
                break;
            case DATA:
                page = AnalyticsCnsts.MYDATA;
                rechargeIntent.putExtra(RechargeActivity.TYPE_BUNDLE_KEY, PlanConstants.DATA);
                updateFABForDataTalkText();
                setImageHighlight(dataTabImage);
                break;
            case TALK:
                page = AnalyticsCnsts.MYTALK;
                rechargeIntent.putExtra(RechargeActivity.TYPE_BUNDLE_KEY, PlanConstants.TALK);
                updateFABForDataTalkText();
                setImageHighlight(talkTabImage);
                break;
            case TEXT:
                page = AnalyticsCnsts.MYTEXT;
                rechargeIntent.putExtra(RechargeActivity.TYPE_BUNDLE_KEY, PlanConstants.TEXT);
                updateFABForDataTalkText();
                setImageHighlight(textTabImage);
                break;
        }

        NavTracker.getInstance().setScreen(page);
    }

    /**
     * Empty override. Necessary because implementing TabLayout.OnTabSelectedListener.
     *
     * @param tab the tab that was unselected
     */
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    /**
     * Empty override. Necessary because implementing TabLayout.OnTabSelectedListener.
     *
     * @param tab the tab that was reselected
     */
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    /**
     * Highlight the active tab image while dimming all other tab images.
     *
     * @param tabImageToHighlight the tab image to highlight
     */
    private void setImageHighlight(ImageView tabImageToHighlight) {
        for (ImageView image : tabImages) {
            if (image == tabImageToHighlight) {
                image.setAlpha((float) 1.0);
            } else {
                image.setAlpha((float) 0.5);
            }
        }
    }

    /**
     * Inflate the overflow menu for MainActivity.
     *
     * @param menu the menu to inflate
     * @return true if you want menu to be shown, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Callback method for when user taps an option in overflow menu.
     *
     * @param   item the item that was tapped
     * @return  true for tap event to be consumed by this method,
     *          false to allow falling through to other item click functions
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_walkthrough) {
            Intent intent = new Intent(this, OnboardingActivity.class);
            intent.putExtra(OnboardingActivity.COMING_FROM_OVERFLOW, true);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Update the FAB image and click listener for My Plan tab.
     */
    private void updateFABForMyPlanTab() {
        floatingActionButton.setImageResource(R.drawable.wifi_white);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavTracker.getInstance().setScreen(AnalyticsCnsts.WIFI);
                startActivity(new Intent(MainActivity.this, HotSpotActivity.class));
            }
        });
    }

    /**
     * Update the FAB image and click listener for Data/Talk/Text tabs.
     */
    private void updateFABForDataTalkText() {
        floatingActionButton.setImageResource(R.drawable.recharge_white);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view
            ) {
                NavTracker.getInstance().setScreen(AnalyticsCnsts.RECHARGE);
                startActivity(rechargeIntent);
            }
        });
    }

    /**
     * Set the presenter being used by MyPlan.
     *
     * @param presenter the presenter being used
     */
    public void setMyPlanPresenter(MyPlanPresenter presenter) {
        myPlanPresenter = presenter;
    }

    /**
     * Generates a hotspot system notification.
     *
     * @param item the menu item to select
     */
    public void generateHotspotNotification(MenuItem item) {
        Intent intent = new Intent(this, HotSpotActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.hotspot_notification_title))
                .setContentText(getString(R.string.hotspot_notification_body))
                .setSmallIcon(R.drawable.ic_stat_acme_transparent)
                .setColor(getResources().getColor(R.color.dark_indigo))
                .setAutoCancel(true)
                .setContentIntent(pIntent);
        int mNotificationId = 2;

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(mNotificationId, mBuilder.build());
    }

    /**
     * Generates a demo offer and a system notification about said offer.
     *
     * @param item the menu item to select
     */
    public void generateDemoOffer(MenuItem item) {
        Offer offerToAdd = new Offer(getString(R.string.unlimited_sundays),
                getString(R.string.offer_recharge),
                R.drawable.recharge,
                5.00, "recharge", false, false, true);
        if (myPlanPresenter != null) {
            myPlanPresenter.addOffer(offerToAdd);
        }
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.recharge_notification)))
                .setContentTitle(getString(R.string.offer_notification_title))
                .setContentText(getString(R.string.recharge_notification))
                .setSmallIcon(R.drawable.ic_stat_acme_transparent)
                .setColor(getResources().getColor(R.color.dark_indigo))
                .setAutoCancel(true)
                .setContentIntent(pIntent);
        int mNotificationId = 1;

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(mNotificationId, mBuilder.build());

    }

    /**
     * Update user's Twitter status if already granted the app access,
     * or ask for access and then Tweet (if access granted) otherwise.
     *
     * @param status the Twitter status update for the user
     */
    public void tryToTweet(final String status) {
        if (TwitterHelper.alreadyAuthorized()) {
            TwitterHelper.tweet(status);
        } else {
            TwitterHelper.authorizeThenTweet(this, twitterAuthClient, status);
        }
    }

    /**
     * Needed by Twitter SDK to callback after asking for authorization of app.
     *
     * @param requestCode the type of request
     * @param resultCode the result of the authorization
     * @param data any data attached to authorization
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Set up the ViewPager with Fragments
     */
    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<TabFragment> fragmentList = new ArrayList<>();

        /**
         * Initialize a ViewPagerAdapter with the support fragment manager.
         *
         * @param fragmentManager the fragment manager to init with
         */
        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        /**
         * Get the fragment associated with a position.
         *
         * @param position the position of fragment we want to retrieve
         * @return the fragment at the specified position
         */
        @Override public Fragment getItem(int position) {
            return fragmentList.get(position).getFragment();
        }

        /**
         * Returns the number of fragments in the ViewPager.
         *
         * @return the number of fragments
         */
        @Override public int getCount() {
            return fragmentList.size();
        }

        /**
         * Add a TabFragment instance to the ViewPagerAdapter.
         *
         * @param tabFragment the TabFragment to add
         */
        public void addTabFragment(TabFragment tabFragment) {
            fragmentList.add(tabFragment);
        }

        /**
         * Automatically set the titles of the TabLayout.
         *
         * @param position the position we want the title for
         * @return the title for the specified position
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentList.get(position).getTitle();
        }
    }

    /**
     * Helper class for having a Fragment with a tab title.
     */
    private static class TabFragment {
        private final String title;
        private final Fragment fragment;

        public TabFragment(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
