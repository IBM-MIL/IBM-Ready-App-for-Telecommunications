/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.offers;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.utils.RecyclerDivider;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Binder to append the Offer header
 * to the corresponding recyclerView ViewHolder
 */
public class OfferBinderHeader extends DataBinder<OfferBinderHeader.DataHeaderViewHolder> {
    private final Resources resources;
    private RecyclerDivider myAppHeader;


    /**
     * Constructor to initialize the offer binder
     *
     * @param dataBindAdapter the adapter that uses this binder to populate the recyclerView
     * @param context context from the main activity
     *               used to access application resources
     */
    public OfferBinderHeader(DataBindAdapter dataBindAdapter, Context context) {
        super(dataBindAdapter);
        resources = context.getResources();
    }

    /**
     * creates a new ViewHolder using the provided xml layout
     *
     * @param viewGroup the parent ViewGroup that this ViewHolder will inflate
     * specifies the xml layout that the binder should use to create the view
     * @return the inflated view
     */
    @Override
    public DataHeaderViewHolder newViewHolder(ViewGroup viewGroup) {
        View dataHeaderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offer_header, viewGroup, false);
        return new DataHeaderViewHolder(dataHeaderView);
    }

    /**
     * @param dataHeaderViewHolder the ViewHolder to use onBind
     *                   has all the associated views in this layout
     * @param position the position of this ViewHolder in the binder
     */
    @Override
    public void bindViewHolder(DataHeaderViewHolder dataHeaderViewHolder, int position) {
        dataHeaderViewHolder.headerLayout.setBackgroundColor(-1);
        dataHeaderViewHolder.dataHeader.setText(myAppHeader.getDividerText());
        int bgColor = myAppHeader.getDividerBGcolor();
        dataHeaderViewHolder.noOfferPlaceholder.setVisibility(View.GONE);
        if (bgColor != -1)
            dataHeaderViewHolder.headerLayout.setBackgroundColor(bgColor);
        if(new OfferModelImpl().noOffers()){
            dataHeaderViewHolder.noOfferPlaceholder.setText(resources.getString(R.string.no_offers));
            dataHeaderViewHolder.noOfferPlaceholder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * Set the header and update the binder dataset
     *
     * @param header to set
     */
    public void add(RecyclerDivider header) {
        this.myAppHeader = header;
        notifyBinderDataSetChanged();
    }

    /**
     * Define the DataHeaderViewHolder
     * inject views that need to be updated in the ViewHolder onBind
     */
    public class DataHeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.divider_text_view) RobotoTextView dataHeader;
        @Bind(R.id.header) LinearLayout headerLayout;
        @Bind(R.id.no_offer_placeholder) RobotoTextView noOfferPlaceholder;

        public DataHeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
