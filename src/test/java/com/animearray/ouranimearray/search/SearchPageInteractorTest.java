package com.animearray.ouranimearray.search;

import com.animearray.ouranimearray.widgets.DAOs.SortType;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class SearchPageInteractorTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Note: Test won't run unless you add
        // --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED
        // to the VM options in the Run/Debug Configurations.
        // See https://github.com/TestFX/TestFX/issues/638
        super.start(stage);
    }

    @Test
    void searchAnime() {
        var model = new SearchPageModel();
        var interactor = new SearchPageInteractor(model);
        model.setSearchQuery("Bocchi the Rock!");
        model.setSortType(SortType.POPULARITY);
        interactor.searchAnime();
        interactor.updateAnimeListFromSearch();
        assertEquals("19", model.getAnimeList().get(0).id());
    }
}