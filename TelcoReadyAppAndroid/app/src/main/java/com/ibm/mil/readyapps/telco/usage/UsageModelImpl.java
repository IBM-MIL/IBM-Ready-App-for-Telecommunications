/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.usage;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ibm.mil.readyapps.telco.offers.Offer;
import com.ibm.mil.readyapps.telco.utils.JsonUtils;

import java.util.List;

import rx.Observable;

/**
 * Implementation of methods in UsageModel interface
 */
public class UsageModelImpl implements UsageModel {
    private static List<Usage> usages;
    private static Usage talkUsage;
    private static Usage textUsage;

    @Override
    public Observable<Usage> getUsages(Context context) {
        return Observable.from(createUsages(context));
    }

    @Override
    public Observable<Usage> getTalkUsage(Context context) {
        return Observable.just(createTalkUsage(context));
    }

    @Override
    public Observable<Usage> getTextUsage(Context context) {
        return Observable.just(createTextUsage(context));
    }

    /**
     * Set new values for an existing usage
     * Example: usage can go from limited to unlimited
     *
     * @param newUsage to set
     */
    @Override
    public void setNewUsage(Usage newUsage) {
        int index = indexToReplace(newUsage.getAppName());
        Usage usage = usages.get(index);
        newUsage.setIsUnlimited(true);
        newUsage.setLimit(usage.getLimit());
        usages.set(index, newUsage);
    }

    /**
     * Find the index of already existing app usage in usages
     * @param appName name of the applcation
     * @return index of the app usage
     */
    private int indexToReplace(String appName) {
        int index = 0;
            for(int i=0; i<usages.size(); i++){
                if (usages.get(i).getAppName().equals(appName)){
                    index = i;
                }
            }
        return index;
    }

    /**
     * Change an offer to a limited usage
     * @param offer to change
     * @return limited usage object
     */
    @Override
    public Usage setLimitedUsage(Offer offer){
        int index  = indexToReplace(offer.getAppName());
        Usage usage = usages.get(index);
        Usage newUsage = usage.offerToUsage(offer);
        newUsage.setIsUnlimited(false);
        newUsage.setLimit(usage.getLimit());
        usages.set(index, newUsage);
        return newUsage;
    }

    /**
     * Get app usages from the json file and save in cache
     *
     * @param context used in the JsonUtils
     * @return list of usages either from the json file or from cache
     */
    private List<Usage> createUsages(Context context) {
        if (usages != null) {
            return usages;
        }

        TypeToken<List<Usage>> token = new TypeToken<List<Usage>>() {
        };
        List<Usage> initialUsages = JsonUtils.parseJsonFile(context, "app_usages.json", token);

        for(Usage usage: initialUsages){
            int resId = context.getResources().getIdentifier(usage.getImageName(), "drawable", context.getPackageName());
            usage.setUsageImage(resId);
            usage.setSeekBarProgress((int) ((usage.getUsed() * 100 )/ usage.getLimit()));
        }
        usages = initialUsages;

        return usages;
    }

    /**
     * Get talk incoming/outgoing info from the json file and save in cache
     *
     * @param context used in the JsonUtils
     * @return talk usage either from the json file or from cache
     */
    private Usage createTalkUsage(Context context) {
        if (talkUsage != null) {
            return talkUsage;
        }
        TypeToken<List<Usage>> token = new TypeToken<List<Usage>>() {
        };
        List<Usage> initTalkIO = JsonUtils.parseJsonFile(context, "talk_io.json", token);
        talkUsage = initTalkIO.get(0);

        return talkUsage;
    }

    /**
     * Get text incoming/outgoing from the json file and save in cache
     *
     * @param context used in the JsonUtils
     * @return text usage either from the json file or from cache
     */
    private Usage createTextUsage(Context context) {
        if (textUsage != null) {
            return textUsage;
        }

        TypeToken<List<Usage>> token = new TypeToken<List<Usage>>() {
        };
        List<Usage> initTextIO = JsonUtils.parseJsonFile(context, "text_io.json", token);
        textUsage = initTextIO.get(0);

        return textUsage;
    }
}
