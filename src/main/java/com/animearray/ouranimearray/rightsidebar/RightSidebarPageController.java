package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.User;
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
        viewBuilder = new RightSidebarPageViewBuilder(model, this::getAnimeStatus, this::setAnimeStatus);

//        animeProperty.addListener((observable, oldValue, newValue) -> {
//                        if (newValue != null && model.isLoggedIn()) {
//                interactor.getAnimeWatchStatus();
//                interactor.updateAnimeWatchStatus();
//            }
//        });

        // Share model with other controllers
        model.animeProperty().bindBidirectional(animeProperty);
        model.currentUserProperty().bind(currentUserProperty);
        model.rightSideBarVisibleProperty().bindBidirectional(rightSideBarVisibleProperty);
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

    private void getAnimeStatus(Runnable postAnimeStatusGet) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.getAnimeWatchStatus();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateAnimeWatchStatus();
            postAnimeStatusGet.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }
    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
