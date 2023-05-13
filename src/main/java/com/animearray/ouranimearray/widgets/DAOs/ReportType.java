package com.animearray.ouranimearray.widgets.DAOs;

public enum ReportType {
    Popularity("Popularity"),
    Score("Score"),
    UserScore("User Score");

    private final String reportType;

    ReportType(String reportType) {
        this.reportType = reportType;
    }

    @Override
    public String toString() {
        return reportType;
    }
}
