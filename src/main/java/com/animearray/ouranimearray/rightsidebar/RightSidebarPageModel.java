package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.*;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.User;
import com.animearray.ouranimearray.widgets.DAOs.UserAnime;
import com.animearray.ouranimearray.widgets.DAOs.WatchStatus;
import javafx.beans.property.*;

public class RightSidebarPageModel {
    private final AnimeProperty anime = new AnimeProperty();
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);
    private final BooleanProperty rightSideBarVisible = new SimpleBooleanProperty(false);
    private final IntegerProperty userEpisodesWatched = new SimpleIntegerProperty(0);
    private final ObjectProperty<WatchStatus> userWatchStatus = new SimpleObjectProperty<>();
    private final BooleanProperty watchStatusNotSet = new SimpleBooleanProperty(true);
    private final UserAnimeProperty userAnimeData = new UserAnimeProperty();
    private final IntegerProperty userScore = new SimpleIntegerProperty(0);

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


    public boolean isRightSideBarVisible() {
        return rightSideBarVisible.get();
    }

    public void setRightSideBarVisible(boolean rightSideBarVisible) {
        this.rightSideBarVisible.set(rightSideBarVisible);
    }

    public BooleanProperty rightSideBarVisibleProperty() {
        return rightSideBarVisible;
    }
}
