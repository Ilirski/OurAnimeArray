package com.animearray.ouranimearray.interactor;

import com.animearray.ouranimearray.controller.Controller;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Objects;

class AnimeInteractorTest extends ApplicationTest {
    @Override
    public void start (Stage stage) throws Exception {
        stage.setTitle("OurAnimeArrayTest");
        Scene scene = new Scene(new Controller().getViewBuilder(), 1200, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    @Test
    void searchAnime() {
        Button button = new Button();
        Model model = new Model();
        AnimeInteractor animeInteractor = new AnimeInteractor(model);
        model.setSearchQuery("Bocchi");
        animeInteractor.searchAnime();
        assertEquals("Bocchi The Rock!", model.animeProperty().get().title());
    }

    @Test
    void getUser() {
    }
}