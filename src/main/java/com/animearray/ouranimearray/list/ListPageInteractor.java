package com.animearray.ouranimearray.list;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.DAOs.UserList;
import com.animearray.ouranimearray.widgets.DAOs.Anime;

import java.util.List;

public class ListPageInteractor {
    private final ListPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    private List<Anime> animeListData;
    private UserList userListData;

    public ListPageInteractor(ListPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }

    public void getUserAnimeList() {
        System.out.println("Before");
        animeListData = databaseFetcher.getAnimeFromList(viewModel.getListId());
        System.out.println(animeListData);
    }

    public void updateAnimeList() {
        viewModel.animeListProperty().clear();
        viewModel.animeListProperty().addAll(animeListData);
    }

    public void getUserListDetails() {
        userListData = databaseFetcher.getUserListDetails(viewModel.getListId());
    }

    public void updateUserListDetails() {
        viewModel.setUserList(userListData);
    }

    public void removeAnimeFromList() {
        databaseFetcher.removeAnimeFromList(viewModel.getAnimeIdToRemove(), viewModel.getListId());
    }

    public void editUserAnimeList() {
        databaseFetcher.editUserAnimeList(viewModel.getListId(), viewModel.userListProperty().getListName(), viewModel.userListProperty().getDescription());
    }
}
