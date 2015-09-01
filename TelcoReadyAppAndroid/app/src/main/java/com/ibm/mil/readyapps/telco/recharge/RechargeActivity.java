/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.recharge;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.analytics.GestureListener;
import com.ibm.mil.readyapps.telco.utils.Currency;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Activity for recharging either talk, text, or data.
 * Can be accessed by tapping FAB on talk, text, or data tab.
 */
public class RechargeActivity extends AppCompatActivity implements RechargeView {
    public static final String TYPE_BUNDLE_KEY = "RechargeTypeKey";
    private GestureDetectorCompat detector;
    @Bind(R.id.recharge_name) TextView rechargeName;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.unit_type) TextView unitTypeTextView;
    @Bind(R.id.unit_value) TextView unitValueTextView;
    @Bind(R.id.costTextView) TextView costTextView;
    @Bind(R.id.down_arrow) ImageView downArrow;
    @Bind(R.id.confirmation_question) TextView confirmationTextView;
    private RechargePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        detector = new GestureDetectorCompat(this, new GestureListener());
        ButterKnife.bind(this);

        setupToolbar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            @PlanConstants.Type int type = extras.getInt(TYPE_BUNDLE_KEY);
            presenter = new RechargePresenterImpl(this, type, getRechargeTitle(type));
            presenter.createRecharge();
        }
    }

    /**
     * Get the title that should appear on the recharge ui based
     * on what type of recharge is happening.
     *
     * @param type the type of recharge
     * @return the title to display (e.g. "Recharge Data")
     */
    private String getRechargeTitle(@PlanConstants.Type int type) {
        switch (type) {
            case PlanConstants.DATA:
                return getString(R.string.recharge_data);
            case PlanConstants.TALK:
                return getString(R.string.recharge_talk);
            case PlanConstants.TEXT:
                return getString(R.string.recharge_text);
        }

        return null;
    }

    /**
     * Set up the toolbar for this activity and make sure
     * the back arrow will be displayed in top left.
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * Update the text views based on the recharge data being passed in.
     *
     * @param recharge the recharge that contains the data needed
     *                 to set the text views
     */
    @Override
    public void updateTextViews(Recharge recharge) {
        unitTypeTextView.setText(recharge.getUnits());
        unitValueTextView.setText(String.valueOf(recharge.getCurrentAmount()));
        costTextView.setText(Currency.localize(recharge.getCurrentCost(), true));
        rechargeName.setText(recharge.getTitle());
        confirmationTextView.setText("Add " + recharge.getCurrentAmount() + " extra " + recharge
                .getUnits() + " for the month?");

        if (recharge.getCurrentAmount() == recharge.getInitialAmount()) {
            downArrow.setVisibility(View.INVISIBLE);
        } else {
            downArrow.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Exit this screen and go back to Main Activity
     * after user accepts the r charge.
     */
    @Override
    public void close() {
        finish();
    }

    /**
     * Tell presenter to update the model as values
     * change from arrow taps.
     *
     * @param view the view that was tapped
     */
    @OnClick(R.id.up_arrow)
    public void increaseRecharge(View view) {
        presenter.increaseAmount();
    }

    /**
     * Tell presenter to update the model as values
     * change from arrow taps.
     *
     * @param view the view that was tapped
     */
    @OnClick(R.id.down_arrow)
    public void decreaseRecharge(View view) {
        presenter.decreaseAmount();
    }

    /**
     * Tell presenter to accept a modification to the
     * user's plan.
     *
     * @param view the view that was tapped
     */
    @OnClick(R.id.accept_button)
    public void acceptRecharge(View view) {
        presenter.accept(this);
    }

    /**
     * Need to return true here so onOptionsItemSelected back button still works.
     *
     * @param menu the options menu
     * @return if this file should handle options menu taps
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Go back to MainActivity if back button tapped.
     *
     * @param item      the item tapped
     * @return boolean  return false to allow normal menu processing to proceed,
     *                  true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
