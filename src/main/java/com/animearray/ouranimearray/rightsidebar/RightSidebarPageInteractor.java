package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.DAOs.UserAnime;

public class RightSidebarPageInteractor {
    private final RightSidebarPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    private UserAnime userAnimeData;

    public RightSidebarPageInteractor(RightSidebarPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
        createLoggedInBinding();
        createWatchStatusNotSetBinding();
    }

    public void getUserAnimeData() {
        userAnimeData = databaseFetcher.getUserAnimeData(viewModel.getAnime().id(), viewModel.getCurrentUser().id());
    }

    public void updateUserAnimeData() {
        viewModel.setUserAnimeData(userAnimeData);
    }

    public void setEpisodeWatched() {
        databaseFetcher.setEpisodeWatched(viewModel.getAnime().id(), viewModel.getCurrentUser().id(), viewModel.userAnimeDataProperty().getWatchedEpisodes());
    }

    public void updateEpisodeWatched() {
        viewModel.setUserEpisodesWatched(userAnimeData.watchedEpisodes());
    }

    public void setAnimeWatchStatus() {
        // Get first letter
        String watchStatus = viewModel.getUserWatchStatus().toString().substring(0, 1).toUpperCase();
        databaseFetcher.setAnimeWatchStatus(viewModel.getAnime().id(), viewModel.getCurrentUser().id(), watchStatus);
    }

    public void updateAnimeWatchStatus() {
        viewModel.setUserWatchStatus(userAnimeData.watchStatus());
    }

    public void setUserAnimeScore() {
        databaseFetcher.setUserAnimeScore(viewModel.getAnime().id(), viewModel.getCurrentUser().id(), viewModel.userAnimeDataProperty().getScore().getRating());
    }

    public void updateUserAnimeScore() {
        System.out.println(viewModel.getUserScore());
        viewModel.setUserScore(viewModel.getUserScore());
    }

    public void createLoggedInBinding() {
        viewModel.loggedInProperty().bind(viewModel.currentUserProperty().isNotNull());
    }

    public void createWatchStatusNotSetBinding() {
        viewModel.watchStatusNotSetProperty().bind(viewModel.userWatchStatusProperty().isNull());
    }
}
