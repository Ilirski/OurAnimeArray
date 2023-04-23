package com.animearray.ouranimearray.home;

import com.animearray.ouranimearray.model.Anime;
import com.animearray.ouranimearray.model.User;
import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.tobiasdiez.easybind.EasyBind;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HomePageModel {
    private final AnimeProperty anime = new AnimeProperty();
    private final BooleanProperty rightSideBarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty leftSideBarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty searchPageSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty loginRegisterPageSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty profilePageSelected = new SimpleBooleanProperty(false);
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final BooleanBinding loggedIn = EasyBind.wrapNullable(currentUser).isPresent();

    public User getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser.set(currentUser);
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public Boolean isLoggedIn() {
        return loggedIn.get();
    }

    public BooleanBinding loggedInProperty() {
        return loggedIn;
    }

    public boolean isSearchPageSelected() {
        return searchPageSelected.get();
    }

    public BooleanProperty searchPageSelectedProperty() {
        return searchPageSelected;
    }

    public boolean isLoginRegisterPageSelected() {
        return loginRegisterPageSelected.get();
    }

    public BooleanProperty loginRegisterPageSelectedProperty() {
        return loginRegisterPageSelected;
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

    public boolean isLeftSideBarVisible() {
        return leftSideBarVisible.get();
    }

    public void setLeftSideBarVisible(boolean leftSideBarVisible) {
        this.leftSideBarVisible.set(leftSideBarVisible);
    }

    public BooleanProperty leftSideBarVisibleProperty() {
        return leftSideBarVisible;
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

    public boolean isProfilePageSelected() {
        return profilePageSelected.get();
    }

    public BooleanProperty profilePageSelectedProperty() {
        return profilePageSelected;
    }
}