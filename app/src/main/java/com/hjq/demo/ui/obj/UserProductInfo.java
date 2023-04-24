package com.hjq.demo.ui.obj;

public class UserProductInfo {
    private String label;
    private String secondary_label;
    private String describe;
    private String originalPrice;
    private String memberPrice;
    private String url;


    public UserProductInfo() {
    }

    public UserProductInfo(String label, String secondary_label, String describe, String originalPrice, String memberPrice, String url) {
        this.label = label;
        this.secondary_label = secondary_label;
        this.describe = describe;
        this.originalPrice = originalPrice;
        this.memberPrice = memberPrice;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSecondary_label() {
        return secondary_label;
    }

    public void setSecondary_label(String secondary_label) {
        this.secondary_label = secondary_label;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(String memberPrice) {
        this.memberPrice = memberPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserProductInfo{" +
                "label='" + label + '\'' +
                ", secondary_label='" + secondary_label + '\'' +
                ", describe='" + describe + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", memberPrice='" + memberPrice + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
