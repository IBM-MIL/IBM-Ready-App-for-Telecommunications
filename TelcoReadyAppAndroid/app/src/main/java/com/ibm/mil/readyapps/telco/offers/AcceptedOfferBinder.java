/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.offers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanModelImpl;
import com.ibm.mil.readyapps.telco.cycles.CycleModel;
import com.ibm.mil.readyapps.telco.cycles.CycleModelImpl;
import com.ibm.mil.readyapps.telco.mydata.DataRecyclerAdapter;
import com.ibm.mil.readyapps.telco.termsconditions.TermsConditionsActivity;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Binder to append the user data/talk/text accepted offer information
 * to the corresponding recyclerView ViewHolder
 */
public class AcceptedOfferBinder extends DataBinder<AcceptedOfferBinder.OfferViewHolder> {
    private final CoordinatorLayout snackbar_layout;
    private final Context context;
    private DataRecyclerAdapter dataRecyclerAdapter;
    private final Resources resources;
    private long mLastClickTime = 0;
    private final int BINDER_OFFSET = 3;
    private List<Offer> offers;
    private Offer originalOffer;
    private final BasePlanModelImpl basePlanModel;
    private final OfferModel offerModel;
    private final PublishSubject<Offer> removeOfferStream;
    private static final CycleModel cycleModel = new CycleModelImpl();

    /**
     * Constructor to initialize the cycle binder
     *
     * @param dataBindAdapter the adapter that uses this binder to populate the recyclerView
     * @param layout coordinator layout from the main activity
     *               used for inflating the snackBar
     * @param context context from the main activity
     *               used to access application resources
     */
    public AcceptedOfferBinder(DataBindAdapter dataBindAdapter, CoordinatorLayout layout, Context context) {
        super(dataBindAdapter);
        this.snackbar_layout = layout;
        removeOfferStream = PublishSubject.create();
        this.context = context;
        offerModel = new OfferModelImpl();
        resources = context.getResources();
        this.basePlanModel = new BasePlanModelImpl();
        if (dataBindAdapter instanceof DataRecyclerAdapter)
            dataRecyclerAdapter = (DataRecyclerAdapter) dataBindAdapter;
    }

    /**
    * creates a new ViewHolder using the provided xml layout
     *
    * @param viewGroup the parent ViewGroup that this ViewHolder will inflate
    * specifies the xml layout that the binder should use to create the view
    * @return the inflated view
    */
    @Override
    public OfferViewHolder newViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.telco_card_layout_view, viewGroup, false);
        return new OfferViewHolder(view);
    }

    /**
     * @param offerViewHolder the ViewHolder to use onBind
     *                   has all the associated views in this layout
     * @param position the position of this ViewHolder in the binder
     */
    @Override
    public void bindViewHolder(OfferViewHolder offerViewHolder, int position) {
        Offer offer = offers.get(position);
        offerViewHolder.iconImageView.setImageResource(offer.getCardIcon());
        offerViewHolder.titleTextView.setText(offer.getTitle());
        offerViewHolder.bodyTextView.setText(offer.getBody());
        offerViewHolder.costTextView.setText(offer.getLocalizedCost());
        offerViewHolder.removeOfferBtn.setText(context.getString(R.string.card_remove));
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    /**
     * Add all accepted offers for data/talk/text to the binder dataset
     * @param offers to add
     */
    public void addAll(List<Offer> offers) {
        this.offers = offers;
        notifyBinderDataSetChanged();
    }

    /**
     * @param offer to remove from accepted offers
     */
    public void remove(Offer offer) {
        offers.remove(offer);
        cycleModel.setLimit(offer.getType(), -offer.getAmountAddedToCycle());
        notifyDataSetChanged();
    }

    /**
     * @param offer to add to the accepted offers list
     */
    public void addNewCard(Offer offer) {
        offers.add(offer);
        notifyDataSetChanged();
    }

    /**
     * Get position of the accepted app offer
     * @param position position of recyclerView that was accessed
     * @return position of the accepted offer with respect to the
     *         accepted offers list not the recyclerView
     */
    private int getCardPosition(int position) {
        int usageCount = dataRecyclerAdapter.getAppUsageBinder().getItemCount();
        int myAppCount = dataRecyclerAdapter.getMyAppBinder().getItemCount();
        int BINDER_DATA_OFFSET = 4;
        return position - BINDER_DATA_OFFSET - (usageCount + myAppCount);
    }

    /**
     * Get the Publish Subject stream that observes when an accepted offer is removed
     *
     * @return observable for the offer remove update
     */
    public Observable<Offer> getRemoveOfferStream() {
        return removeOfferStream;
    }

    /**
     * create a SnackBar after changing base plan
     * the user gets ability to undo the plan change
     *
     * @param addedCost the amount added/removed after plan change
     *                  used to undo the plan change
     */
    private void createSnackBar(final int addedCost) {
        View.OnClickListener undoAction;
        /**
         * on click listener for undo of offer removal
         */
        undoAction = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                cycleModel.setLimit(originalOffer.getType(), originalOffer.getAmountAddedToCycle());
                offerModel.undoRemove(originalOffer);
                basePlanModel.updateCost(-addedCost, originalOffer.doesAffectBaseCost());
            }
        };
        //build the SnackBar and display it
        Snackbar.make(snackbar_layout, resources.getString(R.string.baseplan_snackbar), Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.undo), undoAction)
                .setActionTextColor(resources.getColor(R.color.light_indigo))
                .show();
    }

    /**
     * Starts a the TermsConditionsActivity and passes along data from the passed offer
     * @param selectedOffer The selected offer
     * @param offerContext The context of the selected offer
     */
    private void goToInfo(Offer selectedOffer, Context offerContext) {
        String offerTitle = selectedOffer.getTitle();
        String termsCond = selectedOffer.getTermsConditions();

        Intent intent = new Intent(offerContext, TermsConditionsActivity.class);
        intent.putExtra(TermsConditionsActivity.OFFER_TITLE, offerTitle);
        intent.putExtra(TermsConditionsActivity.OFFER_TERMS_CONDITIONS, termsCond);
        offerContext.startActivity(intent);
    }

    /**
     * Define the OfferViewHolder
     * inject views that need to be updated in the ViewHolder onBind
     */
    public class OfferViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.offer_title) RobotoTextView titleTextView;
        @Bind(R.id.offer_body) RobotoTextView bodyTextView;
        @Bind(R.id.card_icon) ImageView iconImageView;
        @Bind(R.id.costTextView) RobotoTextView costTextView;
        @Bind(R.id.accept) RobotoTextView removeOfferBtn;

        public OfferViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        /**
         * Remove accepted offer cards from data/talk/text
         * update the basePlan accordingly
         * provide options for undo
         *
         * @param view view that was clicked
         */
        @OnClick(R.id.accept)public void onRemoveClick(View view){

            //prevent multiple fast clicks
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            Offer offerToRemove;
            if (dataRecyclerAdapter != null) {
                offerToRemove = offers.get(getCardPosition(getAdapterPosition()));
            } else {
                offerToRemove = offers.get(getAdapterPosition() - BINDER_OFFSET);
            }
            originalOffer = offerToRemove;
            createSnackBar(-(int) offerToRemove.getCost());

            removeOfferStream.onNext(offerToRemove);
            basePlanModel.updateCost(-offerToRemove.getCost(), offerToRemove.doesAffectBaseCost());
        }

        /**
         * When the info icon button is clicked on a card, need to get the offer
         * that was selected and call the function goToInfo()
         * @param view The view that was clicked
         */
        @OnClick(R.id.info_icon_button)public void onInfoClick(View view){
            Offer selectedOffer;
            if (dataRecyclerAdapter != null) {
                selectedOffer = offers.get(getCardPosition(getAdapterPosition()));
            } else {
                selectedOffer = offers.get(getAdapterPosition() - BINDER_OFFSET);
            }
            goToInfo(selectedOffer, view.getContext());
        }
    }
}
