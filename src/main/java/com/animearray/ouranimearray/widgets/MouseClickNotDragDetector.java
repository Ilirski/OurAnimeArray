package com.animearray.ouranimearray.widgets;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

import static java.lang.System.currentTimeMillis;

// Stolen from https://stackoverflow.com/questions/41655507/javafx-distinguish-drag-and-click
public class MouseClickNotDragDetector {

    private Consumer<MouseEvent> onClickedNotDragged;
    private boolean wasDragged;
    private long timePressed;
    private long timeReleased;
    private long pressedDurationThreshold;

    private MouseClickNotDragDetector(Node node) {

        node.addEventHandler(MouseEvent.MOUSE_PRESSED, (mouseEvent) -> this.timePressed = currentTimeMillis());

        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, (mouseEvent) -> this.wasDragged = true);

        node.addEventHandler(MouseEvent.MOUSE_RELEASED, (mouseEvent) -> {
            this.timeReleased = currentTimeMillis();
            this.fireEventIfWasClickedNotDragged(mouseEvent);
            this.clear();
        });

        this.pressedDurationThreshold = 200;
    }

    static MouseClickNotDragDetector clickNotDragDetectingOn(Node node) {
        return new MouseClickNotDragDetector(node);
    }

    MouseClickNotDragDetector withPressedDurationThreshold() {
        this.pressedDurationThreshold = 150;
        return this;
    }

    void setOnMouseClickedNotDragged(Consumer<MouseEvent> onClickedNotDragged) {
        this.onClickedNotDragged = onClickedNotDragged;
    }

    private void clear() {
        this.wasDragged = false;
        this.timePressed = 0;
        this.timeReleased = 0;
    }

    private void fireEventIfWasClickedNotDragged(MouseEvent mouseEvent) {
        if (this.wasDragged) {
//            System.out.println("[CLICK-NOT-DRAG] dragged!");
            return;
        }
        if (this.mousePressedDuration() > this.pressedDurationThreshold) {
//            System.out.println("[CLICK-NOT-DRAG] pressed too long, not a click!");
            return;
        }
//        System.out.println("[CLICK-NOT-DRAG] click!");
        this.onClickedNotDragged.accept(mouseEvent);
    }

    private long mousePressedDuration() {
        return this.timeReleased - this.timePressed;
    }
}
