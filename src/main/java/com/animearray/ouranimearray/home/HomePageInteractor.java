package com.animearray.ouranimearray.home;

import com.animearray.ouranimearray.model.DatabaseFetcher;

public class HomePageInteractor {
    private final HomePageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    public HomePageInteractor(HomePageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }
}
