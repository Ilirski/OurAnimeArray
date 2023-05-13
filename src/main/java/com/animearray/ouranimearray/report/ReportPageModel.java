package com.animearray.ouranimearray.report;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.Genre;
import com.animearray.ouranimearray.widgets.DAOs.ReportType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportPageModel {
    private final ListProperty<Anime> mostPopularAnimes = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final IntegerProperty mostPopularAnimesAmount = new SimpleIntegerProperty(15);
    private final ListProperty<Genre> genres = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Genre> selectedGenre = new SimpleObjectProperty<>();
    private final ObjectProperty<ReportType> selectedReportType = new SimpleObjectProperty<>();
    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    public boolean isLoading() {
        return loading.get();
    }

    public void setLoading(boolean loading) {
        this.loading.set(loading);
    }

    public BooleanProperty loadingProperty() {
        return loading;
    }

    public ReportType getSelectedReportType() {
        return selectedReportType.get();
    }

    public ObjectProperty<ReportType> selectedReportTypeProperty() {
        return selectedReportType;
    }

    public Genre getSelectedGenre() {
        return selectedGenre.get();
    }

    public void setSelectedGenre(Genre selectedGenre) {
        this.selectedGenre.set(selectedGenre);
    }

    public ObjectProperty<Genre> selectedGenreProperty() {
        return selectedGenre;
    }

    public ObservableList<Genre> getGenres() {
        return genres.get();
    }

    public ListProperty<Genre> genresProperty() {
        return genres;
    }

    public int getMostPopularAnimesAmount() {
        return mostPopularAnimesAmount.get();
    }

    public void setMostPopularAnimesAmount(int mostPopularAnimesAmount) {
        this.mostPopularAnimesAmount.set(mostPopularAnimesAmount);
    }

    public IntegerProperty mostPopularAnimesAmountProperty() {
        return mostPopularAnimesAmount;
    }

    public ObservableList<Anime> getMostPopularAnimes() {
        return mostPopularAnimes.get();
    }

    public void setMostPopularAnimes(ObservableList<Anime> mostPopularAnimes) {
        this.mostPopularAnimes.set(mostPopularAnimes);
    }

    public ListProperty<Anime> mostPopularAnimesProperty() {
        return mostPopularAnimes;
    }

}
