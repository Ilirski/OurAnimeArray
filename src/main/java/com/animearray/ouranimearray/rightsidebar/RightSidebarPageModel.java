package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.Anime;
import com.animearray.ouranimearray.widgets.User;
import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.animearray.ouranimearray.widgets.WatchStatus;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RightSidebarPageModel {
    private final AnimeProperty anime = new AnimeProperty();
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);
    private final BooleanProperty rightSideBarVisible = new SimpleBooleanProperty(false);
    private final ObjectProperty<WatchStatus> watchStatus = new SimpleObjectProperty<>();
    private final BooleanProperty watchStatusNotSet = new SimpleBooleanProperty(true);

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

    public WatchStatus getWatchStatus() {
        return watchStatus.get();
    }

    public void setWatchStatus(WatchStatus watchStatus) {
        this.watchStatus.set(watchStatus);
    }

    public ObjectProperty<WatchStatus> watchStatusProperty() {
        return watchStatus;
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
