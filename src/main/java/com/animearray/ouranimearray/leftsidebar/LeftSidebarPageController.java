package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.widgets.ControllerFX;
import com.animearray.ouranimearray.widgets.DAOs.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.sql.SQLException;

public class LeftSidebarPageController implements ControllerFX {
    private final Builder<Region> viewBuilder;
    private final LeftSidebarPageInteractor interactor;

    public LeftSidebarPageController(ObjectProperty<User> currentUserProperty, BooleanProperty leftSideBarVisibleProperty,
                                     BooleanProperty listPageSelectedProperty, StringProperty listIdProperty) {
        var model = new LeftSidebarPageModel();
        interactor = new LeftSidebarPageInteractor(model);
        viewBuilder = new LeftSidebarPageViewBuilder(model, this::fetchAnimeLists, this::addAnimeToList, this::createNewAnimeList);

        // Share the leftSideBarVisibleProperty with the model
        model.leftSideBarVisibleProperty().bind(leftSideBarVisibleProperty);
        model.currentUserProperty().bind(currentUserProperty);
        model.listPageSelectedProperty().bindBidirectional(listPageSelectedProperty);
        model.listIdProperty().bindBidirectional(listIdProperty);
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

    public void createNewAnimeList(Runnable postAnimeListCreate) {
        Task<Void> createTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                interactor.createNewAnimeList();
                return null;
            }
        };
        createTask.setOnSucceeded(event -> {
            interactor.updateNotification("List successfully created!");
            postAnimeListCreate.run();
        });
        createTask.setOnFailed(event -> {
            SQLException sqlException = (SQLException) createTask.getException();
            if (sqlException.getErrorCode() == 19) {
                interactor.updateNotification("ERROR: List name already exists.");
            }
        });
        Thread createThread = new Thread(createTask);
        createThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
