package com.animearray.ouranimearray.model;

import com.tobiasdiez.easybind.EasyBind;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
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
    private final StringProperty currentUserID = new SimpleStringProperty();
    private final StringProperty usernameRegister = new SimpleStringProperty("");
    private final StringProperty passwordRegister = new SimpleStringProperty("");
    private final StringProperty usernameLogin = new SimpleStringProperty("");
    private final StringProperty passwordLogin = new SimpleStringProperty("");
    private final BooleanBinding isLoggedIn = EasyBind.wrapNullable(currentUserID).isPresent();
//    private final Binding<Boolean> isLoggedIn = EasyBind.wrap(currentUserID).map(String::isBlank).map(Boolean.FALSE::equals);

    public String getUsernameLogin() {
        return usernameLogin.get();
    }

    public void setUsernameLogin(String usernameLogin) {
        this.usernameLogin.set(usernameLogin);
    }

    public StringProperty usernameLoginProperty() {
        return usernameLogin;
    }

    public String getPasswordLogin() {
        return passwordLogin.get();
    }

    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin.set(passwordLogin);
    }

    public StringProperty passwordLoginProperty() {
        return passwordLogin;
    }

    public BooleanBinding isLoggedInProperty() {
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

    public String getUsernameRegister() {
        return usernameRegister.get();
    }

    public void setUsernameRegister(String usernameRegister) {
        this.usernameRegister.set(usernameRegister);
    }

    public StringProperty usernameRegisterProperty() {
        return usernameRegister;
    }

    public String getPasswordRegister() {
        return passwordRegister.get();
    }

    public void setPasswordRegister(String passwordRegister) {
        this.passwordRegister.set(passwordRegister);
    }

    public StringProperty passwordRegisterProperty() {
        return passwordRegister;
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
