package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.WatchStatus;

public class RightSidebarPageInteractor {
    private final RightSidebarPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    private WatchStatus watchStatus;
    public RightSidebarPageInteractor(RightSidebarPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
        createLoggedInBinding();
    }

    public void setAnimeWatchStatus() {
        // Get first letter
        String watchStatus = viewModel.getWatchStatus().toString().substring(0, 1).toUpperCase();
        databaseFetcher.setAnimeWatchStatus(viewModel.getAnime().id(), viewModel.getCurrentUser().id(), watchStatus);
    }

    public void getAnimeWatchStatus() {
        watchStatus = databaseFetcher.getAnimeWatchStatus(viewModel.getAnime().id(), viewModel.getCurrentUser().id())
                .orElse(null);
    }

    public void updateAnimeWatchStatus() {
        viewModel.setWatchStatus(watchStatus);
    }

    public void createLoggedInBinding() {
        viewModel.loggedInProperty().bind(viewModel.currentUserProperty().isNotNull());
    }

    public void createWatchStatusNotSetBinding() {
        viewModel.watchStatusNotSetProperty().bind(viewModel.watchStatusProperty().isNull());
    }
}
