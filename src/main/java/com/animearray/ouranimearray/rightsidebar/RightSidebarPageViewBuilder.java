package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.DAOs.Score;
import com.animearray.ouranimearray.widgets.DAOs.WatchStatus;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXSpinner;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import javafx.geometry.Orientation;
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
    private final Consumer<Runnable> setAnimeStatus;
    private final Consumer<Runnable> setEpisodeWatched;
    private final Consumer<Runnable> getUserAnimeData;
    private final Consumer<Runnable> setUserScore;

    public RightSidebarPageViewBuilder(RightSidebarPageModel model, Consumer<Runnable> setAnimeStatus,
                                       Consumer<Runnable> setEpisodeWatched, Consumer<Runnable> getUserAnimeData,
                                       Consumer<Runnable> setUserScore) {
        this.model = model;
        this.setAnimeStatus = setAnimeStatus;
        this.setEpisodeWatched = setEpisodeWatched;
        this.getUserAnimeData = getUserAnimeData;
        this.setUserScore = setUserScore;
    }

    @Override
    public Region build() {
        MigPane rightSideBar = new MigPane(
                new LC().insets("10").fillX(),
                new AC().align("center")
        );

        var scrollPane = new MFXScrollPane();
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
        MFXComboBox<WatchStatus> statusComboBox = createWatchStatusComboBox();

        // Episodes
        MFXSpinner<Integer> episodesWatchedSpinner = createEpisodesWatchedSpinner();

        // Score
        MFXComboBox<Score> scoreComboBox = createScoreComboBox();

        // Notify model when animeProperty is changed
        model.animeProperty().addListener(observable -> {
            if (model.getAnime() == null || !model.isLoggedIn()) {
                return;
            }
            // Clear selection after every anime change
            statusComboBox.clearSelection();
            scoreComboBox.clearSelection();
            getUserAnimeData.accept(() -> {
                statusComboBox.requestLayout();
                episodesWatchedSpinner.requestLayout();
                scoreComboBox.requestLayout();
                // Hacky way of letting the spinner know that the value has changed and it should update
                var val = episodesWatchedSpinner.getValue();
                episodesWatchedSpinner.setValue(0);
                episodesWatchedSpinner.setValue(val);
            });
        });

        // Synopsis
        Label animeSynopsis = new Label();
        animeSynopsis.textProperty().bind(model.animeProperty().synopsisBinding());
        animeSynopsis.setTextAlignment(TextAlignment.JUSTIFY);
        animeSynopsis.setWrapText(true);

        // Exit button
        MFXButton button = new MFXButton("Exit");
        button.setOnAction(event -> {
            model.setRightSideBarVisible(false);
            model.setAnime(null);
        });

        // Bind visibility
        scrollPane.visibleProperty().bindBidirectional(model.rightSideBarVisibleProperty());
        // Scroll to top when new anime is selected
        model.animeProperty().addListener(observable -> scrollPane.setVvalue(0.0));

        rightSideBar.add(animePoster, new CC().wrap());
        rightSideBar.add(animeTitle, new CC().wrap());
        rightSideBar.add(animeEpisodes, new CC().split(2));
        rightSideBar.add(animeScore, new CC().wrap());
        rightSideBar.add(animeGenres, new CC().wrap());
        rightSideBar.add(statusComboBox, new CC().wrap().hideMode(3));
        rightSideBar.add(episodesWatchedSpinner, new CC().wrap().hideMode(3));
        rightSideBar.add(scoreComboBox, new CC().wrap().hideMode(3));
        rightSideBar.add(animeSynopsis, new CC().wrap());
        rightSideBar.add(button);

        scrollPane.getStyleClass().add("right-sidebar");

//        return rightSideBar;
        return scrollPane;
    }

    private MFXSpinner<Integer> createEpisodesWatchedSpinner() {
        MFXSpinner<Integer> episodesWatchedSpinner = new MFXSpinner<>();
        episodesWatchedSpinner.setOrientation(Orientation.HORIZONTAL);
        episodesWatchedSpinner.setPromptText("Episodes Watched");

        // Set spinner model and bind to user anime data
        var episodesWatchedSpinnerModel = new IntegerSpinnerModel();
        episodesWatchedSpinner.setEditable(true);
        episodesWatchedSpinnerModel.maxProperty().bind(model.animeProperty().episodesBinding());
        episodesWatchedSpinnerModel.valueProperty().bindBidirectional(model.userAnimeDataProperty().watchedEpisodesProperty());
        episodesWatchedSpinner.setSpinnerModel(episodesWatchedSpinnerModel);

        // Handles when user types in the spinner
        episodesWatchedSpinner.setOnCommit(text -> {
            if (text.matches("\\d+")) {
                // Check if text is within bounds
                int episode = Integer.parseInt(text);
                if (episode <= model.getAnime().episodes()) {
                    episodesWatchedSpinner.setValue(episode);
                }
            }
        });
        model.userAnimeDataProperty().statusProperty().addListener(observable -> {
            WatchStatus watchStatus = model.getUserAnimeData().watchStatus();
            if (watchStatus == WatchStatus.COMPLETED) {
                // If anime is completed, set episodes watched to total episodes
                System.out.println(model.getAnime().episodes());
                episodesWatchedSpinner.setValue(model.getAnime().episodes());
            }
        });

        // Set text transformer to show "Not Started" when 0 episodes are watched
        episodesWatchedSpinner.setTextTransformer((focused, text) -> {
            if (!focused) {
                if (text.equals("0")) return "Not Started";
                if (text.equals("1")) return text + "/" + model.getAnime().episodes() + " episode";
                return text + "/" + model.getAnime().episodes() + " episodes";
            } else {
                return text;
            }
        });
        episodesWatchedSpinner.visibleProperty().bind(model.loggedInProperty());
        episodesWatchedSpinner.valueProperty().addListener(observable -> {
            setEpisodeWatched.accept(() -> {});
        });
        return episodesWatchedSpinner;
    }

    private MFXComboBox<Score> createScoreComboBox() {
        MFXComboBox<Score> scoreComboBox = new MFXComboBox<>();
        scoreComboBox.setFloatingText("Score");
        scoreComboBox.getItems().setAll(Score.values());
        scoreComboBox.valueProperty().bindBidirectional(model.userAnimeDataProperty().scoreProperty());
        scoreComboBox.visibleProperty().bind(model.loggedInProperty());
        scoreComboBox.setScrollOnOpen(false);
        scoreComboBox.setContextMenuDisabled(true);
        scoreComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Score object) {
                return object == null ? "" : object.toString();
            }

            @Override
            public Score fromString(String string) {
                return null;
            }
        });

        scoreComboBox.setOnAction(event -> {
            Score score = scoreComboBox.getValue();
            if (score == null || score.getRating() == model.getUserAnimeData().score()) {
                return;
            }
            model.setUserScore(score.getRating());
            scoreComboBox.setDisable(true);
            setUserScore.accept(() -> {
                scoreComboBox.setDisable(false);
            });
        });

        return scoreComboBox;
    }

    private MFXComboBox<WatchStatus> createWatchStatusComboBox() {
        MFXComboBox<WatchStatus> statusComboBox = new MFXComboBox<>();
        statusComboBox.setFloatingText("Status");
        statusComboBox.getItems().setAll(WatchStatus.values());
        statusComboBox.visibleProperty().bind(model.loggedInProperty());
        statusComboBox.valueProperty().bindBidirectional(model.userAnimeDataProperty().statusProperty());
        statusComboBox.setScrollOnOpen(false);
        statusComboBox.setContextMenuDisabled(true);

        statusComboBox.setOnAction(event -> {
            WatchStatus watchStatus = statusComboBox.getValue();
            if (statusComboBox.getValue() == null || watchStatus == model.getUserAnimeData().watchStatus()) {
                return;
            }
            model.setUserWatchStatus(watchStatus);
            statusComboBox.setDisable(true);
            setAnimeStatus.accept(() -> {
                statusComboBox.setDisable(false);
            });
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
        return statusComboBox;
    }
}
