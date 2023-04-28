package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.widgets.Anime;
import com.animearray.ouranimearray.widgets.AnimeList;
import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.animearray.ouranimearray.widgets.User;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LeftSidebarPageModel {
    private final ListProperty<AnimeList> animeList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final BooleanProperty leftSideBarVisible = new SimpleBooleanProperty(false);
    private final ObjectProperty<Anime> animeToAdd = new SimpleObjectProperty<>();
    private final ObjectProperty<AnimeList> animeListToAddTo = new SimpleObjectProperty<>();
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);
    private final BooleanProperty listPageSelected = new SimpleBooleanProperty(false);

    public boolean isListPageSelected() {
        return listPageSelected.get();
    }

    public void setListPageSelected(boolean listPageSelected) {
        this.listPageSelected.set(listPageSelected);
    }

    public BooleanProperty listPageSelectedProperty() {
        return listPageSelected;
    }

    public Anime getAnimeToAdd() {
        return animeToAdd.get();
    }

    public void setAnimeToAdd(Anime animeToAdd) {
        this.animeToAdd.set(animeToAdd);
    }

    public ObjectProperty<Anime> animeToAddProperty() {
        return animeToAdd;
    }

    public AnimeList getAnimeListToAddTo() {
        return animeListToAddTo.get();
    }

    public void setAnimeListToAddTo(AnimeList animeListToAddTo) {
        this.animeListToAddTo.set(animeListToAddTo);
    }

    public ObjectProperty<AnimeList> animeListToAddToProperty() {
        return animeListToAddTo;
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser.set(currentUser);
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return loggedIn.get();
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn.set(loggedIn);
    }

    public BooleanProperty loggedInProperty() {
        return loggedIn;
    }

    public ObservableList<AnimeList> getAnimeList() {
        return animeList.get();
    }

    public void setAnimeList(ObservableList<AnimeList> animeList) {
        this.animeList.set(animeList);
    }

    public ListProperty<AnimeList> animeWatchListProperty() {
        return animeList;
    }

    public boolean isLeftSideBarVisible() {
        return leftSideBarVisible.get();
    }

    public void setLeftSideBarVisible(boolean leftSideBarVisible) {
        this.leftSideBarVisible.set(leftSideBarVisible);
    }

    public BooleanProperty leftSideBarVisibleProperty() {
        return leftSideBarVisible;
    }

}
