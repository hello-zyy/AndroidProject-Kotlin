package com.hjq.demo.ui.obj;

public class UserProductInfo {
    private String label;
    private String name;
    private String selectLabel;
    private String url;


    public UserProductInfo() {
    }

    public UserProductInfo(String label, String name, String url) {
        this.label = label;
        this.name = name;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserProductObj{" +
                "label='" + label + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
