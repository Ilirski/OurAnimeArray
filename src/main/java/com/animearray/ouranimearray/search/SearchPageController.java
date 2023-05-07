package com.animearray.ouranimearray.search;

import com.animearray.ouranimearray.widgets.ControllerFX;
import com.animearray.ouranimearray.widgets.AnimeProperty;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class SearchPageController implements ControllerFX {
    private final SearchPageInteractor interactor;
    private final Builder<Region> viewBuilder;

    public SearchPageController(AnimeProperty animeProperty, BooleanProperty rightSideBarVisibleProperty,
                                BooleanProperty adminProperty, BooleanProperty editingProperty) {
        SearchPageModel model = new SearchPageModel();
        interactor = new SearchPageInteractor(model);
        viewBuilder = new SearchPageViewBuilder(model, this::fetchAnimeList);

        // Share model with other controllers
        animeProperty.bindBidirectional(model.animeProperty());
        rightSideBarVisibleProperty.bindBidirectional(model.rightSideBarVisibleProperty());
        model.adminProperty().bind(adminProperty);
        model.editingProperty().bind(editingProperty);

        // Setup default view
        interactor.searchAnime();
        interactor.updateAnimeListFromSearch();
    }

    private void fetchAnimeList(Runnable postFetchAnimeList) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.searchAnime();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateAnimeListFromSearch();
            postFetchAnimeList.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
