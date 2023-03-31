package com.animearray.ouranimearray;

//import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
//import io.github.palexdev.materialfx.css.themes.Themes;
import com.animearray.ouranimearray.controller.Controller;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.Executors;

public class Main extends Application {


    @Override
    public void start(Stage stage) {
        stage.setTitle("OurAnimeArray");
        Scene scene = new Scene(new Controller().getViewBuilder(), 1200, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());
        stage.setScene(scene);
//        stage.getIcons().add(new Image("com/animearray/ouranimearray/icons/rimuru-thumbnail.jpg"));

        // Don't forget to remove below!!
        CSSFX.start();

        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}