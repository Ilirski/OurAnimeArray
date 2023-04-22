package com.animearray.ouranimearray.widgets;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;

public class CustomToggleNode extends ToggleButton {
    private StackPane container;

    public CustomToggleNode(javafx.scene.Node content) {
        container = new StackPane(content);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(container);

        selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                container.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
            } else {
                container.setStyle("");
            }
        });
    }
}