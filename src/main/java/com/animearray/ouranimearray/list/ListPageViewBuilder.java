package com.animearray.ouranimearray.list;

import animatefx.animation.Bounce;
import com.animearray.ouranimearray.widgets.AnimeGridCell;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.controlsfx.control.GridView;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.function.Consumer;

import static com.animearray.ouranimearray.widgets.Widgets.createAnimeGridView;

public class ListPageViewBuilder implements Builder<Region> {
    private final ListPageModel model;
    private final Consumer<Runnable> fetchAnimeList;
    private final Consumer<Runnable> fetchUserListDetails;
    private final Consumer<Runnable> removeAnimeFromList;
    private final Consumer<Runnable> editUserAnimeList;
    public ListPageViewBuilder(ListPageModel model, Consumer<Runnable> fetchAnimeList, Consumer<Runnable> fetchUserListDetails,
                               Consumer<Runnable> removeAnimeFromList, Consumer<Runnable> editUserAnimeList) {
        this.model = model;
        this.fetchAnimeList = fetchAnimeList;
        this.fetchUserListDetails = fetchUserListDetails;
        this.removeAnimeFromList = removeAnimeFromList;
        this.editUserAnimeList = editUserAnimeList;
    }

    @Override
    public Region build() {
        var profilePane = new MigPane(
                new LC().fill(),
                new AC().grow(),
                new AC().grow()
        );
        StackPane stackPane = new StackPane();

        MigPane loadingPane = new MigPane(new LC().fill());
        var loadingSpinner = new MFXProgressSpinner();
        loadingPane.visibleProperty().bind(model.loadingProperty());
        loadingPane.add(loadingSpinner, new CC().alignX("center").alignY("center").width("15%").height("15%"));
        loadingSpinner.setId("loading-spinner");
        loadingPane.setId("loading-pane");

        profilePane.add(createUserList(), new CC().dockNorth());
        profilePane.add(createListGridView(fetchAnimeList), new CC().grow());
        stackPane.getChildren().addAll(profilePane, loadingPane);

        return stackPane;
    }

    public MigPane createUserList() {
        var userListPane = new MigPane(
                new LC().fill().insets("5", "20", "5", "5")
        );

        var listNameLabel = new Label();
        listNameLabel.textProperty().bindBidirectional(model.userListProperty().listNameProperty());
        listNameLabel.visibleProperty().bind(model.editListInfoProperty().not());
        listNameLabel.setId("list-name");

        var listDescriptionLabel = new Label();
        listDescriptionLabel.textProperty().bind(Bindings.when(model.userListProperty().descriptionProperty().isEmpty())
                .then("No description... Add one!")
                .otherwise(model.userListProperty().descriptionProperty()));
        listDescriptionLabel.visibleProperty().bind(model.editListInfoProperty().not());
        listDescriptionLabel.setId("list-description");

        var trashIcon = new MFXFontIcon(FontResources.DELETE.getDescription(), 32);
        trashIcon.visibleProperty().bind(model.editListInfoProperty().not());

        trashIcon.setOnMouseDragEntered(event -> trashIcon.setColor(Color.RED));

        trashIcon.setOnMouseDragExited(event -> trashIcon.setColor(Color.WHITE));

        trashIcon.setOnMouseDragReleased(event -> {
            Object source = event.getGestureSource();
            if (!(source instanceof AnimeGridCell)) return;
            Anime anime = ((AnimeGridCell) source).getItem();
            model.setAnimeIdToRemove(anime.id());
            model.setLoading(true);
            removeAnimeFromList.accept(() -> {
                model.setAnimeIdToRemove(null);
                fetchAnimeList.accept(() -> {
                    model.setLoading(false);
                    new Bounce(trashIcon).play();
                });
            });
        });

        var editAnimeListButton = new MFXButton("Edit List Info");
        editAnimeListButton.visibleProperty().bind(model.editListInfoProperty().not());
        editAnimeListButton.setOnAction(event -> model.setEditListInfo(true));

        var listNameEditField = new MFXTextField();
        listNameEditField.textProperty().bindBidirectional(model.userListProperty().listNameProperty());
        listNameEditField.visibleProperty().bind(model.editListInfoProperty());

        var listDescriptionEditField = new MFXTextField();
        listDescriptionEditField.textProperty().bindBidirectional(model.userListProperty().descriptionProperty());
        listDescriptionEditField.visibleProperty().bind(model.editListInfoProperty());

        var saveListInfoButton = new MFXButton("Save");
        saveListInfoButton.visibleProperty().bind(model.editListInfoProperty());
        saveListInfoButton.setOnAction(event -> {
            model.setEditListInfo(false);
            model.setLoading(true);
            editUserAnimeList.accept(() -> fetchUserListDetails.accept(() -> model.setLoading(false)));
        });

        var cancelListInfoButton = new MFXButton("Cancel");
        cancelListInfoButton.visibleProperty().bind(model.editListInfoProperty());
        cancelListInfoButton.setOnAction(event -> {
            model.setEditListInfo(false);
            model.setLoading(true);
            fetchAnimeList.accept(() -> model.setLoading(false));
        });

        userListPane.add(listNameLabel, new CC().grow().wrap().hideMode(3));
        userListPane.add(listDescriptionLabel, new CC().grow().wrap().hideMode(3));
        userListPane.add(listNameEditField, new CC().grow().wrap().hideMode(3));
        userListPane.add(listDescriptionEditField, new CC().grow().wrap().hideMode(3));
        userListPane.add(editAnimeListButton, new CC().grow().width("10%").hideMode(3));
        userListPane.add(saveListInfoButton, new CC().grow().width("10%").hideMode(3));
        userListPane.add(cancelListInfoButton, new CC().grow().width("10%").hideMode(3));
        userListPane.add(trashIcon, new CC().grow().width("10%").alignX("left").hideMode(3));

        return userListPane;
    }

    private Region createListGridView(Consumer<Runnable> fetchAnimeList) {
        MigPane animeGridPane = new MigPane(new LC().fill());

        GridView<Anime> animeGridView = createAnimeGridView(model.animeListProperty(), model.animeProperty(), model.rightSideBarVisibleProperty());

        model.listIdProperty().addListener(observable -> {
            System.out.println("List ID changed");
            fetchUserListDetails.accept(() -> {
                // Honestly there's some bug here where user list doesn't load
                // apparently when I add print statements the bug goes away
                // ¯\_(ツ)_/¯
                System.out.println("Fetching User List Details");
            });
            model.setLoading(true);
            fetchAnimeList.accept(() -> {
                System.out.println("Fetching Anime List");
                model.setLoading(false);
            });
        });

        var label = new Label("No Anime in List... Add some by dragging animes from the search page!");
        label.visibleProperty().bind(model.animeListProperty().emptyProperty());

        // So that not on the window edge
        animeGridPane.add(animeGridView, new CC().grow());
        animeGridPane.add(label, new CC().grow().hideMode(3));
        return animeGridPane;
    }
}
