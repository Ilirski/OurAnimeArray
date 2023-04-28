package com.animearray.ouranimearray.search;

import com.animearray.ouranimearray.widgets.Anime;
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
        String searchQuery = viewModel.getSearchQuery();
        animeListData = databaseFetcher.searchAnime(searchQuery);
    }

    public void updateAnimeListFromSearch() {
        viewModel.animeListProperty().clear();
        viewModel.animeListProperty().addAll(animeListData);
    }
}
