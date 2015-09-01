/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.recharge;

import com.ibm.mil.readyapps.telco.R;
import com.ibm.mil.readyapps.telco.utils.PlanConstants;

/**
 * Class for creating different recharges based on the type passed in.
 */
final class RechargeFactory {

    /**
     * Notify attempted instantiators that this is non-instantiable as it is only
     * a helper factory class.
     */
    private RechargeFactory() {
        throw new AssertionError(RechargeFactory.class.getName() + " is non-instantiable");
    }

    /**
     * Decide what type of recharge to make and then call a helper method
     * to return that type of recharge.
     *
     * @param type the type of recharge wanted
     * @param title the title for the recharge screen
     * @return the built recharge object
     */
    public static Recharge createRecharge(@PlanConstants.Type int type, String title) {
        switch (type) {
            case PlanConstants.DATA:
                return buildDataRecharge(title);
            case PlanConstants.TALK:
                return buildTalkRecharge(title);
            case PlanConstants.TEXT:
                return buildTextRecharge(title);
        }

        return null;
    }

    /**
     * Construct and return a data recharge.
     *
     * @param title the title for data recharges
     * @return the Recharge object built
     */
    private static Recharge buildDataRecharge(String title) {
        Recharge.Amount amount = new Recharge.Amount(1, PlanConstants.DATA_STEP_AMOUNT);
        Recharge.MetaData metaData = new Recharge.MetaData(title, PlanConstants.DATA_UNIT,
                R.drawable.data_dark_gray);

        return new Recharge(amount, PlanConstants.DATA_DOLLARS_PER_STEP, metaData);
    }

    /**
     * Construct and return a talk recharge.
     *
     * @param title the title for talk recharges
     * @return the Recharge object built
     */
    private static Recharge buildTalkRecharge(String title) {
        Recharge.Amount amount = new Recharge.Amount(25, PlanConstants.TALK_STEP_AMOUNT);
        Recharge.MetaData metaData = new Recharge.MetaData(title, PlanConstants.TALK_UNIT,
                R.drawable.talk_dark_gray);

        return new Recharge(amount, PlanConstants.TALK_DOLLARS_PER_STEP, metaData);
    }

    /**
     * Construct and return a text recharge.
     *
     * @param title the title for text recharges
     * @return the Recharge object built
     */
    private static Recharge buildTextRecharge(String title) {
        Recharge.Amount amount = new Recharge.Amount(50, PlanConstants.TEXT_STEP_AMOUNT);
        Recharge.MetaData metaData = new Recharge.MetaData(title, PlanConstants.TEXT_UNIT,
                R.drawable.text_dark_gray);

        return new Recharge(amount, PlanConstants.TEXT_DOLLARS_PER_STEP, metaData);
    }

}
