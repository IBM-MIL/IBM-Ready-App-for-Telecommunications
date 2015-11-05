/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.myplan;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.activities.MainActivity;
import com.ibm.mil.readyapps.telco.baseplan.BasePlan;
import com.ibm.mil.readyapps.telco.cycles.Cycle;
import com.ibm.mil.readyapps.telco.cycles.CycleBinder;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.offers.OfferBinder;
import com.ibm.mil.readyapps.telco.offers.OfferModel;
import com.ibm.mil.readyapps.telco.offers.OfferModelImpl;
import com.ibm.mil.readyapps.telco.utils.RecyclerDivider;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Fragment for My Plan tab of app.
 */
public class MyPlanFragment extends Fragment implements MyPlanView {
    private Resources resources;
    @Bind(R.id.recyclerView)RecyclerView recyclerView;
    private MyPlanPresenter presenter;
    private List<Offer> offers;
    private BasePlan basePlan;
    private Cycle dataCycle;
    private Cycle talkCycle;
    private Cycle textCycle;
    private boolean alreadyInitializedRecyclerList = false;
    private MyPlanRecyclerAdapter planAdapter;
    private OfferModel offerModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_plan, container, false);
        ButterKnife.bind(this, rootView);

        CoordinatorLayout layout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
        resources = getActivity().getResources();
        offerModel = new OfferModelImpl();
        presenter = new MyPlanPresenterImpl(this);
        ((MainActivity) getActivity()).setMyPlanPresenter(presenter);
        planAdapter = new MyPlanRecyclerAdapter(getActivity(), layout);

        presenter.getBasePlanInfo();
        presenter.getOffers(getActivity());
        presenter.getDataCycle();
        presenter.getTalkCycle();
        presenter.getTextCycle();

        return rootView;
    }

    /**
     * Initialize the recycler view with adapter and add subscribers.
     */
    private void initRecyclerView() {
        CycleBinder cycleBinder = planAdapter.getCycleBinder();
        OfferBinder offerBinder = planAdapter.getOfferBinder();
        recyclerView.setAdapter(planAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        presenter.acceptOffers(offerBinder.getOfferAcceptStream());
        presenter.dismissOffer(offerBinder.getOfferDismissStream());
        presenter.dismissOffer(offerModel.getUndoOfferRemoveStream());
        presenter.undoAcceptOffer(offerBinder.getUndoAcceptStream());
    }

    /**
     * Populate the recycler view with the relevant data.
     */
    private void populateRecyclerList() {
        planAdapter.setBasePlanInfo(basePlan);
        planAdapter.setDataCycle(dataCycle);
        planAdapter.setTalkCycle(talkCycle);
        planAdapter.setTextCycle(textCycle);
        //Accepted offer header
        planAdapter.setAcceptedOfferHeader(new RecyclerDivider(resources.getString(R.string.plan_picks_header), 0));
        planAdapter.setOffers(offers);
    }

    /**
     * Update the offers property and set offers on adapter.
     *
     * @param offers the list of offers to set
     */
    @Override
    public void displayOffers(List<Offer> offers) {
        this.offers = offers;
        planAdapter.setOffers(offers);
    }

    /**
     * Populate recycler view with updated cycle.
     *
     * @param cycle the updated cycle
     */
    @Override
    public void displayTalkCycle(Cycle cycle) {
        talkCycle = cycle;
        if (alreadyInitializedRecyclerList) {
            populateRecyclerList();
        }
    }

    /**
     * Populate recycler list with updated cycle info.
     *
     * @param cycle the updated cycle to update view with
     */
    @Override
    public void displayTextCycle(Cycle cycle) {
        this.textCycle = cycle;
        if (alreadyInitializedRecyclerList) {
            populateRecyclerList();
        } else {
            initRecyclerView();
            alreadyInitializedRecyclerList = true;
            populateRecyclerList();
        }
    }

    /**
     * Populate recycler list with updated cycle info.
     *
     * @param cycle the updated cycle to update view with
     */
    @Override
    public void displayDataCycle(Cycle cycle) {
        dataCycle = cycle;
        if (alreadyInitializedRecyclerList) {
            populateRecyclerList();
        }
    }

    /**
     * Add an offer to the list.
     *
     * @param offer the offer to add
     */
    @Override
    public void displayOffer(Offer offer) {
        planAdapter.addOffer(offer);
    }

    /**
     * Update the data cycle in the adapter.
     *
     * @param cycle the new cycle
     */
    @Override
    public void updateDataCycle(Cycle cycle) {
        planAdapter.setDataCycle(cycle);
    }

    /**
     * Update the talk cycle in the adapter.
     *
     * @param cycle the new cycle
     */
    @Override
    public void updateTalkCycle(Cycle cycle) {
        planAdapter.setTalkCycle(cycle);
    }

    /**
     * Update the text cycle in the adapter
     *
     * @param cycle the new cycle
     */
    @Override
    public void updateTextCycle(Cycle cycle) {
        planAdapter.setTextCycle(cycle);
    }

    /**
     * Update basePlan property and change base plan on adapter.
     *
     * @param basePlan the new base plan info
     */
    @Override
    public void displayBasePlanInfo(BasePlan basePlan) {
        this.basePlan = basePlan;
        planAdapter.setBasePlanInfo(basePlan);
    }

    /**
     * Remove an offer from the adapter.
     *
     * @param offer the offer to remove
     */
    @Override
    public void updateRemove(Offer offer){
        planAdapter.removeOffer(offer);
    }

}
