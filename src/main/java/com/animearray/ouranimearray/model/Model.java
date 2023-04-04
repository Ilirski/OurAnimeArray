package com.animearray.ouranimearray.model;

import com.tobiasdiez.easybind.EasyBind;
import javafx.beans.binding.Binding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.tbee.javafx.scene.layout.MigPane;

public class Model {

    private final ListProperty<Anime> animeList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final BooleanProperty rightSideBarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty leftSideBarVisible = new SimpleBooleanProperty(false);
    private final ObjectProperty<MigPane> currentMainPane = new SimpleObjectProperty<>();
    private final SimpleAnimeProperty anime = new SimpleAnimeProperty();
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private final StringProperty currentUserID = new SimpleStringProperty("");
    private final Binding<Boolean> isLoggedIn = EasyBind.wrap(currentUserID).map(String::isBlank);
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");

    public Binding<Boolean> isLoggedInProperty() {
        return isLoggedIn;
    }

    public String getCurrentUserID() {
        return currentUserID.get();
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID.set(currentUserID);
    }

    public StringProperty currentUserIDProperty() {
        return currentUserID;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
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

    public SimpleAnimeProperty animeProperty() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime.set(anime);
    }

    public MigPane getCurrentMainPane() {
        return currentMainPane.get();
    }

    public void setCurrentMainPane(MigPane currentMainPane) {
        this.currentMainPane.set(currentMainPane);
    }

    public ObjectProperty<MigPane> currentMainPaneProperty() {
        return currentMainPane;
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

    public BooleanProperty rightSideBarVisibleProperty() {
        return rightSideBarVisible;
    }

    public boolean isRightSideBarVisible() {
        return rightSideBarVisible.get();
    }

    public void setRightSideBarVisible(boolean visible) {
        rightSideBarVisible.set(visible);
    }

    public ListProperty<Anime> animeListProperty() {
        return animeList;
    }

    public void setAnimeList(ObservableList<Anime> animeList) {
        this.animeList.set(animeList);
    }

}
