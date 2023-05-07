package com.animearray.ouranimearray.report;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.report.ReportPageModel;

public class ReportPageInteractor {
    private final ReportPageModel model;
    private final DatabaseFetcher databaseFetcher;

    public ReportPageInteractor(ReportPageModel model) {
        this.model = model;
        this.databaseFetcher = new DatabaseFetcher();
    }
}
