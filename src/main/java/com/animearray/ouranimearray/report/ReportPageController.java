package com.animearray.ouranimearray.report;

import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ReportPageController implements ControllerFX {
    private final Builder<Region> viewBuilder;
    private final ReportPageInteractor interactor;

    public ReportPageController() {
        var model = new ReportPageModel();
        interactor = new ReportPageInteractor(model);
        viewBuilder = new ReportPageViewBuilder(model, this::getMostPopularAnime, this::getGenres);
    }

    private void getMostPopularAnime(Runnable postUserAnimeDataGet) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.getMostPopularAnime();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateMostPopularAnime();
            postUserAnimeDataGet.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void getGenres(Runnable postUserAnimeDataGet) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.getGenres();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateGenres();
            postUserAnimeDataGet.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
