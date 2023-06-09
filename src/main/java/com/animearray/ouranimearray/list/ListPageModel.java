package com.animearray.ouranimearray.list;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.animearray.ouranimearray.widgets.DAOs.UserList;
import com.animearray.ouranimearray.widgets.UserListProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListPageModel {
    private final ListProperty<Anime> animeList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final AnimeProperty anime = new AnimeProperty();
    private final BooleanProperty rightSideBarVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty listPageSelected = new SimpleBooleanProperty(false);
    private final UserListProperty userList = new UserListProperty();
    private final StringProperty listId = new SimpleStringProperty("");
    private final StringProperty animeIdToRemove = new SimpleStringProperty("");
    private final BooleanProperty loading = new SimpleBooleanProperty(false);
    private final BooleanProperty editListInfo = new SimpleBooleanProperty(false);

    public boolean isEditListInfo() {
        return editListInfo.get();
    }

    public void setEditListInfo(boolean editListInfo) {
        this.editListInfo.set(editListInfo);
    }

    public BooleanProperty editListInfoProperty() {
        return editListInfo;
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

    public String getAnimeIdToRemove() {
        return animeIdToRemove.get();
    }

    public void setAnimeIdToRemove(String animeIdToRemove) {
        this.animeIdToRemove.set(animeIdToRemove);
    }

    public StringProperty animeIdToRemoveProperty() {
        return animeIdToRemove;
    }

    public UserList getUserList() {
        return userList.get();
    }

    public void setUserList(UserList userList) {
        this.userList.set(userList);
    }

    public UserListProperty userListProperty() {
        return userList;
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

    public ObservableList<Anime> getAnimeList() {
        return animeList.get();
    }

    public void setAnimeList(ObservableList<Anime> animeList) {
        this.animeList.set(animeList);
    }

    public ListProperty<Anime> animeListProperty() {
        return animeList;
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

}
