/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.cycles;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Implementation of the CycleModel
 */

public class CycleModelImpl implements CycleModel {

    private static final PublishSubject<Cycle> dataCycleStream = PublishSubject.create();
    private static final PublishSubject<Cycle> talkCycleStream = PublishSubject.create();
    private static final PublishSubject<Cycle> textCycleStream = PublishSubject.create();

    // cached data
    private static Cycle dataCycle;
    private static Cycle talkCycle;
    private static Cycle textCycle;

    /**
     * Get the stream for subscribing to data cycle updates
     *
     * @return the stream for data cycle
     */
    @Override public Observable<Cycle> getDataCycleUpdates() {
        return Observable.merge(Observable.just(getDataCycle()), dataCycleStream);
    }

    /**
     * Get the stream for subscribing to talk cycle updates
     *
     * @return the stream for talk cycle
     */
    @Override public Observable<Cycle> getTalkCycleUpdates() {
        return Observable.merge(Observable.just(getTalkCycle()), talkCycleStream);
    }

    /**
     * Get the stream for subscribing to text cycle updates
     *
     * @return the stream for text cycle
     */
    @Override public Observable<Cycle> getTextCycleUpdates() {
        return Observable.merge(Observable.just(getTextCycle()), textCycleStream);
    }

    /**
     * Get the initial data cycle information when dataCycle cache is still null
     *
     * @return current dataCycle
     */
    @Override public Cycle getDataCycle() {
        if (dataCycle != null) {
            return dataCycle;
        }

        dataCycle = new Cycle(R.drawable.data_dark_gray, PlanConstants.INITIAL_USED_DATA, PlanConstants.INITIAL_DATA_AMOUNT,
                PlanConstants.DATA_UNIT, PlanConstants.DATA);

        return dataCycle;
    }

    /**
     * Get the initial talk cycle information when talkCycle cache is still null
     *
     * @return current talkCycle
     */
    @Override public Cycle getTalkCycle() {
        if (talkCycle != null) {
            return talkCycle;
        }

        talkCycle = new Cycle(R.drawable.talk_dark_gray, PlanConstants.INITIAL_USED_TALK, PlanConstants.INITIAL_TALK_AMOUNT,
                PlanConstants.TALK_UNIT, PlanConstants.TALK);

        return talkCycle;
    }

    /**
     * Get the initial text cycle information when textCycle cache is still null
     *
     * @return current textCycle
     */
    @Override public Cycle getTextCycle() {
        if (textCycle != null) {
            return textCycle;
        }

        textCycle = new Cycle(R.drawable.text_dark_gray, PlanConstants.INITIAL_USED_TEXT, PlanConstants.INITIAL_TEXT_AMOUNT,
                PlanConstants.TEXT_UNIT, PlanConstants.TEXT);

        return textCycle;
    }

    /**
     * Update current data cycle to reflect changes
     *
     * @param cycle the new data cycle object to update to
     */
    @Override public void updateDataCycle(Cycle cycle) {
        dataCycle = cycle;
        dataCycleStream.onNext(dataCycle);
    }

    /**
     * Update current talk cycle to reflect changes
     *
     * @param cycle the new talk cycle object to update to
     */
    @Override public void updateTalkCycle(Cycle cycle) {
        talkCycle = cycle;
        talkCycleStream.onNext(talkCycle);
    }

    /**
     * Update current text cycle to reflect changes
     *
     * @param cycle the new text cycle object to update to
     */
    @Override public void updateTextCycle(Cycle cycle) {
        textCycle = cycle;
        textCycleStream.onNext(textCycle);
    }

    /**
     * Set the limit on a give cycle type
     *
     * @param type of the cycle
     * @param currentAmount to update the corresponding cycle with current limit amount
     */
    @Override
    public void setLimit(int type, int currentAmount) {
        switch (type) {
            case PlanConstants.DATA:
                dataCycle.setLimit(currentAmount + dataCycle.getLimit());
                updateDataCycle(dataCycle);
                break;
            case PlanConstants.TALK:
                talkCycle.setLimit(currentAmount + talkCycle.getLimit());
                updateTalkCycle(talkCycle);
                break;
            case PlanConstants.TEXT:
                textCycle.setLimit(currentAmount + textCycle.getLimit());
                updateTextCycle(textCycle);
            default:
                break;
        }
    }

}
