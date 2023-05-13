package com.animearray.ouranimearray.widgets;

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
import javafx.scene.input.MouseButton;

import java.util.Objects;
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
        });

        setOnMouseDragExited(event -> this.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"));

        setOnMouseClicked(event -> {
            if (event.getButton() != null && event.getButton() != MouseButton.SECONDARY) {
                listId.set(data.id());
                listPageSelected.set(true);
            }
        });

        MFXContextMenuItem deleteItem = MFXContextMenuItem.Builder.build()
                .setText("Delete")
                .setIcon(new MFXFontIcon(FontResources.DELETE.getDescription(), 20))
                .setOnAction(event -> {
                    listIdToDelete.set(data.id());
                    deleteAnimeList.accept(() -> {
                        listIdToDelete.set(null);
                        fetchUserAnimeList.accept(() -> {
                            // Select the first list
                            listId.set(Objects.requireNonNull(listView.getItems().stream().findFirst().orElse(null)).id());
                            listPageSelected.set(true);
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
