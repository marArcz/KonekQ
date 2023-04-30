package com.example.konekq;

public class TabItemModel {
    private int icon;
    private int badgeValue;



    public TabItemModel(int icon, int badgeValue) {
        this.icon = icon;
        this.badgeValue = badgeValue;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getBadgeValue() {
        return badgeValue;
    }

    public void setBadgeValue(int badgeValue) {
        this.badgeValue = badgeValue;
    }
}
