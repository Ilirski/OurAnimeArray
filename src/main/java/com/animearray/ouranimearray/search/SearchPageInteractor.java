package com.animearray.ouranimearray.search;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.model.DatabaseFetcher;

import java.util.List;

public class SearchPageInteractor {
    private final SearchPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    private List<Anime> animeListData;
    public SearchPageInteractor(SearchPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }

    public void searchAnime() {
        animeListData = databaseFetcher.searchAnime(viewModel.getSearchQuery(), viewModel.getSortType());
    }

    public void updateAnimeListFromSearch() {
        viewModel.animeListProperty().clear();
        viewModel.animeListProperty().addAll(animeListData);
    }
}
