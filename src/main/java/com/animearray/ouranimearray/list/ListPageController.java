package com.animearray.ouranimearray.list;

import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ListPageController implements ControllerFX {
    private final Builder<Region> viewBuilder;
    private final ListPageInteractor interactor;

    public ListPageController(BooleanProperty listPageSelectedProperty, AnimeProperty animeProperty,
                              BooleanProperty rightSideBarVisibleProperty, StringProperty listIdProperty) {
        var model = new ListPageModel();
        interactor = new ListPageInteractor(model);
        viewBuilder = new ListPageViewBuilder(model, this::fetchAnimeList, this::fetchUserListDetails,
                this::removeAnimeFromList, this::editUserAnimeList);

        // Bind and share properties
        model.listPageSelectedProperty().bindBidirectional(listPageSelectedProperty);
        model.animeProperty().bindBidirectional(animeProperty);
        model.rightSideBarVisibleProperty().bindBidirectional(rightSideBarVisibleProperty);
        model.listIdProperty().bindBidirectional(listIdProperty);
    }

    private void fetchAnimeList(Runnable postFetchAnimeList) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.getUserAnimeList();
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

    public void fetchUserListDetails(Runnable postFetchUserListDetails) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.getUserListDetails();
                return null;
            }
        };
        // Run in either case (success or failure)
        // Probably not good practice, but it works
        fetchTask.setOnSucceeded(event -> {
            interactor.updateUserListDetails();
            postFetchUserListDetails.run();
        });
        fetchTask.setOnFailed(event -> {
            interactor.updateUserListDetails();
            postFetchUserListDetails.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    public void removeAnimeFromList(Runnable postAnimeListRemove) {
        Task<Void> removeTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.removeAnimeFromList();
                return null;
            }
        };
        removeTask.setOnSucceeded(event -> {
            postAnimeListRemove.run();
        });
        Thread removeThread = new Thread(removeTask);
        removeThread.start();
    }

    public void editUserAnimeList(Runnable postAnimeListEdit) {
        Task<Void> editTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.editUserAnimeList();
                return null;
            }
        };
        editTask.setOnSucceeded(event -> {
            postAnimeListEdit.run();
        });
        Thread editThread = new Thread(editTask);
        editThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
