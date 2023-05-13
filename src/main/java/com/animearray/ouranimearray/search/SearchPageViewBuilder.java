package com.animearray.ouranimearray.search;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.SortType;
import io.github.palexdev.materialfx.beans.Alignment;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.controlsfx.control.GridView;
import org.tbee.javafx.scene.layout.MigPane;

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
        searchPane.disableProperty().bind(model.editingProperty());
        return searchPane;
    }

    private Region createSearchGrid(Consumer<Runnable> fetchAnimeList) {
        StackPane stackPane = new StackPane();
        MigPane animeGridPane = new MigPane(new LC().fill());

        // Create grid view
        GridView<Anime> animeGridView = createAnimeGridView(
                model.animeListProperty(),
                model.animeProperty(),
                model.rightSideBarVisibleProperty()
        );

        model.animeProperty().addListener(observable -> {
            if (model.animeProperty().get() == null) {
                fetchAnimeList.accept(() -> {});
            }
        });

        // Create loading spinner
        MFXProgressSpinner loadingSpinner = new MFXProgressSpinner();
        MigPane loadingPane = new MigPane(new LC().fill());
        loadingPane.add(loadingSpinner, new CC().alignX("center").alignY("center").width("15%").height("15%"));
        loadingPane.visibleProperty().bind(model.loadingProperty());

        // Create search bar, sort icon, and add new anime button
        MFXTextField searchBar = createSearchBar(model.searchQueryProperty(), model.loadingProperty(), fetchAnimeList);
        MFXFontIcon sortIcon = new MFXFontIcon(FontResources.FILTER_ALT.getDescription(), 32);
        MFXPopup popup = new MFXPopup();
        MFXComboBox<SortType> sortComboBox = createSortComboBox();
        popup.setContent(sortComboBox);
        sortIcon.setOnMouseClicked(event -> popup.show(sortIcon, Alignment.of(HPos.RIGHT, VPos.CENTER), 40, -35));
        MFXButton addNewAnimeButton = new MFXButton("Add New Anime");
        addNewAnimeButton.setOnAction(event -> {
            model.setAnime(new Anime());
            model.setRightSideBarVisible(true);
        });
        addNewAnimeButton.visibleProperty().bind(model.adminProperty());

        // Add search bar and anime grid view to grid pane
        animeGridPane.add(searchBar, new CC().width("50%").height("10").alignX("center").alignY("top").gap("0", "0", "5", "5").split(3));
        animeGridPane.add(sortIcon, new CC().gapLeft("10"));
        animeGridPane.add(addNewAnimeButton, new CC().gapLeft("10").wrap().hideMode(2));
        animeGridPane.add(animeGridView, new CC().grow().pushY());

        // Add grid pane and loading spinner to stack pane
        stackPane.getChildren().addAll(animeGridPane, loadingPane);
        stackPane.setAlignment(Pos.CENTER);

        loadingSpinner.setId("loading-spinner");
        loadingPane.setId("loading-pane");
        return stackPane;
    }

    private MFXComboBox<SortType> createSortComboBox() {
        MFXComboBox<SortType> sortComboBox = new MFXComboBox<>();
        sortComboBox.setFloatingText("Sort by");
        sortComboBox.getItems().addAll(SortType.values());
        sortComboBox.getSelectionModel().selectItem(SortType.SCORE);
        model.sortTypeProperty().bind(sortComboBox.selectedItemProperty());
        // If sort type changes, fetch new anime list
        model.sortTypeProperty().addListener(observable -> {
            model.setLoading(true);
            fetchAnimeList.accept(() -> model.setLoading(false));
        });
        return sortComboBox;
    }
}
