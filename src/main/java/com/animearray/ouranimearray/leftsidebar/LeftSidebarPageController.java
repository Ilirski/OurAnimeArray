package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.widgets.ControllerFX;
import com.animearray.ouranimearray.widgets.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class LeftSidebarPageController implements ControllerFX {
    private final Builder<Region> viewBuilder;
    private final LeftSidebarPageInteractor interactor;

    public LeftSidebarPageController(ObjectProperty<User> currentUserProperty, BooleanProperty leftSideBarVisibleProperty) {
        var model = new LeftSidebarPageModel();
        interactor = new LeftSidebarPageInteractor(model);
        viewBuilder = new LeftSidebarPageViewBuilder(model, this::fetchAnimeLists, this::addAnimeToList);

        // Share the leftSideBarVisibleProperty with the model
        model.leftSideBarVisibleProperty().bind(leftSideBarVisibleProperty);
        model.currentUserProperty().bind(currentUserProperty);
    }

    private void fetchAnimeLists(Runnable postAnimeListFetch) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.loadAnimeList();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateAnimeList();
            postAnimeListFetch.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void addAnimeToList(Runnable postAnimeListAdd) {
        Task<Void> addTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.addAnimeToList();
                return null;
            }
        };
        addTask.setOnSucceeded(event -> {
            interactor.updateAnimeList();
            postAnimeListAdd.run();
        });
        Thread addThread = new Thread(addTask);
        addThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
