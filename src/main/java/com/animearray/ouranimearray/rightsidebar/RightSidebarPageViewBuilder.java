package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.widgets.DAOs.*;
import com.animearray.ouranimearray.widgets.GenreTagsField;
import com.dlsc.gemsfx.ExpandingTextArea;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import io.github.palexdev.materialfx.dialogs.MFXDialogs;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.util.Builder;
import javafx.util.StringConverter;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final Consumer<Runnable> editAnime;
    private final Consumer<Runnable> deleteAnime;
    private final Consumer<Runnable> addAnime;

    public RightSidebarPageViewBuilder(RightSidebarPageModel model, Consumer<Runnable> setAnimeStatus,
                                       Consumer<Runnable> setEpisodeWatched, Consumer<Runnable> getUserAnimeData,
                                       Consumer<Runnable> setUserScore, Consumer<Runnable> getGenres,
                                       Consumer<Runnable> editAnime, Consumer<Runnable> deleteAnime,
                                       Consumer<Runnable> addAnime) {
        this.model = model;
        this.setAnimeStatus = setAnimeStatus;
        this.setEpisodeWatched = setEpisodeWatched;
        this.getUserAnimeData = getUserAnimeData;
        this.setUserScore = setUserScore;
        this.getGenres = getGenres;
        this.editAnime = editAnime;
        this.deleteAnime = deleteAnime;
        this.addAnime = addAnime;
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
        MigPane rightSidebar = new MigPane(
                new LC().insets("0", "0", "5", "0"),
                new AC().align("center")
        );

        MigPane innerPane = new MigPane(
                new LC().insets("10", "10", "0", "10"),
                new AC().align("center")
        );

        // Scroll pane
        var userScrollPane = new MFXScrollPane();
        userScrollPane.setFitToWidth(true);
        userScrollPane.setContent(innerPane);

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
        animeGenres.setTextAlignment(TextAlignment.CENTER);
        animeGenres.textProperty().bind(EasyBind.map((ObservableValue<ObservableList<Genre>>) model.animeProperty().genresBinding(),
                list -> list.stream().map(Genre::genre).collect(Collectors.joining(", "))));
//        animeGenres.textProperty().bind(EasyBind.map(model.animeProperty().genresBinding(),
//                list -> list.stream().map(Genre::genre).collect(Collectors.joining(", "))));
        animeGenres.setWrapText(true);

        // Bind start and end dates together
        Label animeAiredDates = new Label();
        animeAiredDates.textProperty().bind(EasyBind.combine(model.animeProperty().airedStartDateBinding(),
                model.animeProperty().airedEndDateBinding(), (startDate, endDate) -> {
                    String parsedStartDate = parseDate(startDate);
                    String parsedEndDate = parseDate(endDate);

                    if (parsedStartDate.equals("Unknown") && parsedEndDate.equals("Unknown")) {
                        return "Unknown";
                    } else if (parsedEndDate.equals("Unknown")) {
                        return parsedStartDate;
                    } else {
                        return parsedStartDate + " to " + parsedEndDate;
                    }
                }));

        // Status
        MFXComboBox<WatchStatus> statusComboBox = createWatchStatusComboBox();

        // Episodes
        MFXSpinner<Integer> episodesWatchedSpinner = createEpisodesWatchedSpinner();

        // Score
        MFXComboBox<Score> scoreComboBox = createScoreComboBox();

        statusComboBox.valueProperty().addListener(observable -> {
            WatchStatus watchStatus = statusComboBox.getValue();
            if (watchStatus == WatchStatus.COMPLETED) {
                // If anime is completed, set episodes watched to total episodes
                episodesWatchedSpinner.setValue(model.getAnime().episodes());
            } else if (watchStatus == WatchStatus.PLAN_TO_WATCH) {
                // If anime is plan to watch, set episodes watched to 0
                episodesWatchedSpinner.setValue(0);
            }
        });

        // Notify model when animeProperty is changed
        model.animeProperty().addListener(observable -> {
            // Scroll to top
            userScrollPane.setVvalue(0.0);
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
        animeSynopsis.textProperty().bind(EasyBind.map(model.animeProperty().synopsisBinding(),
                synopsis -> synopsis.isBlank() ? "No synopsis found" : synopsis));
        animeSynopsis.setTextAlignment(TextAlignment.JUSTIFY);
        animeSynopsis.setWrapText(true);

        // Exit button
        MFXButton exitButton = createExitButton();

        // Edit button
        MFXButton editButton = createEditButton();

        // Add to inner pane
        innerPane.add(animePoster, new CC().wrap().pushX());
        innerPane.add(animeTitle, new CC().wrap());
        innerPane.add(animeEpisodes, new CC().split(2));
        innerPane.add(animeScore, new CC().wrap());
        innerPane.add(animeGenres, new CC().wrap());
        innerPane.add(animeAiredDates, new CC().wrap());
        innerPane.add(statusComboBox, new CC().wrap().hideMode(2).width("60%"));
        innerPane.add(episodesWatchedSpinner, new CC().wrap().hideMode(2).width("90%"));
        innerPane.add(scoreComboBox, new CC().wrap().hideMode(2).width("60%"));
        innerPane.add(animeSynopsis, new CC().wrap());

        // Add to right sidebar
        rightSidebar.add(userScrollPane, new CC().grow().push().wrap().alignX("center").alignY("top"));
        rightSidebar.add(exitButton, new CC().split(2));
        rightSidebar.add(editButton, new CC().hideMode(3));

        rightSidebar.visibleProperty().bindBidirectional(model.userRightSidebarVisibleProperty());

        return rightSidebar;
    }

    private String parseDate(LocalDate date) {
        String parsedDate;
        try {
            parsedDate = date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
        } catch (Exception e) {
            parsedDate = "Unknown";
        }
        return parsedDate;
    }

    private Region createAdminRightSidebar() {
        MigPane rightSidebar = new MigPane(
                new LC().insets("0", "0", "5", "0").fill(),
                new AC().align("center")
        );

        MigPane adminInnerPane = new MigPane(
                new LC().insets("10", "10", "0", "10"),
                new AC().align("left").gap("5"),
                new AC().align("left")
        );

        // Scroll pane
        var adminScrollPane = new MFXScrollPane();
        adminScrollPane.setFitToWidth(true);
        adminScrollPane.setContent(adminInnerPane);

        var idField = new MFXTextField();
        idField.setFloatingText("Anime ID (Not editable)");
        idField.setEditable(false);
        var titleField = createTitleField();
        var imageURLField = new ExpandingTextArea();
        imageURLField.setPromptText("Image URL");
        var episodesField = createFieldWithRegexConstraint("Episodes", "Must be an integer", "^\\d+$");
        var scoreField = createFieldWithRegexConstraint("Score", "Must be a double", "^\\d+(\\.\\d+)?$");

        var synopsisField = new ExpandingTextArea();
        synopsisField.setPromptText("Synopsis");
        var genresTagsField = new GenreTagsField();

        var airedStartDateField = new MFXDatePicker();
        airedStartDateField.setPromptText("Aired Start Date");
        airedStartDateField.setEditable(false);

        var airedEndDateField = new MFXDatePicker();
        airedEndDateField.setPromptText("Aired End Date");
        airedStartDateField.setEditable(false);

        // Get genres from model
        model.adminRightSidebarVisibleProperty().addListener(observable ->
                getGenres.accept(() ->
                        genresTagsField.setSuggestionProvider(request -> model.getGenres().stream()
                                .filter(genre -> genre.genre().toLowerCase().contains(request.getUserText().toLowerCase()))
                                .collect(Collectors.toList()))));

        // Scroll to top when new anime is selected
        model.animeProperty().addListener(observable -> {
            adminScrollPane.setVvalue(0.0);
            if (model.getAnime() == null || !model.isLoggedIn() || !model.isAdmin()) {
                return;
            }

            // Set fields in edit sidebar
            idField.setText(model.getAnime().id());
            titleField.setText(model.getAnime().title());
            titleField.requestFocus();
            // Check if image is null
            imageURLField.setText(model.getAnime().image() == null ? "" : model.getAnime().image().getUrl());
            episodesField.setText(String.valueOf(model.getAnime().episodes()));
            scoreField.setText(String.valueOf(model.getAnime().score()));
            synopsisField.setText(model.getAnime().synopsis());
            airedStartDateField.setValue(model.getAnime().airedStartDate());
            airedEndDateField.setValue(model.getAnime().airedEndDate());

            // Add genres
            ObservableList<Genre> genres = FXCollections.observableArrayList(model.getAnime().genres());
            genresTagsField.clearTags();
            genres.forEach(genresTagsField::addTags);

            // Hide user right sidebar and show Admin's edit sidebar
            if (Objects.equals(model.getAnime().id(), "Not set")) {
                model.setUserRightSidebarVisible(false);
                model.setAdminRightSidebarVisible(true);
            }
        });

        // Create node only after stage has been shown
        Platform.runLater(() -> {
            MFXGenericDialog confirmDialogContent = MFXDialogs.warn()
                    .makeScrollable(true)
                    .addStylesheets(Objects.requireNonNull(getClass().getResource("/com/animearray/ouranimearray/dialogs.css")).toExternalForm())
                    .get();

            MFXStageDialog confirmDialog = MFXGenericDialogBuilder.build(confirmDialogContent)
                    .toStageDialogBuilder()
                    .initOwner(rightSidebar.getScene().getWindow())
                    .initModality(Modality.APPLICATION_MODAL)
                    .setDraggable(true)
                    .setScrimPriority(ScrimPriority.WINDOW)
                    .setScrimOwner(true)
                    .get();

            confirmDialogContent.setOnClose(event -> {
                confirmDialog.close();
                rightSidebar.setDisable(false);
            });

            MFXButton saveChangesButton = new MFXButton("Save changes");
            saveChangesButton.setOnAction(event -> {
                confirmDialogContent.setHeaderText("Save changes?");
                confirmDialogContent.setContentText("Are you sure you want to save these changes?");
                confirmDialog.show();
                rightSidebar.setDisable(true);
            });

            MFXButton cancelChangesButton = getCancelButton();
            cancelChangesButton.setOnAction(event -> {
                confirmDialogContent.setHeaderText("Cancel changes?");
                confirmDialogContent.setContentText("Are you sure you want to cancel? All changes will be lost.");

                if (Objects.equals(model.getAnime(), new Anime())) {
                    model.setAdminRightSidebarVisible(false);
                    model.setUserRightSidebarVisible(true);
                    model.setRightSidebarVisible(false);
                }

                // Check if any fields have been changed
                if (isAnimeFieldChanged(idField, titleField, imageURLField, episodesField, scoreField, synopsisField, genresTagsField, airedStartDateField, airedEndDateField)) {
                    confirmDialog.show();
                    rightSidebar.setDisable(true);
                } else {
                    // Hide sidebar
                    model.setAdminRightSidebarVisible(false);
                    model.setUserRightSidebarVisible(true);
                }
            });

            MFXButton deleteChangesButton = new MFXButton("Delete");
            deleteChangesButton.setOnAction(event -> {
                confirmDialogContent.setHeaderText("Delete anime?");
                confirmDialogContent.setContentText("Are you sure you want to delete this anime? This action cannot be undone.");
                confirmDialog.show();
                rightSidebar.setDisable(true);
            });

            // If adding new anime, disable delete button
            deleteChangesButton.visibleProperty().bind(EasyBind.wrap(model.animeProperty()).map(anime -> {
                if (anime == null) {
                    return false;
                }
                return !anime.id().equals("Not set");
            }));

            confirmDialogContent.addActions(
                    Map.entry(new MFXButton("Confirm"), event -> {
                        // Get values from fields
                        var animeId = idField.getText();
                        var title = titleField.getText();
                        var imageURL = imageURLField.getText();
                        var episodes = episodesField.getText();
                        var score = scoreField.getText();
                        List<Genre> genres = genresTagsField.getTags();
                        var synopsis = synopsisField.getText();
                        var airedStartDate = airedStartDateField.getValue();
                        var airedEndDate = airedEndDateField.getValue();
                        var animeToSave = new AnimeToSave(animeId, title, imageURL, Integer.parseInt(episodes),
                                Double.parseDouble(score), synopsis, genres, airedStartDate, airedEndDate);
                        var action = confirmDialogContent.getHeaderText();
                        switch (action) {
                            case "Save changes?" -> {
                                // Set anime to save in model
                                model.setAnimeToCreateOrModify(animeToSave);
                                model.setSavingAnime(true);
                                if (Objects.equals(model.getAnime().id(), "Not set")) {
                                    addAnime.accept(() -> {
                                        model.setAnime(null);
                                        model.setSavingAnime(false);
                                        model.setAdminRightSidebarVisible(false);
                                        model.setUserRightSidebarVisible(true);
                                        model.setRightSidebarVisible(false);
                                    });
                                } else {
                                    editAnime.accept(() -> {
                                        model.setAnime(null);
                                        model.setSavingAnime(false);
                                        model.setAdminRightSidebarVisible(false);
                                        model.setUserRightSidebarVisible(true);
                                        model.setRightSidebarVisible(false);
                                    });
                                }
                            }
                            case "Cancel changes?" -> {
                                model.setAnimeToCreateOrModify(null);
                                model.setSavingAnime(false);
                                model.setAdminRightSidebarVisible(false);
                                model.setUserRightSidebarVisible(true);
                                model.setRightSidebarVisible(false);
                            }
                            case "Delete anime?" -> {
                                model.setAnimeToCreateOrModify(animeToSave);
                                model.setSavingAnime(true);
                                deleteAnime.accept(() -> {
                                    model.setAnime(null);
                                    model.setSavingAnime(false);
                                    model.setAdminRightSidebarVisible(false);
                                    model.setUserRightSidebarVisible(true);
                                    model.setRightSidebarVisible(false);
                                });
                            }
                        }
                        confirmDialog.close();
                        rightSidebar.setDisable(false);
                    }),
                    Map.entry(new MFXButton("Cancel"), event -> {
                        confirmDialog.close();
                        rightSidebar.setDisable(false);
                    })
            );

            // Disable save button if any of the fields are invalid or anime is being saved
            saveChangesButton.disableProperty().bind(
                    episodesField.getValidator().validProperty()
                            .and(scoreField.getValidator().validProperty())
                            .and(titleField.getValidator().validProperty())
                            .not()
                            .or(model.savingAnimeProperty())
            );

            rightSidebar.add(adminInnerPane, new CC().grow().push().wrap());
            rightSidebar.add(saveChangesButton, new CC().split(3));
            rightSidebar.add(deleteChangesButton);
            rightSidebar.add(cancelChangesButton);
        });

        adminInnerPane.add(idField, new CC().wrap().growX().pushX());
        adminInnerPane.add(titleField, new CC().wrap().growX().pushX());
        adminInnerPane.add(imageURLField, new CC().wrap().growX().pushX());
        adminInnerPane.add(episodesField, new CC().wrap().growX().pushX());
        adminInnerPane.add(scoreField, new CC().wrap().growX().pushX());
        adminInnerPane.add(genresTagsField, new CC().wrap().growX().pushX());
        adminInnerPane.add(airedStartDateField, new CC().wrap().growX().pushX());
        adminInnerPane.add(airedEndDateField, new CC().wrap().growX().pushX());
        adminInnerPane.add(synopsisField, new CC().wrap().growX().pushX());

        rightSidebar.visibleProperty().bindBidirectional(model.adminRightSidebarVisibleProperty());
        return rightSidebar;
    }

    private boolean isAnimeFieldChanged(MFXTextField idField, MFXTextField titleField, ExpandingTextArea imageURLField,
                                        MFXTextField episodesField, MFXTextField scoreField, ExpandingTextArea synopsisField,
                                        GenreTagsField genresTagsField, MFXDatePicker airedStartDateField, MFXDatePicker airedEndDateField) {
        Anime anime = model.getAnime();
        return anime != null && (!anime.id().equals(idField.getText()) || !anime.title().equals(titleField.getText())
                || (anime.image() != null && !anime.image().getUrl().equals(imageURLField.getText()))
                || anime.episodes() != Integer.parseInt(episodesField.getText())
                || anime.score() != Double.parseDouble(scoreField.getText())
                || !anime.synopsis().equals(synopsisField.getText())
                || !anime.genres().equals(genresTagsField.getTags())
                || !Objects.equals(anime.airedStartDate(), airedStartDateField.getValue())
                || !Objects.equals(anime.airedEndDate(), airedEndDateField.getValue()));
    }

    private MFXButton createEditButton() {
        MFXButton editButton = new MFXButton("Edit");
        editButton.setOnAction(event -> {
            model.setUserRightSidebarVisible(false);
            model.setAdminRightSidebarVisible(true);
        });
        editButton.visibleProperty().bind(model.adminProperty()); // Only show if admin
        return editButton;
    }

    private MFXButton createExitButton() {
        MFXButton exitButton = new MFXButton("Exit");
        exitButton.setOnAction(event -> {
            model.setRightSidebarVisible(false);
            model.setAnime(null);
        });
        return exitButton;
    }

    private static MFXTextField createTitleField() {
        var titleField = new MFXTextField();
        titleField.setFloatingText("Title");
        Constraint titleConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Title cannot be empty")
                .setCondition(titleField.textProperty().isNotEmpty())
                .get();
        titleField.getValidator().constraint(titleConstraint);
        return titleField;
    }

    private static MFXTextField createFieldWithRegexConstraint(String floatingText, String validationMessage, String regex) {
        var textField = new MFXTextField();
        textField.setFloatingText(floatingText);
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(validationMessage)
                .setCondition(Bindings.createBooleanBinding(() -> textField.getText()
                        .matches(regex), textField.textProperty()))
                .get();
        textField.getValidator().constraint(constraint);
        return textField;
    }

    private MFXButton getCancelButton() {
        var cancelButton = new MFXButton("Cancel");
        cancelButton.setOnAction(event -> {
            model.setUserRightSidebarVisible(true);
            model.setAdminRightSidebarVisible(false);
        });
        return cancelButton;
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
        episodesWatchedSpinner.visibleProperty().bind(EasyBind.combine(model.loggedInProperty(), model.adminProperty(), (loggedIn, admin) -> loggedIn && !admin));
        episodesWatchedSpinner.valueProperty().addListener(observable -> setEpisodeWatched.accept(() -> {}));
        return episodesWatchedSpinner;
    }

    private MFXComboBox<Score> createScoreComboBox() {
        MFXComboBox<Score> scoreComboBox = new MFXComboBox<>();
        scoreComboBox.setFloatingText("Score");
        scoreComboBox.getItems().setAll(Score.values());
        scoreComboBox.valueProperty().bindBidirectional(model.userAnimeDataProperty().scoreProperty());
        scoreComboBox.visibleProperty().bind(EasyBind.combine(model.loggedInProperty(), model.adminProperty(), (loggedIn, admin) -> loggedIn && !admin));
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
            setUserScore.accept(() -> scoreComboBox.setDisable(false));
        });

        return scoreComboBox;
    }

    private MFXComboBox<WatchStatus> createWatchStatusComboBox() {
        MFXComboBox<WatchStatus> statusComboBox = new MFXComboBox<>();
        statusComboBox.setFloatingText("Status");
        statusComboBox.getItems().setAll(WatchStatus.values());
        statusComboBox.visibleProperty().bind(EasyBind.combine(model.loggedInProperty(), model.adminProperty(), (loggedIn, admin) -> loggedIn && !admin));
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
            setAnimeStatus.accept(() -> statusComboBox.setDisable(false));
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

