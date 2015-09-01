/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mytalk;

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
import com.ibm.mil.readyapps.telco.cycles.CycleBinder;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.offers.AcceptedOfferBinder;
import com.ibm.mil.readyapps.telco.offers.OfferModel;
import com.ibm.mil.readyapps.telco.offers.OfferModelImpl;
import com.ibm.mil.readyapps.telco.usage.Usage;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;
import com.ibm.mil.readyapps.telco.utils.RecyclerDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Fragment for Talk tab of app.
 */
public class TalkFragment extends Fragment implements TalkView {
    @Bind(R.id.my_talk_recyclerview) RecyclerView talkRecyclerView;
    private TalkRecyclerAdapter talkAdapter;
    private TalkPresenter presenter;
    private OfferModel offerModel;
    private List<Offer> acceptedOffers;
    private Usage dataUsage;
    private Cycle cycle;
    private Resources resources;
    private boolean alreadyInitializedRecyclerList = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_talk, container, false);
        ButterKnife.bind(this, view);

        offerModel = new OfferModelImpl();
        this.getClass().getSimpleName();
        CoordinatorLayout layout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        resources = getActivity().getResources();

        presenter = new TalkPresenterImpl(this, getActivity().getApplicationContext());
        talkAdapter = new TalkRecyclerAdapter(layout, getActivity().getApplicationContext());

        presenter.getAcceptedOffers();
        presenter.listenForUndoAccept();
        presenter.getUsage();
        presenter.getCycle();

        return view;
    }

    /**
     * Initialize the recycler view with adapter and add subscribers.
     */
    private void initRecyclerView() {
        CycleBinder cycleBinder = talkAdapter.getCycleBinder();
        AcceptedOfferBinder offerBinder = talkAdapter.getOfferBinder();
        talkRecyclerView.setAdapter(talkAdapter);
        talkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        presenter.removeOffer(offerBinder.getRemoveOfferStream());
        presenter.undoRemoveOffer(offerModel.getUndoOfferRemoveStream());
    }

    /**
     * Populate the recycler view with the relevant data.
     */
    private void populateRecyclerList() {
        talkAdapter.setCycle(cycle);
        talkAdapter.setIncomingOutgoing(dataUsage);
        talkAdapter.setDividerHeader(new RecyclerDivider(resources.getString(R.string.add_to_plan_header), 0));

        List<Offer> talkOffers = new ArrayList<>();
        for (Offer offer : acceptedOffers) {
            if ((offer.getType()) == PlanConstants.TALK) {
                talkOffers.add(offer);
            }
        }
        talkAdapter.setCardOffers(talkOffers);
    }

    /**
     * TalkView override for displaying the cycle as it emits changes.
     * Only initialize the recycler view if it has not been initialized
     * before.
     *
     * @param cycle the cycle that has been updated
     */
    @Override
    public void displayCycle(Cycle cycle) {
        this.cycle = cycle;
        if (alreadyInitializedRecyclerList) {
            populateRecyclerList();
        } else {
            initRecyclerView();
            alreadyInitializedRecyclerList = true;
            populateRecyclerList();
        }
    }

    /**
     * Update the data usage property.
     *
     * @param usage the new value
     */
    @Override
    public void displayUsage(Usage usage) {
        this.dataUsage = usage;
    }

    /**
     * Update the accepted offers property.
     *
     * @param offers the new list of offers
     */
    @Override
    public void displayAcceptedOffers(List<Offer> offers) {
        this.acceptedOffers = offers;
    }

    /**
     * Update the cycle in the adapter.
     *
     * @param cycle the cycle to update with
     */
    @Override
    public void updateCycleView(Cycle cycle) {
        talkAdapter.setCycle(cycle);
    }

    /**
     * Remove an offer from accepted offers and tell
     * adapter to remove the card.
     *
     * @param offer the offer to remove
     */
    @Override
    public void removeAcceptedOffer(Offer offer) {
        this.acceptedOffers.remove(offer);
        talkAdapter.removeCardOffer(offer);
    }

    /**
     * Add offer to accepted offers and add card
     * offer to adapter.
     *
     * @param offer the offer to add
     */
    @Override
    public void displayAcceptedOffer(Offer offer) {
        this.acceptedOffers.add(offer);
        if (offer.getType() == PlanConstants.TALK) {
            talkAdapter.addCardOffer(offer);
        }
    }
}
