package com.animearray.ouranimearray.misc;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import javafx.beans.Observable;

public class NewMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final MigPane root = new MigPane(new LC().wrap());
            final ScrollPane scroller = new ScrollPane();
            scroller.setContent(root);
            final Scene scene = new Scene(scroller,400,200);

            for (int i=1; i<=20; i++) {
                final Label label = new Label("Item "+i);
                addWithDragging(root, label);
            }

            // in case user drops node in blank space in root:
            root.setOnMouseDragReleased(event -> {
                int indexOfDraggingNode = root.getChildren().indexOf(event.getGestureSource());
                rotateNodes(root, indexOfDraggingNode, root.getChildren().size()-1);
                removePreview(root);
            });

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addWithDragging(final MigPane root, final Label label) {
        label.setOnDragDetected(event -> {
            label.startFullDrag();
            addPreview(root, label);
        });

        // next two handlers just an idea how to show the drop target visually:
        label.setOnMouseDragEntered(event -> label.setStyle("-fx-background-color: #ffffa0;"));
        label.setOnMouseDragExited(event -> label.setStyle(""));

        label.setOnMouseDragReleased(event -> {
            label.setStyle("");
            int indexOfDraggingNode = root.getChildren().indexOf(event.getGestureSource());
            int indexOfDropTarget = root.getChildren().indexOf(label);
            rotateNodes(root, indexOfDraggingNode, indexOfDropTarget);
            removePreview(root);
            event.consume();
        });
        root.getChildren().add(label);
    }

    private void rotateNodes(final MigPane root, final int indexOfDraggingNode,
                             final int indexOfDropTarget) {
        if (indexOfDraggingNode >= 0 && indexOfDropTarget >= 0) {
            final Node node = root.getChildren().remove(indexOfDraggingNode);
            root.getChildren().add(indexOfDropTarget, node);
        }
    }

    private void addPreview(final MigPane root, final Label label) {
        ImageView imageView = new ImageView(label.snapshot(null, null));
        imageView.setManaged(false);
        imageView.setMouseTransparent(true);
        root.getChildren().add(imageView);
        root.setUserData(imageView);
        root.setOnMouseDragged(event -> imageView.relocate(event.getX(), event.getY()));
    }

    private void removePreview(final MigPane root) {
        root.setOnMouseDragged(null);
        root.getChildren().remove((ImageView) root.getUserData());
        root.setUserData(null);
    }


    public static void main(String[] args) {
        launch(args);
    }
}