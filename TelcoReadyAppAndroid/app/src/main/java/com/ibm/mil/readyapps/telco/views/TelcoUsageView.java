/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibm.mil.readyapps.telco.R;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Class for setting up a 'Telco usage view' which is essentially
 * a horizontal progress bar with an icon with optional text
 * above/below the progress bar. Used on all the tabs to show
 * percentage of data/talk/text already used for the month.
 */
public class TelcoUsageView extends LinearLayout {

    @Bind(R.id.usage_image) ImageView usageImage;
    @Bind(R.id.progress_bar) ProgressBar usageProgressBar;
    @Bind(R.id.usage_top_text) TextView topTextView;
    @Bind(R.id.bottom_left_text) TextView bottomLeftTextView;
    @Bind(R.id.bottom_right_text) TextView bottomRightTextView;
    private int percentUsed;
    private Drawable imageSource;
    private String topText;
    private String bottomLeftText;
    private String bottomRightText;
    private boolean progressBarHidden;

    /**
     * The constructor called when inflating the view from XML.
     * Used to read custom XML attributes and set up the view based
     * on those attributes.
     *
     * @param context the context of the view
     * @param attrs the attributes of the view
     */
    public TelcoUsageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TelcoUsageView, 0, 0);
        try {
            percentUsed = a.getInteger(R.styleable.TelcoUsageView_percentUsed, 50);
            imageSource = a.getDrawable(R.styleable.TelcoUsageView_image);
            topText = a.getString(R.styleable.TelcoUsageView_topText);
            bottomLeftText = a.getString(R.styleable.TelcoUsageView_bottomLeftText);
            bottomRightText = a.getString(R.styleable.TelcoUsageView_bottomRightText);
            progressBarHidden = a.getBoolean(R.styleable.TelcoUsageView_progressBarHidden, false);
        } finally {
            a.recycle();
        }
        init();
    }

    /**
     * Initialize the view with the attributes received from constructor.
     */
    private void init() {
        View view = inflate(getContext(), R.layout.telco_usage_view, this);
        ButterKnife.bind(view);

        setProgressBarUI();
        setUsageImageUI();
        setTextView(topTextView, topText);
        setTextView(bottomLeftTextView, bottomLeftText);
        setTextView(bottomRightTextView, bottomRightText);
    }

    /**
     * Set the progress of the progress bar or hide
     * the bar if the progressBarHidden attribute was
     * set to true.
     */
    private void setProgressBarUI() {
        if (progressBarHidden) {
            usageProgressBar.setVisibility(GONE);
        } else {
            usageProgressBar.setProgress(percentUsed);
            usageProgressBar.setVisibility(VISIBLE);
        }
    }

    /**
     * Set the icon for the view or hide it if there was
     * no image source specified in XML.
     */
    private void setUsageImageUI() {
        if (imageSource != null) {
            usageImage.setImageDrawable(imageSource);
            usageImage.setVisibility(VISIBLE);
        } else {
            usageImage.setVisibility(GONE);
        }
    }

    /**
     * Set the text for the specified textview or hide
     * the textview if there is no text for it.
     *
     * @param textView the textview to set text on
     * @param text the text to set on the textview
     */
    private void setTextView(TextView textView, String text) {
        if (text != null && !text.isEmpty()) {
            textView.setText(text);
            textView.setVisibility(VISIBLE);
        } else {
            textView.setVisibility(GONE);
        }
    }

    /**
     * Change the percent used property and update the UI.
     *
     * @param percentUsed the percent used to set for the progress bar
     */
    public void setPercentUsed(int percentUsed) {
        this.percentUsed = percentUsed;
        setProgressBarUI();
    }

    /**
     * Set the image source for the view and then update the UI.
     *
     * @param imageSource the drawable wanted as the icon image
     */
    public void setImageSource(Drawable imageSource) {
        this.imageSource = imageSource;
        setUsageImageUI();
    }

    /**
     * Set the text for the bottom left text view.
     *
     * @param text the text to set for bottom left textview
     */
    public void setBottomLeftText(String text) {
        this.bottomLeftText = text;
        setTextView(bottomLeftTextView, text);
    }

    /**
     * Set the text for the bottom right text view.
     *
     * @param text the text to set for bottom right textview
     */
    public void setBottomRightText(String text) {
        this.bottomRightText = text;
        setTextView(bottomRightTextView, text);
    }

}
