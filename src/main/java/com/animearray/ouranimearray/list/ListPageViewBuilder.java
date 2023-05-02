package com.animearray.ouranimearray.list;

import com.animearray.ouranimearray.widgets.AnimeGridCell;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
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
    public ListPageViewBuilder(ListPageModel model, Consumer<Runnable> fetchAnimeList, Consumer<Runnable> fetchUserListDetails,
                               Consumer<Runnable> removeAnimeFromList) {
        this.model = model;
        this.fetchAnimeList = fetchAnimeList;
        this.fetchUserListDetails = fetchUserListDetails;
        this.removeAnimeFromList = removeAnimeFromList;
    }

    @Override
    public Region build() {
        var profilePane = new MigPane(
                new LC().fill(),
                new AC().grow(),
                new AC().grow()
        );

        profilePane.add(createUserList(), new CC().dockNorth());
        profilePane.add(createListGridView(fetchAnimeList), new CC().grow());

        return profilePane;
    }

    public MigPane createUserList() {
        var userListPane = new MigPane(
                new LC().fill()
        );

        var listNameLabel = MFXTextField.asLabel();
        listNameLabel.textProperty().bindBidirectional(model.userListProperty().listNameProperty());

        var listDescriptionLabel = MFXTextField.asLabel();
        listDescriptionLabel.textProperty().bindBidirectional(model.userListProperty().descriptionProperty());

//        var trashIcon = new MFXIconWrapper(FontResources.DELETE.getDescription(), 24);
        var trashIcon = new MFXFontIcon(FontResources.DELETE.getDescription(), 32);

        trashIcon.setOnMouseDragEntered(event -> {
            trashIcon.setColor(Color.RED);
        });
        trashIcon.setOnMouseDragReleased(event -> {
            Object source = event.getGestureSource();
            if (!(source instanceof AnimeGridCell)) return;
            System.out.println("Removing anime from list");
            Anime anime = ((AnimeGridCell) source).getItem();
            model.setAnimeIdToRemove(anime.id());
            removeAnimeFromList.accept(() -> {
                System.out.println("Removing " + anime.id() + " from list");
                model.setAnimeIdToRemove(null);
                fetchAnimeList.accept(() -> {
                });
            });
        });


//        var listAnimeCountLabel = MFXTextField.asLabel();

        userListPane.add(listNameLabel, new CC().grow());
        userListPane.add(listDescriptionLabel, new CC().grow());
        userListPane.add(trashIcon, new CC().grow());

        return userListPane;
    }

    private MigPane createListGridView(Consumer<Runnable> fetchAnimeList) {
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
            fetchAnimeList.accept(() -> {
                System.out.println("Fetching Anime List");
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
