package com.airbyte.dorm.dto;

public class LogHistoryDTO extends ParentDTO {
    private String url;
    private String doer;
    private String action;
    private String date;
    private String hour;
    private String category;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDoer() {
        return doer;
    }

    public void setDoer(String doer) {
        this.doer = doer;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
