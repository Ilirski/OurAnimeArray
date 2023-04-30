package com.animearray.ouranimearray.search;

import com.animearray.ouranimearray.widgets.AnimeProperty;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.AnimeGridCell;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.controlsfx.control.GridView;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.Objects;
import java.util.function.Consumer;

import static com.animearray.ouranimearray.widgets.Widgets.createAnimeGridView;
import static com.animearray.ouranimearray.widgets.Widgets.createSearchBar;

public class SearchPageViewBuilder implements Builder<Region> {
    private final SearchPageModel model;
    private final Consumer<Runnable> fetchAnimeList;

    public SearchPageViewBuilder(SearchPageModel model, Consumer<Runnable> fetchAnimeList) {
        this.model = model;
        this.fetchAnimeList = fetchAnimeList;
    }
    @Override
    public Region build() {
        MigPane searchPane = new MigPane(
                new LC().fill(),
                new AC().grow(),
                new AC().grow());

        searchPane.add(createSearchGrid(fetchAnimeList), new CC().grow());
        return searchPane;
    }

    private Region createSearchGrid(Consumer<Runnable> fetchAnimeList) {
        StackPane stackPane = new StackPane();
        MigPane animeGridPane = new MigPane(new LC().fill());

        GridView<Anime> animeGridView = createAnimeGridView(model.animeListProperty(), model.animeProperty(),model.rightSideBarVisibleProperty());

        // Add loading spinner
        MigPane loadingPane = new MigPane(new LC().fill());
        var loadingSpinner = new MFXProgressSpinner();
        loadingPane.visibleProperty().bind(model.loadingProperty());
        loadingPane.add(loadingSpinner, new CC().alignX("center").alignY("center").width("15%").height("15%"));
        stackPane.getChildren().addAll(animeGridPane, loadingPane);

        loadingSpinner.setId("loading-spinner");
        loadingPane.setId("loading-pane");

        // So that not on the window edge
        animeGridPane.add(createSearchBar(model.searchQueryProperty(), model.loadingProperty(), fetchAnimeList),
                new CC().wrap().width("50%").height("10").alignX("center").alignY("top").gap("0", "0", "10", "5"));
        animeGridPane.add(animeGridView, new CC().grow().pushY());
        return stackPane;
    }
}
