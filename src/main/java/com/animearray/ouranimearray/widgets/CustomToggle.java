package com.animearray.ouranimearray.widgets;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class CustomToggle extends Node implements Toggle {
    private Node node;
    private BooleanProperty selectedProperty = new SimpleBooleanProperty(false);
    private ObjectProperty<ToggleGroup> toggleGroupProperty = new SimpleObjectProperty<>(null);

    public CustomToggle(Node node) {
        this.node = node;
    }

    @Override
    public ToggleGroup getToggleGroup() {
        return toggleGroupProperty.get();
    }

    @Override
    public void setToggleGroup(ToggleGroup toggleGroup) {
        toggleGroupProperty.set(toggleGroup);
    }

    @Override
    public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        return toggleGroupProperty;
    }

    @Override
    public boolean isSelected() {
        return selectedProperty.get();
    }

    @Override
    public void setSelected(boolean selected) {
        selectedProperty.set(selected);
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }
}
