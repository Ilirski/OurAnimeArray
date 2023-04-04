package com.animearray.ouranimearray.interactor;

import com.animearray.ouranimearray.model.Anime;
import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.model.Model;
import com.animearray.ouranimearray.model.User;

import java.util.List;
import java.util.Optional;

public class AnimeInteractor {
    private final Model viewModel;
    private final DatabaseFetcher databaseFetcher;
    private List<Anime> animeListData;

    public AnimeInteractor(Model viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }

    public void searchAnime() {
        String searchQuery = viewModel.getSearchQuery();
        animeListData = databaseFetcher.searchAnime(searchQuery);
    }

    public void getUser() {
        Optional<String> user = databaseFetcher.getUser(viewModel.getUsername(), viewModel.getPassword());
        viewModel.setCurrentUserID(user.orElse(""));
    }


    public void getUsers() {
        List<User> users = databaseFetcher.getUsers();
        System.out.println(users);
    }

    public void updateAnimeList() {
        viewModel.animeListProperty().clear();
        viewModel.animeListProperty().addAll(animeListData);
    }
}
