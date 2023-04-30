package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.DAOs.User;
import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class RightSidebarPageController implements ControllerFX {
    private final RightSidebarPageInteractor interactor;
    private final Builder<Region> viewBuilder;

    public RightSidebarPageController(AnimeProperty animeProperty, ObjectProperty<User> currentUserProperty, BooleanProperty rightSideBarVisibleProperty) {
        var model = new RightSidebarPageModel();
        interactor = new RightSidebarPageInteractor(model);
        viewBuilder = new RightSidebarPageViewBuilder(model, this::setAnimeStatus,
                this::setEpisodeWatched, this::getUserAnimeData,
                this::setUserScore);

        // Share model with other controllers
        model.currentUserProperty().bind(currentUserProperty);
        model.animeProperty().bindBidirectional(animeProperty);
        model.rightSideBarVisibleProperty().bindBidirectional(rightSideBarVisibleProperty);
    }

    private void getUserAnimeData(Runnable postUserAnimeDataGet) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.getUserAnimeData();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateUserAnimeData();
            postUserAnimeDataGet.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void setAnimeStatus(Runnable postAnimeStatusSet) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.setAnimeWatchStatus();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateAnimeWatchStatus();
            postAnimeStatusSet.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void setEpisodeWatched(Runnable postEpisodeWatchedSet) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.setEpisodeWatched();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateEpisodeWatched();
            postEpisodeWatchedSet.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    public void setUserScore(Runnable postUserScoreSet) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.setUserAnimeScore();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateUserAnimeScore();
            postUserScoreSet.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
