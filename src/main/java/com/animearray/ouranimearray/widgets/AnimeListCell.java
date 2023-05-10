package com.animearray.ouranimearray.widgets;

import animatefx.animation.GlowBackground;
import animatefx.animation.Tada;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.AnimeList;
import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.util.function.Consumer;

// This class is used to render the cells of the list view
public class AnimeListCell extends MFXListCell<AnimeList> {
    private final MFXFontIcon userIcon;
    private final MFXContextMenu contextMenu;

    public AnimeListCell(MFXListView<AnimeList> listView, AnimeList data, ObjectProperty<AnimeList> animeListToAddTo,
                         ObjectProperty<Anime> animeToBeAdded, BooleanProperty listPageSelected,
                         StringProperty listId, StringProperty listIdToDelete, Consumer<Runnable> deleteAnimeList,
                         Consumer<Runnable> fetchUserAnimeList) {
        super(listView, data);

        // IMPORTANT AS NODES OF SUBCLASS REGIONS DO NOT GET CALCULATED
        // BY THE GEOMETRIC SHAPE OF THE NODE
        // See https://stackoverflow.com/questions/24607969/mouse-events-get-ignored-on-the-underlying-layer
        setPickOnBounds(false);

        setOnMouseDragReleased(event -> {
            Object source = event.getGestureSource();
            if (!(source instanceof AnimeGridCell)) return;
            Anime anime = ((AnimeGridCell) source).getItem();
            animeToBeAdded.set(anime);
            animeListToAddTo.set(data);
        });

        setOnMouseDragEntered(event -> {
            getScene().setCursor(Cursor.OPEN_HAND);
            this.setStyle("-fx-background-color: #2966e1; -fx-border-color: #c0bdbd;");
            new Tada(this).play();
//            new GlowBackground(this, Color.WHITE, Color.YELLOW, 20)
//                    .setDelay(Duration.millis(50))
//                    .setCycleCount(3)
//                    .setResetOnFinished(true)
//                    .play();
        });

        setOnMouseDragExited(event -> this.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"));

        setOnMouseClicked(event -> {
            if (!event.isSecondaryButtonDown()) {
                listId.set(data.id());
                listPageSelected.set(true);
            }
        });

        MFXContextMenuItem deleteItem = MFXContextMenuItem.Builder.build()
                .setText("Delete")
                .setOnAction(event -> {
                    listIdToDelete.set(data.id());
                    deleteAnimeList.accept(() -> {
                        System.out.println("Deleting anime list: " + data.id());
                        listIdToDelete.set(null);
                        fetchUserAnimeList.accept(() -> {
                        });
                    });
                })
                .get();

        contextMenu = MFXContextMenu.Builder.build(this)
                .addItem(deleteItem)
                .installAndGet();

        userIcon = new MFXFontIcon(FontResources.FOLDER.getDescription(), 18);
        render(data);
    }

    @Override
    protected void render(AnimeList data) {
        super.render(data);
        if (userIcon != null) getChildren().add(0, userIcon);
    }
}
