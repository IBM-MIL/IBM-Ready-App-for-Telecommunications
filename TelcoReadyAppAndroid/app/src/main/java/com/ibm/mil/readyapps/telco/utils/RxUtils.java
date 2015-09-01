package com.ibm.mil.readyapps.telco.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** Collection of Rx-related utility functions */
public final class RxUtils {

    private RxUtils() {
        throw new AssertionError(RxUtils.class.getName() + " is non-instantiable");
    }

    /** subscribes on background thread; observes on main thread */
    public static <T> Observable.Transformer<T, T> showBackgroundWork() {
        return new Observable.Transformer<T, T>() {
            @Override public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
