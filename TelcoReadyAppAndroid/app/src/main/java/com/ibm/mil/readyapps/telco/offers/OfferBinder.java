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
import android.widget.RelativeLayout;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.activities.MainActivity;
import com.ibm.mil.readyapps.telco.baseplan.BasePlanModelImpl;
import com.ibm.mil.readyapps.telco.termsconditions.TermsConditionsActivity;
import com.ibm.mil.readyapps.telco.views.RobotoTextView;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter;
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder;

import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Binder to append the user myPlan offer information
 * to the corresponding recyclerView ViewHolder
 */
public class OfferBinder extends DataBinder<OfferBinder.OfferViewHolder> {
    private final CoordinatorLayout snackbar_layout;
    private final Context context;
    private final Resources resources;
    private long mLastClickTime = 0;
    private List<Offer> offers;
    private Offer offerToAccept;
    private int cardPosition;
    private final BasePlanModelImpl basePlanModel;
    private Snackbar snackbar;

    private final PublishSubject<Offer> offerAcceptStream;
    private final PublishSubject<Offer> offerDismissStream;
    private final PublishSubject<Offer> undoAcceptStream;

    /**
     * Constructor to initialize the cycle binder
     *
     * @param dataBindAdapter the adapter that uses this binder to populate the recyclerView
     * @param layout coordinator layout from the main activity
     *               used for inflating the snackBar
     * @param context context from the main activity
     *               used to access application resources
     */
    public OfferBinder(DataBindAdapter dataBindAdapter, CoordinatorLayout layout, Context context) {
        super(dataBindAdapter);
        this.snackbar_layout = layout;
        this.context = context;
        resources = context.getResources();
        offerAcceptStream = PublishSubject.create();
        offerDismissStream = PublishSubject.create();
        undoAcceptStream = PublishSubject.create();
        basePlanModel = new BasePlanModelImpl();
        initTabListener();
    }

    /**
     * listen for tab changes
     */
    private void initTabListener() {
        MainActivity.tabChangeStream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer tabPosition) {
                        if (tabPosition != MainActivity.MY_PLAN) {
                            removeAllShareCards();
                        }
                    }
                });
    }

    /**
     * remove share cards from my plan tab if any exist on tab change
     */
    private void removeAllShareCards() {
        for (Iterator<Offer> iterator = offers.iterator(); iterator.hasNext(); ) {
            Offer offer = iterator.next();
            if (offer.isShareCard()) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
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
        offerViewHolder.titleTextView.setText(offer.getTitle());
        offerViewHolder.bodyTextView.setText(offer.getBody());
        offerViewHolder.dismissButton.setText(context.getString(R.string.card_dismiss));
        offerViewHolder.iconImageView.setImageResource(offer.getCardIcon());

        String actionText = context.getString(R.string.card_accept);
        int acceptColor = context.getResources().getColor(R.color.light_indigo);
        int dismissColor = context.getResources().getColor(R.color.gray_ae);
        int bottomPortionColor = context.getResources().getColor(R.color.white);
        String costText = offer.getLocalizedCost();

        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) offerViewHolder.actionButton.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        if (offer.isShareCard()) {
            actionText = context.getString(R.string.card_share);
            acceptColor = context.getResources().getColor(R.color.white);
            dismissColor = context.getResources().getColor(R.color.transparent_white);
            bottomPortionColor = context.getResources().getColor(R.color.light_indigo);
            costText = "";
        }

        offerViewHolder.actionButton.setText(actionText);
        offerViewHolder.actionButton.setTextColor(acceptColor);
        offerViewHolder.dismissButton.setTextColor(dismissColor);
        offerViewHolder.cardBottomPortion.setBackgroundColor(bottomPortionColor);
        offerViewHolder.costTextView.setText(costText);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    /**
     * Add all offers for MyPlan to the binder dataset
     * @param offers to add
     */
    public void addAll(List<Offer> offers) {
        this.offers = offers;
        notifyBinderDataSetChanged();
    }

    /**
     * @param offer to add to the offers list
     */
    public void addNewOffer(Offer offer) {
        offers.add(offer);
        notifyBinderDataSetChanged();
    }

    /**
     * @param offer to remove from offers list
     */
    public void removeOffer(Offer offer) {
        offers.remove(offer);
        notifyDataSetChanged();
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
    public class OfferViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.offer_title)RobotoTextView titleTextView;
        @Bind(R.id.offer_body)RobotoTextView bodyTextView;
        @Bind(R.id.card_icon)ImageView iconImageView;
        @Bind(R.id.costTextView)RobotoTextView costTextView;
        @Bind(R.id.accept)RobotoTextView actionButton;
        @Bind(R.id.dismiss_button)RobotoTextView dismissButton;
        @Bind(R.id.card_bottom_portion)RelativeLayout cardBottomPortion;

        public OfferViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        /**
         * Accept tapped offer and add it to corresponding list
         *
         * @param view clicked
         */
        @OnClick(R.id.accept) public void onAcceptClick(View view) {
            int position = getAdapterPosition();
            cardPosition = getCardPosition(position);
            offerToAccept = offers.get(cardPosition);
            if (offerToAccept.isShareCard()) {
                ((MainActivity) context).tryToTweet(offerToAccept.getBody());
                removeFromMyPlanUI(cardPosition);
            } else {
                acceptOffer();
                transformToShareCard();
            }
        }

        /**
         * Dismiss tapped offer and remove from offers list
         *
         * @param view clicked
         */
        @OnClick(R.id.dismiss_button) public void onDismissClick(View view) {
            //prevent multiple fast clicks
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            dismissOffer(getAdapterPosition());
            if (snackbar != null) {
                snackbar.dismiss();
            }

        }

        /**
         * When the info icon button is clicked on a card, need to get the offer
         * that was selected and call the function goToInfo()
         * @param view The view that was clicked
         */
        @OnClick(R.id.info_icon_button)public void onInfoClick(View view){
            Offer selectedOffer;
            int position = getAdapterPosition();
            int cardPosition = getCardPosition(position);
            selectedOffer = offers.get(cardPosition);
            goToInfo(selectedOffer, view.getContext());
        }
    }

    /**
     * Get position of the offer
     * @param position position of recyclerView that was accessed
     * @return position of the offer with respect to the
     *         offers list not the recyclerView
     */
    private int getCardPosition(int position) {
        int BINDER_OFFSET = 3;
        return position - BINDER_OFFSET;
    }

    /**
     * Change UI of an accepted card to a Twitter shared card
     * update views accordinly
     */
    private void transformToShareCard() {
        Offer copyPlaceholderOffer = new Offer(offerToAccept);
        copyPlaceholderOffer.setShareCard();
        copyPlaceholderOffer.setCardIcon(R.drawable.twitter);
        copyPlaceholderOffer.setTitle(context.getString(R.string.card_tell_friends));
        copyPlaceholderOffer.setBody(context.getString(R.string.card_share_body));
        copyPlaceholderOffer.setCostZero();
        offers.set(cardPosition, copyPlaceholderOffer);
        notifyDataSetChanged();
    }

    /**
     * Accept selected offer
     * update basePlan
     * provide undo option
     */
    private void acceptOffer() {
        if (offerToAccept.doesAffectBaseCost()) {
            basePlanModel.updateBaseCost(offerToAccept.getCost());
        } else {
            basePlanModel.updateAddonCost(offerToAccept.getCost());
        }
        createSnackBar(offerToAccept);
        offerAcceptStream.onNext(offerToAccept);
    }

    /**
     * Dismiss selected offer
     * remove from offer list and view
     *
     * @param position of the offer to be dismissed
     */
    private void dismissOffer(int position) {
        int cardPosition = getCardPosition(position);
        Offer offerToDismiss = offers.get(cardPosition);
        removeFromMyPlanUI(cardPosition);
        offerDismissStream.onNext(offerToDismiss);
    }

    /**
     * Remove offer from UI
     * @param position to remove
     */
    private void removeFromMyPlanUI(int position) {
        offers.remove(position);
        notifyBinderItemRemoved(position);
        notifyBinderItemRangeChanged(position, 1);
    }

    /**
     * create a SnackBar after changing base plan
     * the user gets ability to undo the plan change
     *
     * @param acceptedOffer the offer added/removed after plan change
     *                  used to undo the plan change
     */
    private void createSnackBar(final Offer acceptedOffer) {
        View.OnClickListener undoAction;
        //on click listener for undo of offer acceptance
        undoAction = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                offers.set(cardPosition, offerToAccept);
                undoAcceptStream.onNext(acceptedOffer);
                notifyDataSetChanged();
                basePlanModel.updateCost(-acceptedOffer.getCost(), acceptedOffer.doesAffectBaseCost());
            }
        };
        //build the SnackBar and display it
        snackbar = Snackbar.make(snackbar_layout, resources.getString(R.string.baseplan_snackbar), Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.undo), undoAction)
                .setActionTextColor(resources.getColor(R.color.light_indigo));

        snackbar.show();
    }

    /**
     * Get the Publish Subject stream that observes when an accepted offer is accepted
     *
     * @return observable for the offer accepted update
     */
    public Observable<Offer> getOfferAcceptStream() {
        return offerAcceptStream;
    }

    /**
     * Get the Publish Subject stream that observes when an accepted offer is dismissed
     *
     * @return observable for the offer dismissed update
     */
    public Observable<Offer> getOfferDismissStream() {
        return offerDismissStream;
    }

    /**
     * Get the Publish Subject stream that observes when the
     * user clicks "undo" on an accepted offer
     *
     * @return observable for the offer undo accept update
     */
    public Observable<Offer> getUndoAcceptStream() {
        return undoAcceptStream;
    }
}
