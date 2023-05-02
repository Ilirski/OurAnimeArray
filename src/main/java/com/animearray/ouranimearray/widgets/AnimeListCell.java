package com.animearray.ouranimearray.widgets;

import animatefx.animation.Wobble;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.AnimeList;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;

import java.util.function.Consumer;

// This class is used to render the cells of the list view
public class AnimeListCell extends MFXListCell<AnimeList> {
    private final MFXFontIcon userIcon;

    public AnimeListCell(MFXListView<AnimeList> listView, AnimeList data, ObjectProperty<AnimeList> animeListToAddTo,
                         ObjectProperty<Anime> animeToBeAdded, BooleanProperty listPageSelected,
                         StringProperty listId, Consumer<Runnable> addAnimeToList, Consumer<Runnable> fetchAnimeList) {
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
            new Wobble(this).play();
        });

        setOnMouseDragExited(event -> {
            this.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        });

//        setOnMouseEntered(event -> {
//            listId.set(data.id());
//        });

        setOnMouseClicked(event -> {
//            System.out.println("Setting list id to " + data.id());
            listId.set(data.id());
//            fetchAnimeList.accept(() -> System.out.println("Anime List Fetched"));
            listPageSelected.set(true);
//            System.out.println("Current list id: " + listId.get());
        });

        userIcon = new MFXFontIcon(FontResources.FOLDER.getDescription(), 18);
        render(data);
    }

    @Override
    protected void render(AnimeList data) {
        super.render(data);
        if (userIcon != null) getChildren().add(0, userIcon);
    }
}
