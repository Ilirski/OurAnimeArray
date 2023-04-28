package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.widgets.AnimeList;
import com.animearray.ouranimearray.widgets.AnimeListCell;
import com.animearray.ouranimearray.widgets.Widgets;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.beans.InvalidationListener;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import javafx.util.StringConverter;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.function.Consumer;

import static com.animearray.ouranimearray.widgets.Widgets.createListToggle;

public class LeftSidebarPageViewBuilder implements Builder<Region> {
    private final LeftSidebarPageModel model;
    private final Consumer<Runnable> fetchAnimeList;
    private final Consumer<Runnable> addAnimeToList;
    public LeftSidebarPageViewBuilder(LeftSidebarPageModel model, Consumer<Runnable> fetchAnimeList, Consumer<Runnable> addAnimeToList) {
        this.model = model;
        this.fetchAnimeList = fetchAnimeList;
        this.addAnimeToList = addAnimeToList;
    }

    @Override
    public Region build() {
        MigPane leftSideBar = new MigPane(
                new LC().insets("0").fill()
        );

        MFXListView<AnimeList> animeListView = createAnimeListView();

        // When leftSideBarVisibleProperty is visible, refresh user's the anime list
        model.leftSideBarVisibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                fetchAnimeList.accept(() -> System.out.println("Anime List Fetched"));
            }
        });

        leftSideBar.getStyleClass().add("left-sidebar");

        leftSideBar.add(animeListView, new CC().grow().push().maxWidth("100%"));
        return leftSideBar;
    }

    public MFXListView<AnimeList> createAnimeListView() {

        // Creating the Anime List View
        MFXListView<AnimeList> animeListView = new MFXListView<>(model.animeWatchListProperty());
        StringConverter<AnimeList> converter = FunctionalStringConverter.to(animeList -> (animeList == null) ? "" : animeList.name());
        animeListView.setCellFactory(animeList -> new AnimeListCell(animeListView, animeList, model.animeListToAddToProperty(), model.animeToAddProperty()));
        animeListView.setConverter(converter);
        animeListView.features().enableBounceEffect();
        animeListView.features().enableSmoothScrolling(0.5);

        // When animeToAddProperty is changed, add the anime to the list to be added to
        model.animeToAddProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addAnimeToList.accept(() -> System.out.println("Anime Added"));
            }
        });

        return animeListView;

    }
}
