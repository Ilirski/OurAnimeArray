package com.animearray.ouranimearray.home;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.User;
import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.tobiasdiez.easybind.EasyBind;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;

public class HomePageModel {
    private final AnimeProperty anime = new AnimeProperty();
    private final BooleanProperty rightSideBarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty leftSideBarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty searchPageSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty loginRegisterPageSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty profilePageSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty listPageSelected = new SimpleBooleanProperty(false);
    private final StringProperty listId = new SimpleStringProperty();
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final BooleanBinding loggedIn = EasyBind.wrapNullable(currentUser).isPresent();
    private final BooleanProperty admin = new SimpleBooleanProperty(false);
    private final BooleanProperty editing = new SimpleBooleanProperty(false);

    public boolean isEditing() {
        return editing.get();
    }

    public BooleanProperty editingProperty() {
        return editing;
    }

    public String getListId() {
        return listId.get();
    }

    public void setListId(String listId) {
        this.listId.set(listId);
    }

    public StringProperty listIdProperty() {
        return listId;
    }

    public boolean isListPageSelected() {
        return listPageSelected.get();
    }

    public void setListPageSelected(boolean listPageSelected) {
        this.listPageSelected.set(listPageSelected);
    }

    public BooleanProperty listPageSelectedProperty() {
        return listPageSelected;
    }

    public Boolean isAdmin() {
        return admin.get();
    }

    public BooleanProperty adminProperty() {
        return admin;
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
