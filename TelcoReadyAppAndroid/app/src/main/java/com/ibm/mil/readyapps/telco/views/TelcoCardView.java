/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.ibm.mil.readyapps.telco.R;

import butterknife.ButterKnife;

/**
 * A custom CardView for the Offer Cards.
 */
public class TelcoCardView extends CardView {

    public TelcoCardView(Context context) {
        super(context);
        init();
    }

    public TelcoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TelcoCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.telco_card_layout_view, this);
        ButterKnife.bind(view);
    }

}
