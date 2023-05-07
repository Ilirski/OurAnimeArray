package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.AnimeList;
import com.animearray.ouranimearray.widgets.DAOs.User;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LeftSidebarPageModel {
    private final ListProperty<AnimeList> userAnimeLists = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final BooleanProperty leftSideBarVisible = new SimpleBooleanProperty(false);
    private final ObjectProperty<Anime> animeToAdd = new SimpleObjectProperty<>();
    private final ObjectProperty<AnimeList> animeListToAddTo = new SimpleObjectProperty<>();
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);
    private final BooleanProperty listPageSelected = new SimpleBooleanProperty(false);
    private final StringProperty listId = new SimpleStringProperty();
    private final StringProperty listIdToDelete = new SimpleStringProperty();
    private final StringProperty animeListNameToCreate = new SimpleStringProperty();
    private final StringProperty notification = new SimpleStringProperty();

    public String getListIdToDelete() {
        return listIdToDelete.get();
    }

    public void setListIdToDelete(String listIdToDelete) {
        this.listIdToDelete.set(listIdToDelete);
    }

    public StringProperty listIdToDeleteProperty() {
        return listIdToDelete;
    }

    public String getNotification() {
        return notification.get();
    }

    public void setNotification(String notification) {
        this.notification.set(notification);
    }

    public StringProperty notificationProperty() {
        return notification;
    }

    public String getAnimeListNameToCreate() {
        return animeListNameToCreate.get();
    }

    public void setAnimeListNameToCreate(String animeListNameToCreate) {
        this.animeListNameToCreate.set(animeListNameToCreate);
    }

    public StringProperty animeListNameToCreateProperty() {
        return animeListNameToCreate;
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

    public ObservableList<AnimeList> getUserAnimeLists() {
        return userAnimeLists.get();
    }

    public void setUserAnimeLists(ObservableList<AnimeList> userAnimeLists) {
        this.userAnimeLists.set(userAnimeLists);
    }

    public ListProperty<AnimeList> animeWatchListProperty() {
        return userAnimeLists;
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
