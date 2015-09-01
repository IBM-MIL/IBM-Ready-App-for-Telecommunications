package com.ibm.mil.readyapps.telco.hotspots;

public class HotSpotViewMock implements HotSpotView {
    private HotSpot hotSpot;

    @Override public void showHotSpotDetails(HotSpot hotSpot) {
        this.hotSpot = hotSpot;
    }

    public HotSpot getHotSpot() {
        return hotSpot;
    }

}
