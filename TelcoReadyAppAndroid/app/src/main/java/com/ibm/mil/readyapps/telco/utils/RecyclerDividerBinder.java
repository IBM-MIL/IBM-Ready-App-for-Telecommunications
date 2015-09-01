/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.offers.OfferModelImpl;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import butterknife.ButterKnife;
import butterknife.Bind;

public class RecyclerDividerBinder extends DataBinder<RecyclerDividerBinder.DataHeaderViewHolder> {
    private RecyclerDivider header;

    public RecyclerDividerBinder(DataBindAdapter adapter) {
        super(adapter);
    }

    @Override
    public DataHeaderViewHolder newViewHolder(ViewGroup viewGroup) {
        View dataHeaderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offer_header, viewGroup, false);
        return new DataHeaderViewHolder(dataHeaderView);
    }

    @Override
    public void bindViewHolder(DataHeaderViewHolder dataHeaderViewHolder, int i) {
        dataHeaderViewHolder.headerLayout.setBackgroundColor(-1);
        dataHeaderViewHolder.dataHeader.setText(header.getDividerText());
        int bgColor = header.getDividerBGcolor();
        dataHeaderViewHolder.noOfferPlaceholder.setVisibility(View.GONE);
        if (bgColor != -1)
            dataHeaderViewHolder.headerLayout.setBackgroundColor(bgColor);
        if(new OfferModelImpl().noAcceptedOffers()){
            dataHeaderViewHolder.noOfferPlaceholder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void add(RecyclerDivider header) {
        this.header = header;
        notifyBinderDataSetChanged();
    }

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
