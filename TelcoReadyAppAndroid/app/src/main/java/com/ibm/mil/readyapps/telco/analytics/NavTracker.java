/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.readyapps.telco.analytics;

import java.util.Date;

/**
 * NavTracker is a class that maintains state for the user's current screen viewed and previous
 * screen viewed and maintains the values necessary for dwell time on each of the screens that
 * the user visits.
 */
public final class NavTracker {
        private static final NavTracker INSTANCE = new NavTracker();

        @AnalyticsCnsts.Page private String currentPage  = "";
        @AnalyticsCnsts.Page private String previousPage = "";

        private long currentTime = 0;
        private long previousTime = 0;

        private NavTracker() {
            if (INSTANCE != null) {
                throw new IllegalStateException("Already instantiated");
            }
        }

        public static NavTracker getInstance() {
            return INSTANCE;
        }

        /**
         * setScreen() sets the currentPage to the current screen (page) that the user is viewing.
         * @param newCurrentPage the page that the user is currently viewing.
         */
        public void setScreen(@AnalyticsCnsts.Page String newCurrentPage) {

            Date date = new Date();
            long newCurrentTime = date.getTime();

            if (newCurrentPage == null || currentPage.equals(newCurrentPage)) {
                return;
            }
            previousPage = currentPage;
            currentPage = newCurrentPage;

            previousTime = currentTime;
            currentTime = newCurrentTime;

            if (!previousPage.isEmpty() && !currentPage.isEmpty()) {
                long dwellTime = currentTime = previousTime;
                OperationalAnalyticsReporter.pageTransition(previousPage, currentPage, dwellTime);
            }

            MILAnalyticsReporter.setCurrentScreen(currentPage);
        }

        /**
         * getCurrentPage() returns the current page that the user is viewing.
         * @return currentPage the current page the user is viewing.
         */
        public @AnalyticsCnsts.Page String getCurrentPage() {
            return currentPage;
        }

        /**
         * getPreviousPage() returns the previous page that the user was viewing.
         * @return previousPage the previous page the user was viewing.
         */
        public @AnalyticsCnsts.Page String getPreviousPage() {
            return previousPage;
        }
}

