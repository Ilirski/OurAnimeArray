package com.animearray.ouranimearray.view;

import animatefx.animation.FadeIn;
import animatefx.animation.Wobble;
import com.animearray.ouranimearray.model.AnimeGridCell;
import com.animearray.ouranimearray.model.Model;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.List;
import java.util.function.Consumer;

import static io.github.palexdev.materialfx.utils.StringUtils.containsAny;

public class Widgets {

    static MFXTextField createRegisterUsernameField(Label validationLabel) {
        var usernameField = new MFXTextField();
        setupValidatedControl(usernameField, "Username");

        Constraint lengthConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Username must be at least 5 characters long")
                .setCondition(usernameField.textProperty().length().greaterThanOrEqualTo(5))
                .get();

        usernameField.getValidator()
                .constraint(lengthConstraint);

        addValidationListeners(usernameField, validationLabel);

        return usernameField;
    }
    static MFXPasswordField createRegisterPasswordField(Label validationLabel) {
        var passwordField = new MFXPasswordField();
        final String[] upperChar = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");
        final String[] lowerChar = "a b c d e f g h i j k l m n o p q r s t u v w x y z".split(" ");
        final String[] digits = "0 1 2 3 4 5 6 7 8 9".split(" ");

        setupValidatedControl(passwordField, "Password");

        Constraint lengthConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Password must be at least 8 characters long")
                .setCondition(passwordField.textProperty().length().greaterThanOrEqualTo(8))
                .get();

        Constraint digitConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Password must contain at least one digit")
                .setCondition(Bindings.createBooleanBinding(
                        () -> containsAny(passwordField.getText(), "", digits),
                        passwordField.textProperty()
                ))
                .get();

        Constraint charactersConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Password must contain at least one lowercase and one uppercase characters")
                .setCondition(Bindings.createBooleanBinding(
                        () -> containsAny(passwordField.getText(), "", upperChar) && containsAny(passwordField.getText(), "", lowerChar),
                        passwordField.textProperty()
                ))
                .get();

        passwordField.getValidator()
                .constraint(digitConstraint)
                .constraint(charactersConstraint)
                .constraint(lengthConstraint);

        addValidationListeners(passwordField, validationLabel);

        return passwordField;
    }

    private static void setupValidatedControl(MFXTextField control, String floatingText) {
        control.getStyleClass().add("validatedField");
        control.setFloatingText(floatingText);
    }

    private static void addValidationListeners(MFXTextField control, Label validationLabel) {
        final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

        control.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                validationLabel.setVisible(false);
                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });

        control.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Constraint> constraints = control.validate();
            if (!constraints.isEmpty()) {
                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                validationLabel.setText(constraints.get(0).getMessage());
                validationLabel.setVisible(true);
            }
        });
    }

    static Label createValidationLabel() {
        var validationLabel = new Label();
        validationLabel.getStyleClass().add("validationLabel");
        validationLabel.setWrapText(true);
        validationLabel.setTextAlignment(TextAlignment.CENTER);
        validationLabel.setVisible(false);
        return validationLabel;
    }

    static ImageView createAnimePoster(Model model, double targetWidth, double targetHeight) {
        ImageView animePoster = new ImageView();
        animePoster.setFitWidth(targetWidth);
        animePoster.setFitHeight(targetHeight);
        animePoster.setPreserveRatio(true);
        animePoster.setSmooth(true);
        animePoster.imageProperty().bind(model.animeProperty().imageBinding());
        return animePoster;
    }

    static MFXTextField createSearchBar(Model model, Consumer<Runnable> fetchAnimeList) {
        MFXTextField searchBar = new MFXTextField();
        searchBar.setFloatMode(FloatMode.BORDER);
        searchBar.setFloatingText("Search");
        searchBar.setTrailingIcon(new MFXFontIcon("mfx-magnifying-glass", 12));
        searchBar.textProperty().bindBidirectional(model.searchQueryProperty());
        searchBar.setOnAction(event -> {
            searchBar.setDisable(true);
            fetchAnimeList.accept(() -> searchBar.setDisable(false));
        });

        return searchBar;
    }
    static MFXRectangleToggleNode createListToggle(Model model, String text, ToggleGroup toggleGroup, MigPane paneToSwitch) {
        MFXIconWrapper wrapper = new MFXIconWrapper(FontResources.FOLDER.getDescription(), 24, 32);

        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        toggleNode.setOnAction(event -> {
            model.setCurrentMainPane(paneToSwitch);
            model.setRightSideBarVisible(false);
        });

        toggleNode.setOnMouseDragReleased(event -> {
            var hi = (AnimeGridCell) event.getGestureSource();
            System.out.println(hi.getItem());
        });

        toggleNode.setOnMouseDragEntered(event -> {
            toggleNode.getScene().setCursor(Cursor.OPEN_HAND);
            new Wobble(toggleNode).play();
        });

        return toggleNode;
    }

    static MFXRectangleToggleNode createMyListToggle(Model model, String icon, String text, ToggleGroup toggleGroup) {
        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);

        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        toggleNode.setOnAction(event -> model.setLeftSideBarVisible(!model.isLeftSideBarVisible()));

        return toggleNode;
    }

    static MFXRectangleToggleNode createNavToggle(Model model, String icon, String text, ToggleGroup toggleGroup, MigPane paneToSwitch) {
        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);

        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);

        toggleNode.setOnAction(event -> {
            model.setCurrentMainPane(paneToSwitch);
            model.setLeftSideBarVisible(false);
            model.setRightSideBarVisible(false);
        });

        return toggleNode;
    }

    static Hyperlink createPaneLink(MigPane loginPane, MigPane registerPane, String text) {
        var paneLink = new Hyperlink(text);
        paneLink.setTextAlignment(TextAlignment.CENTER);
        paneLink.setAlignment(Pos.CENTER);
        paneLink.setOnAction(event -> {
            loginPane.setVisible(false);
            registerPane.setVisible(true);
            new FadeIn(registerPane).play();
        });
        return paneLink;
    }
}
