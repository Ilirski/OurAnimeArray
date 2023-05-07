package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.*;
import com.animearray.ouranimearray.widgets.DAOs.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RightSidebarPageModel {
    private final AnimeProperty anime = new AnimeProperty();
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final BooleanProperty admin = new SimpleBooleanProperty(false);
    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);
    private final BooleanProperty rightSidebarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty adminRightSidebarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty userRightSidebarVisible = new SimpleBooleanProperty(true);
    private final IntegerProperty userEpisodesWatched = new SimpleIntegerProperty(0);
    private final ObjectProperty<WatchStatus> userWatchStatus = new SimpleObjectProperty<>();
    private final BooleanProperty watchStatusNotSet = new SimpleBooleanProperty(true);
    private final UserAnimeProperty userAnimeData = new UserAnimeProperty();
    private final IntegerProperty userScore = new SimpleIntegerProperty(0);
    private final ObjectProperty<AnimeToSave> animeToCreateOrModify = new SimpleObjectProperty<>();
    private final ListProperty<Genre> genres = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final BooleanProperty savingAnime = new SimpleBooleanProperty(false);
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

    public boolean isSavingAnime() {
        return savingAnime.get();
    }

    public void setSavingAnime(boolean savingAnime) {
        this.savingAnime.set(savingAnime);
    }

    public BooleanProperty savingAnimeProperty() {
        return savingAnime;
    }

    public boolean isAdmin() {
        return admin.get();
    }

    public BooleanProperty adminProperty() {
        return admin;
    }

    public boolean isAdminRightSidebarVisible() {
        return adminRightSidebarVisible.get();
    }

    public void setAdminRightSidebarVisible(boolean adminRightSidebarVisible) {
        this.adminRightSidebarVisible.set(adminRightSidebarVisible);
    }

    public BooleanProperty adminRightSidebarVisibleProperty() {
        return adminRightSidebarVisible;
    }

    public boolean isUserRightSidebarVisible() {
        return userRightSidebarVisible.get();
    }

    public void setUserRightSidebarVisible(boolean userRightSidebarVisible) {
        this.userRightSidebarVisible.set(userRightSidebarVisible);
    }

    public BooleanProperty userRightSidebarVisibleProperty() {
        return userRightSidebarVisible;
    }

    public ObservableList<Genre> getGenres() {
        return genres.get();
    }

    public void setGenres(ObservableList<Genre> genres) {
        this.genres.set(genres);
    }

    public ListProperty<Genre> genresProperty() {
        return genres;
    }

    public AnimeToSave getAnimeToCreateOrModify() {
        return animeToCreateOrModify.get();
    }

    public void setAnimeToCreateOrModify(AnimeToSave animeToCreateOrModify) {
        this.animeToCreateOrModify.set(animeToCreateOrModify);
    }

    public ObjectProperty<AnimeToSave> animeToCreateOrModifyProperty() {
        return animeToCreateOrModify;
    }

    public int getUserScore() {
        return userScore.get();
    }

    public void setUserScore(int userScore) {
        this.userScore.set(userScore);
    }

    public IntegerProperty userScoreProperty() {
        return userScore;
    }

    public UserAnime getUserAnimeData() {
        return userAnimeData.get();
    }

    public void setUserAnimeData(UserAnime userAnimeData) {
        this.userAnimeData.set(userAnimeData);
    }

    public UserAnimeProperty userAnimeDataProperty() {
        return userAnimeData;
    }

    public int getUserEpisodesWatched() {
        return userEpisodesWatched.get();
    }

    public void setUserEpisodesWatched(int userEpisodesWatched) {
        this.userEpisodesWatched.set(userEpisodesWatched);
    }

    public IntegerProperty userEpisodesWatchedProperty() {
        return userEpisodesWatched;
    }

    public boolean isWatchStatusNotSet() {
        return watchStatusNotSet.get();
    }

    public void setWatchStatusNotSet(boolean watchStatusNotSet) {
        this.watchStatusNotSet.set(watchStatusNotSet);
    }

    public BooleanProperty watchStatusNotSetProperty() {
        return watchStatusNotSet;
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

    public WatchStatus getUserWatchStatus() {
        return userWatchStatus.get();
    }

    public void setUserWatchStatus(WatchStatus userWatchStatus) {
        this.userWatchStatus.set(userWatchStatus);
    }

    public ObjectProperty<WatchStatus> userWatchStatusProperty() {
        return userWatchStatus;
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
    public User getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser.set(currentUser);
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }


    public boolean getRightSidebarVisible() {
        return rightSidebarVisible.get();
    }

    public void setRightSidebarVisible(boolean rightSidebarVisible) {
        this.rightSidebarVisible.set(rightSidebarVisible);
    }

    public BooleanProperty rightSidebarVisibleProperty() {
        return rightSidebarVisible;
    }
}
