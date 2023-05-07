package com.animearray.ouranimearray.search;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.animearray.ouranimearray.widgets.DAOs.SortType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchPageModel {
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private final ListProperty<Anime> animeList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final AnimeProperty anime = new AnimeProperty();
    private final BooleanProperty rightSideBarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty loading = new SimpleBooleanProperty(false);
    private final ObjectProperty<SortType> sortType = new SimpleObjectProperty<>(SortType.SCORE);
    private final BooleanProperty admin = new SimpleBooleanProperty(false);
    private final BooleanProperty editing = new SimpleBooleanProperty(false);

    public boolean isEditing() {
        return editing.get();
    }

    public void setEditing(boolean editing) {
        this.editing.set(editing);
    }

    public BooleanProperty editingProperty() {
        return editing;
    }

    public boolean isAdmin() {
        return admin.get();
    }

    public void setAdmin(boolean admin) {
        this.admin.set(admin);
    }

    public BooleanProperty adminProperty() {
        return admin;
    }

    public SortType getSortType() {
        return sortType.get();
    }

    public void setSortType(SortType sortType) {
        this.sortType.set(sortType);
    }

    public ObjectProperty<SortType> sortTypeProperty() {
        return sortType;
    }

    public boolean isLoading() {
        return loading.get();
    }

    public void setLoading(boolean loading) {
        this.loading.set(loading);
    }

    public BooleanProperty loadingProperty() {
        return loading;
    }

    public ObservableList<Anime> getAnimeList() {
        return animeList.get();
    }

    public void setAnimeList(ObservableList<Anime> animeList) {
        this.animeList.set(animeList);
    }

    public Anime getAnime() {
        return anime.get();
    }

    public void setAnime(Anime anime) {
        this.anime.set(anime);
    }

    public AnimeProperty animeProperty() {
        return anime;
    }

    public boolean isRightSideBarVisible() {
        return rightSideBarVisible.get();
    }

    public void setRightSideBarVisible(boolean rightSideBarVisible) {
        this.rightSideBarVisible.set(rightSideBarVisible);
    }

    public BooleanProperty rightSideBarVisibleProperty() {
        return rightSideBarVisible;
    }

    public ListProperty<Anime> animeListProperty() {
        return animeList;
    }

    public String getSearchQuery() {
        return searchQuery.get();
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery.set(searchQuery);
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }
}
