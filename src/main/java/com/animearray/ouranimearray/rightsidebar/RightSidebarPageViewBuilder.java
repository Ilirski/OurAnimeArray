package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.DAOs.*;
import com.animearray.ouranimearray.widgets.GenreTagsField;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.*;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.animearray.ouranimearray.widgets.Widgets.createAnimePoster;

public class RightSidebarPageViewBuilder implements Builder<Region> {
    private final RightSidebarPageModel model;
    private final Consumer<Runnable> setAnimeStatus;
    private final Consumer<Runnable> setEpisodeWatched;
    private final Consumer<Runnable> getUserAnimeData;
    private final Consumer<Runnable> setUserScore;
    private final Consumer<Runnable> getGenres;

    public RightSidebarPageViewBuilder(RightSidebarPageModel model, Consumer<Runnable> setAnimeStatus,
                                       Consumer<Runnable> setEpisodeWatched, Consumer<Runnable> getUserAnimeData,
                                       Consumer<Runnable> setUserScore, Consumer<Runnable> getGenres) {
        this.model = model;
        this.setAnimeStatus = setAnimeStatus;
        this.setEpisodeWatched = setEpisodeWatched;
        this.getUserAnimeData = getUserAnimeData;
        this.setUserScore = setUserScore;
        this.getGenres = getGenres;
    }

    @Override
    public Region build() {
        MigPane rightSidebarOuter = new MigPane(
                new LC().insets("0").fill()
        );

        // Add to outer pane
        rightSidebarOuter.add(createUserRightSidebar(), new CC().grow().push().hideMode(3));
        rightSidebarOuter.add(createAdminRightSidebar(), new CC().grow().push().hideMode(3));

        // Bind visibility
        rightSidebarOuter.visibleProperty().bindBidirectional(model.rightSidebarVisibleProperty());
        rightSidebarOuter.getStyleClass().add("right-sidebar");

        return rightSidebarOuter;
    }

    private Region createUserRightSidebar() {
        MigPane rightSideBar = new MigPane(
                new LC().insets("10"),
                new AC().align("center")
        );

        var userScrollPane = new MFXScrollPane();
        userScrollPane.setFitToWidth(true);
        userScrollPane.setContent(rightSideBar);

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
        MFXButton exitButton = new MFXButton("Exit");
        exitButton.setOnAction(event -> {
            model.setRightSidebarVisible(false);
            model.setAnime(null);
        });

        MFXButton editButton = new MFXButton("Edit");
        editButton.setOnAction(event -> {
            model.setUserRightSidebarVisible(false);
            model.setAdminRightSidebarVisible(true);
        });
        editButton.visibleProperty().bind(model.adminProperty()); // Only show if admin

        // Scroll to top when new anime is selected
        model.animeProperty().addListener(observable -> userScrollPane.setVvalue(0.0));

        rightSideBar.add(animePoster, new CC().wrap());
        rightSideBar.add(animeTitle, new CC().wrap());
        rightSideBar.add(animeEpisodes, new CC().split(2));
        rightSideBar.add(animeScore, new CC().wrap());
        rightSideBar.add(animeGenres, new CC().wrap());
        rightSideBar.add(statusComboBox, new CC().wrap().hideMode(3));
        rightSideBar.add(episodesWatchedSpinner, new CC().wrap().hideMode(3));
        rightSideBar.add(scoreComboBox, new CC().wrap().hideMode(3));
        rightSideBar.add(animeSynopsis, new CC().wrap());
        rightSideBar.add(exitButton, new CC().split(2));
        rightSideBar.add(editButton, new CC().hideMode(3));

        // User sidebar always visible at start
        userScrollPane.visibleProperty().bindBidirectional(model.userRightSidebarVisibleProperty());

//        userScrollPane.getStyleClass().add("right-sidebar");
        return userScrollPane;
    }

    private Region createAdminRightSidebar() {
        MigPane adminRightSidebar = new MigPane(
                new LC().insets("10"),
                new AC().align("left").fill(),
                new AC().grow().fill()
        );

        var adminScrollPane = new MFXScrollPane();
        adminScrollPane.setFitToWidth(true);
        adminScrollPane.setContent(adminRightSidebar);

        var titleField = new MFXTextField();
        titleField.setFloatingText("Title");
        var imageURLField = new MFXTextField();
        imageURLField.setFloatingText("Image URL");
        var episodesField = new MFXTextField();
        episodesField.setFloatingText("Episodes");
        var scoreField = new MFXTextField();
        scoreField.setFloatingText("Score");
        var synopsisField = new MFXTextField();
        synopsisField.setFloatingText("Synopsis");
        var genresTagsField = new GenreTagsField();

        model.adminRightSidebarVisibleProperty().addListener(observable -> {
            getGenres.accept(() -> {
                System.out.println(model.getGenres());
            });
            genresTagsField.setSuggestionProvider(request -> model.getGenres().stream()
                    .filter(genre -> genre.genre().toLowerCase().contains(request.getUserText().toLowerCase()))
                    .collect(Collectors.toList()));
        });

        var submitButton = new MFXButton("Submit");
        submitButton.setOnAction(event -> {
            var title = titleField.getText();
            var imageURL = imageURLField.getText();
            var episodes = episodesField.getText();
            var score = scoreField.getText();
            List<Genre> genres = genresTagsField.getTags();
            var synopsis = synopsisField.getText();
            var anime = new AnimeDAO(null, title, imageURL, Integer.parseInt(episodes), Double.parseDouble(score), synopsis, genres);
            model.setAnimeToCreateOrModify(anime);
            System.out.println(
                    model.getAnimeToCreateOrModify()
            );
        });

        var viewButton = new MFXButton("View");
        viewButton.setOnAction(event -> {
            model.setUserRightSidebarVisible(true);
            model.setAdminRightSidebarVisible(false);
        });

        // Scroll to top when new anime is selected
        model.animeProperty().addListener(observable -> adminScrollPane.setVvalue(0.0));

        adminRightSidebar.add(titleField, new CC().wrap());
        adminRightSidebar.add(imageURLField, new CC().wrap());
        adminRightSidebar.add(episodesField, new CC().wrap());
        adminRightSidebar.add(scoreField, new CC().wrap());
        adminRightSidebar.add(genresTagsField, new CC().wrap());
        adminRightSidebar.add(synopsisField, new CC().wrap());
        adminRightSidebar.add(submitButton, new CC().split(2));
        adminRightSidebar.add(viewButton);

        adminScrollPane.visibleProperty().bindBidirectional(model.adminRightSidebarVisibleProperty());
        return adminScrollPane;
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

