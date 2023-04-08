package com.animearray.ouranimearray.controller;

import com.animearray.ouranimearray.interactor.AnimeInteractor;
import com.animearray.ouranimearray.model.Model;
import com.animearray.ouranimearray.view.ViewBuilder;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;

public class Controller {
    private final AnimeInteractor interactor;
    private final ViewBuilder viewBuilder;

    public Controller() {
        Model viewModel = new Model();
        interactor = new AnimeInteractor(viewModel);
        viewBuilder = new ViewBuilder(viewModel, this::fetchAnimeList, this::verifyUser, this::registerUser);

        // Setup default view
        interactor.searchAnime();
        interactor.updateAnimeList();

        // Listeners
    }

    private void fetchAnimeList(Runnable postFetchAnimeList) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                interactor.searchAnime();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateAnimeList();
            postFetchAnimeList.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void verifyUser(Runnable postFetchUsers) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                interactor.getUser();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            postFetchUsers.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void registerUser(Runnable postFetchUsers) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                interactor.registerUser();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            postFetchUsers.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }



    public Region getViewBuilder() { return viewBuilder.build(); }
}
