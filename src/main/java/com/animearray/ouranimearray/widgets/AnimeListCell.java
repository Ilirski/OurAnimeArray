package com.animearray.ouranimearray.widgets;

import animatefx.animation.Wobble;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;

import java.util.function.Consumer;

public class AnimeListCell extends MFXListCell<AnimeList> {
    private final MFXFontIcon userIcon;

    public AnimeListCell(MFXListView<AnimeList> listView, AnimeList data, ObjectProperty<AnimeList> animeListToAddTo, ObjectProperty<Anime> animeToBeAdded) {
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
            System.out.println(anime);
        });

        setOnMouseDragEntered(event -> {
            getScene().setCursor(Cursor.OPEN_HAND);
            this.setBackground(Background.fill(Paint.valueOf("#f5f5f5")));
            new Wobble(this).play();
        });

        setOnMouseClicked(event -> {

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
