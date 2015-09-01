/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.mytext;

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
import com.ibm.mil.readyapps.telco.offers.AcceptedOfferBinder;
import com.ibm.mil.readyapps.telco.offers.Offer;
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
 * Fragment for Text tab of app.
 */
public class TextFragment extends Fragment implements TextView {
    @Bind(R.id.my_text_recyclerview) RecyclerView talkRecyclerView;
    private TextRecyclerAdapter textAdapter;
    private TextPresenter presenter;
    private List<Offer> acceptedOffers;
    private Usage dataUsage;
    private Cycle cycle;
    private boolean alreadyInitializedRecyclerList = false;
    private OfferModel offerModel;
    private Resources resources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        ButterKnife.bind(this, view);

        CoordinatorLayout layout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        resources = getActivity().getResources();

        offerModel = new OfferModelImpl();
        presenter = new TextPresenterImpl(this, getActivity().getApplicationContext());
        textAdapter = new TextRecyclerAdapter(layout, getActivity().getApplicationContext());

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
        CycleBinder cycleBinder = textAdapter.getCycleBinder();
        AcceptedOfferBinder offerBinder = textAdapter.getOfferBinder();
        talkRecyclerView.setAdapter(textAdapter);
        talkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        populateRecyclerList();

        presenter.removeOffer(offerBinder.getRemoveOfferStream());
        presenter.undoRemoveOffer(offerModel.getUndoOfferRemoveStream());
    }

    /**
     * Populate the recycler view with the relevant data.
     */
    private void populateRecyclerList() {
        textAdapter.setCycle(cycle);
        textAdapter.setIncomingOutgoing(dataUsage);
        textAdapter.setDividerHeader(new RecyclerDivider(resources.getString(R.string.add_to_plan_header), 0));

        List<Offer> textOffers = new ArrayList<>();
        for (Offer offer : acceptedOffers) {
            if ((offer.getType()) == PlanConstants.TEXT) {
                textOffers.add(offer);
            }
        }
        textAdapter.setCardOffers(textOffers);
    }

    /**
     * TextView override for displaying the cycle as it emits changes.
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
     * @param usage the new usage
     */
    @Override
    public void displayUsage(Usage usage) {
        this.dataUsage = usage;
    }

    /**
     * Update the accepted offers property.
     *
     * @param offers the new offers list
     */
    @Override
    public void displayAcceptedOffers(List<Offer> offers) {
        this.acceptedOffers = offers;
    }

    /**
     * Update the adapter with the new cycle.
     *
     * @param cycle the new cycle to be shown
     */
    @Override
    public void updateCycleView(Cycle cycle) {
        textAdapter.setCycle(cycle);
    }

    /**
     * Remove an offer from accepted offers array and
     * update adapter also.
     *
     * @param offer the offer to remove
     */
    @Override
    public void removeAcceptedOffer(Offer offer) {
        this.acceptedOffers.remove(offer);
        textAdapter.removeCardOffer(offer);
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
        if (offer.getType() == PlanConstants.TEXT) {
            textAdapter.setCardOffer(offer);
        }
    }
}
