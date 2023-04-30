package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.DAOs.AnimeList;

import java.sql.SQLException;
import java.util.List;

public class LeftSidebarPageInteractor {
    private final LeftSidebarPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    private List<AnimeList> animeListData;
    public LeftSidebarPageInteractor(LeftSidebarPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }

    public void loadAnimeList() {
        animeListData = databaseFetcher.getAnimeLists(viewModel.getCurrentUser().id());
    }

    public void updateAnimeList() {
        viewModel.animeWatchListProperty().clear();
        viewModel.animeWatchListProperty().addAll(animeListData);
    }

    public void addAnimeToList() {
        databaseFetcher.addAnimeToList(viewModel.getAnimeToAdd().id(), viewModel.getAnimeListToAddTo().id());

        // Clear the animeToAdd and animeListToAddTo properties
        viewModel.setAnimeToAdd(null);
        viewModel.setAnimeListToAddTo(null);
    }

    public void createNewAnimeList() throws SQLException {
        databaseFetcher.createNewAnimeList(viewModel.getCurrentUser().id(), viewModel.getAnimeListNameToCreate());
    }

    public void updateNotification(String notificationMessage) {
        viewModel.notificationProperty().set(notificationMessage);
    }
}
