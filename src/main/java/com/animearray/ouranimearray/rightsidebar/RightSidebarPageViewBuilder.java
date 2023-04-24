package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.WatchStatus;
import com.animearray.ouranimearray.widgets.MFXSilentComboBox;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;
import javafx.util.StringConverter;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.function.Consumer;

import static com.animearray.ouranimearray.widgets.Widgets.createAnimePoster;

public class RightSidebarPageViewBuilder implements Builder<Region> {
    private final RightSidebarPageModel model;
    private final Consumer<Runnable> getAnimeStatus;
    private final Consumer<Runnable> setAnimeStatus;
    public RightSidebarPageViewBuilder(RightSidebarPageModel model, Consumer<Runnable> getAnimeStatus, Consumer<Runnable> setAnimeStatus) {
        this.model = model;
        this.getAnimeStatus = getAnimeStatus;
        this.setAnimeStatus = setAnimeStatus;
    }

    @Override
    public Region build() {
        MigPane rightSideBar = new MigPane(
                new LC().insets("10").fillX(),
                new AC().align("center")
        );

        var scrollPane = new MFXScrollPane();
        // Account for insets
        scrollPane.minWidthProperty().bind(EasyBind.map(rightSideBar.widthProperty(),
                width -> width.doubleValue() + 10));
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(rightSideBar);

        double targetWidth = 225;
        double targetHeight = 350;

        // Image
        ImageView animePoster = createAnimePoster(model.animeProperty(), targetWidth, targetHeight);

        // Title
        Label animeTitle = new Label();
        animeTitle.textProperty().bind(model.animeProperty().titleBinding());
        animeTitle.setWrapText(true);
        // Set font style to bold
        animeTitle.setFont(Font.font(animeTitle.getFont().getFamily(), FontWeight.BOLD, animeTitle.getFont().getSize()));

        // Episode
        Label animeEpisodes = new Label();
        animeEpisodes.textProperty().bind(EasyBind.map(model.animeProperty().episodesBinding(), Object::toString));
        animeEpisodes.setWrapText(true);

        // Score
        Label animeScore = new Label();
        animeScore.textProperty().bind(EasyBind.map(model.animeProperty().scoreBinding(), Object::toString));
        animeScore.setWrapText(true);

        // Genres
        Label animeGenres = new Label();

        // Join list of genres
        animeGenres.textProperty().bind(EasyBind.map(model.animeProperty().genresBinding(),
                list -> String.join(", ", list)));
        animeGenres.setWrapText(true);

        // Status
        MFXSilentComboBox<WatchStatus> statusComboBox = createWatchStatusComboBox();

        // Synopsis
        Label animeSynopsis = new Label();
        animeSynopsis.textProperty().bind(model.animeProperty().synopsisBinding());
        animeSynopsis.setTextAlignment(TextAlignment.JUSTIFY);
        animeSynopsis.setWrapText(true);

        MFXButton button = new MFXButton("Exit");
        button.setOnAction(event -> {
            model.setRightSideBarVisible(false);
            model.setAnime(null);
        });

        scrollPane.visibleProperty().bindBidirectional(model.rightSideBarVisibleProperty());

        rightSideBar.add(animePoster, new CC().wrap());
        rightSideBar.add(animeTitle, new CC().wrap());
        rightSideBar.add(animeEpisodes, new CC().split(2));
        rightSideBar.add(animeScore, new CC().wrap());
        rightSideBar.add(animeGenres, new CC().wrap());
        rightSideBar.add(statusComboBox, new CC().wrap().hideMode(3));
        rightSideBar.add(animeSynopsis, new CC().wrap());
        rightSideBar.add(button);

        scrollPane.getStyleClass().add("right-sidebar");

//        return rightSideBar;
        return scrollPane;
    }

    private MFXSilentComboBox<WatchStatus> createWatchStatusComboBox() {
        MFXSilentComboBox<WatchStatus> statusComboBox = new MFXSilentComboBox<>();
        statusComboBox.setFloatingText("Status");
        statusComboBox.getItems().setAll(WatchStatus.values());
        statusComboBox.setOnAction(event -> {
            if (statusComboBox.getValue() != null) {
                System.out.println("Ehhh... someone called me?");
                model.setWatchStatus(statusComboBox.getValue());
                statusComboBox.setDisable(true);
                setAnimeStatus.accept(() -> statusComboBox.setDisable(false));
            }
        });
        statusComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(WatchStatus object) {
                return object == null ? "" : object.toString();
            }

            @Override
            public WatchStatus fromString(String string) {
                return null;
            }
        });
        statusComboBox.setContextMenuDisabled(true);
        statusComboBox.floatingProperty().bindBidirectional(model.watchStatusNotSetProperty());
        model.animeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && model.isLoggedIn()) {
                statusComboBox.setDisable(true);
                getAnimeStatus.accept(() -> {
                    // Wait for model.watchStatusProperty to be updated
                    statusComboBox.clearSelection();
                    statusComboBox.setValueSilently(model.getWatchStatus());
                    statusComboBox.requestLayout();
                    statusComboBox.setDisable(false);
                });
            }
        });
        statusComboBox.visibleProperty().bind(model.loggedInProperty());
        return statusComboBox;
    }
}
