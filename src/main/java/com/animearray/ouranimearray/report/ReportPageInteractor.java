package com.animearray.ouranimearray.report;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.Genre;

import java.util.List;

public class ReportPageInteractor {
    private final ReportPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    private List<Anime> mostPopularAnimeList;
    private List<Genre> genresList;

    public ReportPageInteractor(ReportPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }

    public void getMostPopularAnime() {
        mostPopularAnimeList = databaseFetcher.getMostPopularAnime(viewModel.getMostPopularAnimesAmount(), viewModel.getSelectedGenre(), viewModel.getSelectedReportType());
    }

    public void updateMostPopularAnime() {
        viewModel.mostPopularAnimesProperty().clear();
        viewModel.mostPopularAnimesProperty().addAll(mostPopularAnimeList);
    }

    public void getGenres() {
        genresList = databaseFetcher.getGenres();
    }

    public void updateGenres() {
        viewModel.genresProperty().clear();
        viewModel.genresProperty().addAll(genresList);
    }
}
