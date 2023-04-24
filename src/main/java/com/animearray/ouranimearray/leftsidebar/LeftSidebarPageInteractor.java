package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.model.DatabaseFetcher;

public class LeftSidebarPageInteractor {
    private final LeftSidebarPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    public LeftSidebarPageInteractor(LeftSidebarPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }
}
