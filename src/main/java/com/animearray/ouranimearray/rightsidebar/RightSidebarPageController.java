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

    public RightSidebarPageController(AnimeProperty animeProperty, ObjectProperty<User> currentUserProperty,
                                      BooleanProperty adminProperty, BooleanProperty rightSideBarVisibleProperty,
                                      BooleanProperty editingProperty) {
        var model = new RightSidebarPageModel();
        interactor = new RightSidebarPageInteractor(model);
        viewBuilder = new RightSidebarPageViewBuilder(model, this::setAnimeStatus,
                this::setEpisodeWatched, this::getUserAnimeData,
                this::setUserScore, this::getGenres,
                this::editAnime, this::deleteAnime,
                this::addAnime);

        // Share model with other controllers
        model.currentUserProperty().bind(currentUserProperty);
        model.adminProperty().bind(adminProperty);
        model.animeProperty().bindBidirectional(animeProperty);
        model.rightSidebarVisibleProperty().bindBidirectional(rightSideBarVisibleProperty);
        editingProperty.bind(model.editingProperty());
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

    public void getGenres(Runnable postGenresGet) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.getGenres();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateGenres();
            postGenresGet.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    public void editAnime(Runnable postAnimeEdit) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.editAnime();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            postAnimeEdit.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    public void deleteAnime(Runnable postAnimeDelete) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.deleteAnime();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            postAnimeDelete.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    public void addAnime(Runnable postAnimeAdd) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.addAnime();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            postAnimeAdd.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
