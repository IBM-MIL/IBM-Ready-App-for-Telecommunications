/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.offers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanModelImpl;
import com.ibm.mil.readyapps.telco.mydata.DataPresenter;
import com.ibm.mil.readyapps.telco.mydata.DataRecyclerAdapter;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Binder to append the My Apps binder
 * to the corresponding recyclerView ViewHolder
 */
public class MyAppsBinder extends DataBinder<MyAppsBinder.MyAppsViewHolder> {
    private final DataPresenter presenter;
    private final DataRecyclerAdapter dataRecyclerAdapter;
    private final Context context;
    private List<Offer> appOffers;
    private final BasePlanModelImpl basePlanModel;

    /**
     * Constructor to initialize the MyAppsBinder
     *
     * @param dataBindAdapter the adapter that uses this binder to populate the recyclerView
     * @param context context from the main activity used to access application resources
     * @param presenter instance of the dataPresenter to access methods for updating model and view
     */
    public MyAppsBinder(DataBindAdapter dataBindAdapter, Context context, DataPresenter presenter) {
        super(dataBindAdapter);
        this.context = context;
        basePlanModel = new BasePlanModelImpl();
        this.dataRecyclerAdapter = (DataRecyclerAdapter) dataBindAdapter;
        this.presenter = presenter;
    }

    /**
     * creates a new ViewHolder using the provided xml layout
     *
     * @param viewGroup the parent ViewGroup that this ViewHolder will inflate
     * specifies the xml layout that the binder should use to create the view
     * @return the inflated view
     */
    @Override
    public MyAppsViewHolder newViewHolder(ViewGroup viewGroup) {
        View myAppsView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_my_apps, viewGroup, false);
        return new MyAppsViewHolder(myAppsView);
    }

    /**
     * @param myAppsViewHolder the ViewHolder to use onBind
     *                   has all the associated views in this layout
     * @param position the position of this ViewHolder in the binder
     */
    @Override
    public void bindViewHolder(MyAppsViewHolder myAppsViewHolder, int position) {
        Offer appOffer = appOffers.get(position);
        myAppsViewHolder.appImage.setImageResource(appOffer.getCardIcon());
        myAppsViewHolder.appOfferCost.setText(appOffer.getLocalizedCost());
        myAppsViewHolder.appOfferTitle.setText(appOffer.getTitle());
        myAppsViewHolder.addApp.setText(context.getString(R.string.add_app_offer));
        myAppsViewHolder.bottom_line.setVisibility(View.VISIBLE);
        if (position == appOffers.size() - 1) {
            myAppsViewHolder.bottom_line.setVisibility(View.GONE);
        }
        if(appOffer.isAppOffer()){
            myAppsViewHolder.appOfferCost.setText(appOffer.getLocalizedCost() + context.getString(R.string.per_cycle_charge));
        }
    }

    @Override
    public int getItemCount() {
        return appOffers.size();
    }

    /**
     * Add all available appOffers to the binder dataset
     * @param appOffers to add
     */
    public void addAll(List<Offer> appOffers) {
        this.appOffers = appOffers;
        notifyBinderDataSetChanged();
    }

    /**
     * Get position of the app offer in the appOffer list
     * @param position position of recyclerView that was accessed
     * @return position of the app offer with respect to the appOffer list not the recyclerView
     */
    private int getMyAppPosition(int position) {
        int BINDER_OFFSET = 3;
        return position - BINDER_OFFSET - dataRecyclerAdapter.getAppUsageBinder().getItemCount();
    }

    /**
     * Update the navigation bar to
     * reflect the base plan changes
     *
     * @param addedCost to update
     */
    private void updateBasePlan(double addedCost) {
        basePlanModel.updateBaseCost(addedCost);
    }

    public void add(Offer offer) {
        appOffers.add(offer);
        notifyBinderDataSetChanged();
    }

    /**
     * Remove an appOffer from the binder when user adds to base plan
     * @param offer to remove
     */
    public void removeAppOffer(Offer offer) {
        int indexToRemove = getIndex(offer);
        if(indexToRemove != -1) {
            appOffers.remove(indexToRemove);
            notifyDataSetChanged();
        }
    }

    /**
     * Given an offer, find the index in the offer list
     * If not there, return -1
     * @param offer to find
     * @return index if found, -1 if not
     */
    private int getIndex(Offer offer) {
        for(int i=0; i<appOffers.size(); i++)
        {
            if((offer.getAppName()).equals(appOffers.get(i).getAppName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Define the MyAppsViewHolder
     * inject views that need to be updated in the ViewHolder onBind
     */
    public class MyAppsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.app_image) ImageView appImage;
        @Bind(R.id.app_offer_title) RobotoTextView appOfferTitle;
        @Bind(R.id.app_offer_cost) RobotoTextView appOfferCost;
        @Bind(R.id.add_app) RobotoTextView addApp;
        @Bind(R.id.bottom_line) View bottom_line;

        public MyAppsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            addApp.setOnClickListener(this);
        }

        /**
         * On click listener for adding an app offer
         *
         * @param view the view that was clicked
         */
        @Override
        public void onClick(View view) {
            if (view.equals(addApp)) {
                int position = getAdapterPosition();
                Offer appOffer = appOffers.get(getMyAppPosition(position));
                presenter.addAppOffer(appOffer);
                updateBasePlan(appOffer.getCost());
            }
        }
    }
}
