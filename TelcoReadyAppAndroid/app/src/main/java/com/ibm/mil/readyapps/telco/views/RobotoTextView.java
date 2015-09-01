/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.utils.FontCache;

/**
 * Class for setting up a roboto text view with custom xml properties.
 */
public class RobotoTextView extends TextView {

    private String robotoFontName;

    /**
     * Constructor called to instantiate a roboto text view created from XML.
     * Used to set the font of the text view to a roboto type.
     *
     * @param context the context of the view
     * @param attrs the attributes of the view
     */
    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RobotoTextView, 0, 0);
        try {
            robotoFontName = a.getString(R.styleable.RobotoTextView_font);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }

        if (robotoFontName == null) {
            robotoFontName = "Roboto-Regular.ttf";
        }

        setFont(context, robotoFontName);
    }

    /**
     * Helper method for setting the font of the text view.
     *
     * @param context the context needed to retrieve a font from assets folder
     * @param robotoFont the type of roboto font to use
     */
    public void setFont(Context context, String robotoFont) {
        Typeface font = FontCache.get(robotoFont, context);
        this.setTypeface(font);
    }

}
