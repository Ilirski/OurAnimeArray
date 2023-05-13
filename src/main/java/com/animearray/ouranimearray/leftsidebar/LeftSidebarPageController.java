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
        viewBuilder = new LeftSidebarPageViewBuilder(model, this::fetchUserAnimeLists, this::addAnimeToList,
                this::createNewAnimeList, this::deleteAnimeList);

        // Share the leftSideBarVisibleProperty with the model
        model.leftSideBarVisibleProperty().bind(leftSideBarVisibleProperty);
        model.currentUserProperty().bind(currentUserProperty);
        model.listPageSelectedProperty().bindBidirectional(listPageSelectedProperty);
        model.listIdProperty().bindBidirectional(listIdProperty);
    }

    private void fetchUserAnimeLists(Runnable postUserAnimeListsFetch) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.loadUserAnimeLists();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateAnimeList();
            postUserAnimeListsFetch.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void addAnimeToList(Runnable postAnimeListAdd) {
        Task<Void> addTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                interactor.addAnimeToList();
                return null;
            }
        };
        addTask.setOnSucceeded(event -> {
            interactor.updateAnimeList();
            interactor.updateNotification("Anime successfully added to list!");
            postAnimeListAdd.run();
        });
        addTask.setOnFailed(event -> {
            SQLException sqlException = (SQLException) addTask.getException();
            System.out.println(sqlException.getErrorCode() + " " + sqlException.getMessage());
            if (sqlException.getErrorCode() == 19) {
                interactor.updateNotification("ERROR: Anime already exists in list.");
            }
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
            System.out.println(sqlException.getErrorCode() + " " + sqlException.getMessage());
            if (sqlException.getErrorCode() == 19) {
                interactor.updateNotification("ERROR: List name already exists.");
            }
        });
        Thread createThread = new Thread(createTask);
        createThread.start();
    }

    public void deleteAnimeList(Runnable postAnimeListDelete) {
        Task<Void> deleteTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.deleteAnimeList();
                return null;
            }
        };
        deleteTask.setOnSucceeded(event -> {
            interactor.updateNotification("List successfully deleted!");
            postAnimeListDelete.run();
        });
        Thread deleteThread = new Thread(deleteTask);
        deleteThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
