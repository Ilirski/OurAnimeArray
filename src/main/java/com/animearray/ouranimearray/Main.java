package com.animearray.ouranimearray;

//import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
//import io.github.palexdev.materialfx.css.themes.Themes;

import com.animearray.ouranimearray.home.HomePageController;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {


    @Override
    public void start(Stage stage) {
        // JavaFX version
        System.out.println("JavaFX Runtime Version: " + System.getProperty("javafx.runtime.version"));
        System.out.println("JavaFX Version: " + System.getProperty("javafx.version"));

        stage.setTitle("OurAnimeArray");
        Scene scene = new Scene(new HomePageController().getViewBuilder(), 1200, 600);

        // Styling
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("tooltip.css")).toExternalForm());
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/rimuru-thumbnail.jpg")).toString()));
        stage.setScene(scene);

        // Don't forget to remove below!!
        CSSFX.start();
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}