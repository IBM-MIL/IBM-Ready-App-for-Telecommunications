/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.cycles;

import rx.Observable;

/**
 * Methods for getting, setting and updating cycle fields
 */

public interface CycleModel {

    Observable<Cycle> getDataCycleUpdates();

    Observable<Cycle> getTalkCycleUpdates();

    Observable<Cycle> getTextCycleUpdates();

    Cycle getDataCycle();

    Cycle getTalkCycle();

    Cycle getTextCycle();

    void updateDataCycle(Cycle cycle);

    void updateTalkCycle(Cycle cycle);

    void updateTextCycle(Cycle cycle);

    void setLimit(int type, int currentAmount);

}
