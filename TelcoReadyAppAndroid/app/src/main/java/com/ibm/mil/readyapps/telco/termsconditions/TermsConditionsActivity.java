package com.ibm.mil.readyapps.telco.termsconditions;
/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.analytics.GestureListener;


import butterknife.ButterKnife;
import butterknife.Bind;

/**
 *
 */
public class TermsConditionsActivity extends AppCompatActivity {

    public static final String OFFER_TITLE = "com.ibm.mil.readyapps.telco.offerTitle";
    public static final String OFFER_TERMS_CONDITIONS = "com.ibm.mil.readyapps.telco.offerTermsConditions";
    private GestureDetectorCompat detector;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.offer_termsconditions_title) TextView offerTitle;
    @Bind(R.id.termsconditions_text) TextView termsText;

    @Override
    /**
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telco_card_termsconditions_view);

        ButterKnife.bind(this);
        detector = new GestureDetectorCompat(this, new GestureListener());
        setupToolbar();
        setupUI();
    }



    private void setupToolbar() {
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupUI() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String offerTitleText = (String) extras.get(OFFER_TITLE);
            offerTitle.setText(offerTitleText);

            String offerTerms = (String) extras.get(OFFER_TERMS_CONDITIONS);
            if (offerTerms != null) {
                if (!offerTerms.equals("")) {
                    termsText.setText(offerTerms);
                } else {
                    setFakeTermsText();
                }
            } else {
                setFakeTermsText();
            }
        }
    }

    private void setFakeTermsText() {
        termsText.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non velit tellus. Donec pellentesque nec velit fermentum ullamcorper. \n" +
                "\n" +
                "Morbi nec mi purus. Nullam convallis eu erat ut ornare. Sed vel mi condimentum mauris dapibus venenatis. Aenean ligula magna, porta aliquet pellentesque sed, porttitor a enim. Curabitur scelerisque orci vel turpis pulvinar imperdiet. Ut auctor porttitor ex. \n" +
                "\n" +
                "Curabitur mattis nunc eu metus maximus, eu iaculis turpis rutrum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur gravida porta libero eu porttitor. \n" +
                "\n" +
                "Donec sit amet leo viverra, rutrum quam ut, hendrerit massa.\n" +
                "\n" +
                "Mauris pulvinar efficitur mi, quis mollis urna tempus vitae. Aliquam dignissim leo arcu, sed eleifend tortor sodales nec. In suscipit metus in metus vestibulum porttitor.\n" +
                "\n" +
                "Cras dictum massa orci, sit amet commodo elit sollicitudin pulvinar. Suspendisse fermentum metus et est pretium tempus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non velit tellus. Donec pellentesque nec velit fermentum ullamcorper. \n" +
                "\n" +
                "Morbi nec mi purus. Nullam convallis eu erat ut ornare. Sed vel mi condimentum mauris dapibus venenatis. Aenean ligula magna, porta aliquet pellentesque sed, porttitor a enim. Curabitur scelerisque orci vel turpis pulvinar imperdiet. Ut auctor porttitor ex. \n" +
                "\n" +
                "Curabitur mattis nunc eu metus maximus, eu iaculis turpis rutrum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur gravida porta libero eu porttitor. \n" +
                "\n" +
                "Donec sit amet leo viverra, rutrum quam ut, hendrerit massa.\n" +
                "\n" +
                "Mauris pulvinar efficitur mi, quis mollis urna tempus vitae. Aliquam dignissim leo arcu, sed eleifend tortor sodales nec. In suscipit metus in metus vestibulum porttitor.\n" +
                "\n" +
                "Cras dictum massa orci, sit amet commodo elit sollicitudin pulvinar. Suspendisse fermentum metus et est pretium tempus. ");
    }

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
