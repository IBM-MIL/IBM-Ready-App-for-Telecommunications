/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mydata;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.offers.AcceptedOfferBinder;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.offers.OfferModel;
import com.ibm.mil.readyapps.telco.offers.OfferModelImpl;
import com.ibm.mil.readyapps.telco.usage.Usage;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;
import com.ibm.mil.readyapps.telco.utils.RecyclerDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Fragment for Data tab of app.
 */
public class DataFragment extends Fragment implements DataView {
    private DataRecyclerAdapter dataAdapter;
    private Resources resources;
    @Bind(R.id.my_data_recyclerview)RecyclerView recyclerView;
    private DataPresenter presenter;
    private List<Offer> appOffers;
    private List<Offer> acceptedOffers;
    private List<Usage> usages;
    private Cycle cycle;
    private boolean alreadyInitializedRecyclerList = false;
    private Subscription myAppSubscription;
    private OfferModel offerModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        ButterKnife.bind(this, view);

        offerModel = new OfferModelImpl();
        resources = getActivity().getResources();
        CoordinatorLayout layout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        presenter = new DataPresenterImpl(this);
        dataAdapter = new DataRecyclerAdapter(layout, getActivity().getApplicationContext(), presenter);

        presenter.getAcceptedOffers();
        presenter.listenForUndoAccept();
        presenter.getAppOffers(getActivity());
        presenter.getAppUsages(getActivity());
        presenter.getCycle();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myAppSubscription.unsubscribe();
    }

    /**
     * Initialize the recycler view with adapter and add subscribers.
     */
    private void initRecyclerView() {
        AcceptedOfferBinder acceptedOfferBinder = dataAdapter.getOfferBinder();
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //anim test
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        animator.setAddDuration(200);
        animator.setRemoveDuration(200);

        myAppSubscription = presenter.addAppUsage(offerModel.getAppOfferStream());
        presenter.removeOffer(acceptedOfferBinder.getRemoveOfferStream());
        presenter.undoRemoveOffer(offerModel.getUndoOfferRemoveStream());
    }

    /**
     * Populate the recycler view with the relevant data.
     */
    private void populateDataRecyclerList() {
        //cycle
        dataAdapter.setCycle(cycle);
        //app usage header
        dataAdapter.setDividerHeader(new RecyclerDivider(resources.getString(R.string.app_usage_header), -1));
        //usages
        dataAdapter.setAppUsage(usages);
        //my apps header
        dataAdapter.setMyAppHeader(new RecyclerDivider(resources.getString(R.string.my_apps_header), -1));
        //my apps
        dataAdapter.setMyApps(appOffers);
        //Accepted offer header
        dataAdapter.setAcceptedOfferHeader(new RecyclerDivider(resources.getString(R.string.add_to_plan_header), 0));
        //accepted offers
        //filter
        List<Offer> dataOffers = new ArrayList<>();
        for(Offer offer : acceptedOffers){
            if(offer.getType() == PlanConstants.DATA){
                dataOffers.add(offer);
            }
        }
      dataAdapter.setCardOffers(dataOffers);
    }

    /**
     * DataView override for displaying the cycle as it emits changes.
     * Only initialize the recycler view if it has not been initialized
     * before.
     *
     * @param cycle the cycle that has been updated
     */
    @Override
    public void displayCycle(Cycle cycle) {
        this.cycle = cycle;
        if (alreadyInitializedRecyclerList) {
            populateDataRecyclerList();
        } else {
            initRecyclerView();
            alreadyInitializedRecyclerList = true;
            populateDataRecyclerList();
        }
    }

    /**
     * Update the app usages property.
     *
     * @param appUsages the new value to replace with
     */
    @Override
    public void displayAppUsages(List<Usage> appUsages) {
        this.usages = appUsages;
    }

    /**
     * Update the app offers property.
     *
     * @param appOffers the new value to replace with
     */
    @Override
    public void displayAppOffers(List<Offer> appOffers) {
        this.appOffers = appOffers;
    }

    /**
     * Update the accepted offers.
     *
     * @param acceptedOffers the offers to replace with
     */
    @Override
    public void displayAcceptedOffers(List<Offer> acceptedOffers) {
        this.acceptedOffers = acceptedOffers;
    }

    /**
     * Remove an offer from accepted offers array.
     *
     * @param offer the offer to remove
     */
    @Override
    public void removeAcceptedOffer(Offer offer) {
        this.acceptedOffers.remove(offer);
        dataAdapter.removeCardOffer(offer);
    }

    /**
     * Update the adapter with the offer we want to show.
     *
     * @param offer the offer to be shown
     */
    @Override
    public void displayCardOffer(Offer offer) {
        this.acceptedOffers.add(offer);
        if(offer.getType() == PlanConstants.DATA) {
            dataAdapter.setCardOffer(offer);
        }
    }

    /**
     * Update the adapter with the usage we want to show.
     *
     * @param newUsage the usage to be shown
     */
    @Override
    public void displayNewUsage(Usage newUsage) {
        dataAdapter.setNewUsage(newUsage);
    }

    /**
     * Update the adapter with the new cycle.
     *
     * @param cycle the new cycle to be shown
     */
    @Override
    public void updateCycleView(Cycle cycle) {
        dataAdapter.setCycle(cycle);
    }

    /**
     * Update the adapter with a new app offer.
     *
     * @param offer the app offer to be shown
     */
    @Override
    public void displayAppOffer(Offer offer) {
        dataAdapter.setAppOffer(offer);
    }

    /**
     * Remove an offer from the adapter.
     *
     * @param offer the offer to remove
     */
    @Override
    public void removeAppOffer(Offer offer) {
        dataAdapter.removeAppOffer(offer);
    }
}
