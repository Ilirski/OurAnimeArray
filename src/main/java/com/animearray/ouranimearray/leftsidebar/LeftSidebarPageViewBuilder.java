package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.widgets.DAOs.AnimeList;
import com.animearray.ouranimearray.widgets.AnimeListCell;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.effects.DepthLevel;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import javafx.util.StringConverter;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.function.Consumer;

public class LeftSidebarPageViewBuilder implements Builder<Region> {
    private final LeftSidebarPageModel model;
    private final Consumer<Runnable> fetchAnimeList;
    private final Consumer<Runnable> addAnimeToList;
    private final Consumer<Runnable> createNewAnimeList;
    public LeftSidebarPageViewBuilder(LeftSidebarPageModel model, Consumer<Runnable> fetchAnimeList, Consumer<Runnable> addAnimeToList, Consumer<Runnable> createNewAnimeList) {
        this.model = model;
        this.fetchAnimeList = fetchAnimeList;
        this.addAnimeToList = addAnimeToList;
        this.createNewAnimeList = createNewAnimeList;
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

        leftSideBar.add(createMyListsPane(), new CC().dockNorth().alignX("center"));
        leftSideBar.add(animeListView, new CC().grow().push().maxWidth("100%"));
        leftSideBar.add(createAddNewListPane(), new CC().dockSouth().alignX("center"));
        return leftSideBar;
    }

    public MigPane createAddNewListPane() {
        MigPane addNewListPane = new MigPane(
                new LC().fill()
        );

        var addNewAnimeListField = new MFXTextField();
        addNewAnimeListField.setPromptText("Add New List");
        addNewAnimeListField.setFloatMode(FloatMode.BORDER);
        addNewAnimeListField.textProperty().bindBidirectional(model.animeListNameToCreateProperty());
        addNewAnimeListField.setOnAction(event -> {
            model.setNotification("");
        });
        addNewListPane.visibleProperty().bind(model.notificationProperty().isNotEmpty());

        var notificationLabel = new Label();
        notificationLabel.textProperty().bindBidirectional(model.notificationProperty());

        var addNewListButton = new MFXButton("Add");
        addNewListButton.disableProperty().bind(model.animeListNameToCreateProperty().length().lessThan(5));
        addNewListButton.setOnAction(event -> {
            createNewAnimeList.accept(() -> System.out.println("New List Created"));
        });

        addNewListPane.add(addNewAnimeListField, new CC().grow().wrap());
        addNewListPane.add(notificationLabel, new CC().grow().wrap().hideMode(3));
        addNewListPane.add(addNewListButton, new CC().grow().height("10%"));

        return addNewListPane;
    }

    public MigPane createMyListsPane() {
        MigPane myListsPane = new MigPane(
                new LC().fill()
        );

        var label = new Label("My Lists");
        myListsPane.add(label, new CC().grow());

        return myListsPane;
    }

    public MFXListView<AnimeList> createAnimeListView() {

        // Creating the Anime List View
        MFXListView<AnimeList> animeListView = new MFXListView<>(model.animeWatchListProperty());
        StringConverter<AnimeList> converter = FunctionalStringConverter.to(animeList -> (animeList == null) ? "" : animeList.name());
        animeListView.setCellFactory(animeList -> new AnimeListCell(animeListView, animeList, model.animeListToAddToProperty(),
                model.animeToAddProperty(), model.listPageSelectedProperty(), model.listIdProperty()));
        animeListView.setConverter(converter);
        animeListView.features().enableBounceEffect();
        animeListView.features().enableSmoothScrolling(0.5);
        animeListView.setDepthLevel(DepthLevel.LEVEL0);

        // When animeToAddProperty is changed, add the anime to the list to be added to
        model.animeToAddProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addAnimeToList.accept(() -> System.out.println("Anime" + newValue.title() + "Added to List"));
            }
        });

        return animeListView;

    }
}
