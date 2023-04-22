package com.animearray.ouranimearray.widgets;

import animatefx.animation.Wobble;
import com.animearray.ouranimearray.home.HomePageModel;
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
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.function.Consumer;

import static io.github.palexdev.materialfx.utils.StringUtils.containsAny;

public class Widgets {

    public static MFXTextField createRegisterUsernameField() {
        var usernameField = new MFXTextField();
        setupValidatedControl(usernameField, "Username");

        Constraint lengthConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Username must be at least 5 characters long")
                .setCondition(usernameField.textProperty().length().greaterThanOrEqualTo(5))
                .get();

        usernameField.getValidator()
                .constraint(lengthConstraint);

        return usernameField;
    }
    public static MFXPasswordField createRegisterPasswordField() {
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

        return passwordField;
    }

    private static void setupValidatedControl(MFXTextField control, String floatingText) {
        control.getStyleClass().add("validatedField");
        control.setFloatingText(floatingText);
    }

    public static void addValidationListeners(StringProperty errorMessage, MFXTextField control) {
        final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

        control.getValidator().validProperty().addListener((observable) -> {
            if (control.getValidator().isValid()) {
                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                errorMessage.set("");
            }
        });

        control.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Constraint> constraints = control.validate();
            if (!constraints.isEmpty()) {
                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                errorMessage.set(constraints.get(0).getMessage());
            }
        });
    }

    public static Label createValidationLabel(StringProperty errorMessageProperty) {
        var validationLabel = new Label();
        validationLabel.getStyleClass().add("validationLabel");
        validationLabel.setWrapText(true);
        validationLabel.setTextAlignment(TextAlignment.CENTER);
//        validationLabel.setTextFill(Color.RED);
        validationLabel.visibleProperty().bind(errorMessageProperty.isNotEmpty());
        validationLabel.textProperty().bind(errorMessageProperty);
        return validationLabel;
    }

    public static ImageView createAnimePoster(HomePageModel model, double targetWidth, double targetHeight) {
        ImageView animePoster = new ImageView();
        animePoster.setFitWidth(targetWidth);
        animePoster.setFitHeight(targetHeight);
        animePoster.setPreserveRatio(true);
        animePoster.setSmooth(true);
        animePoster.imageProperty().bind(model.animeProperty().imageBinding());
        return animePoster;
    }

    public static MFXTextField createSearchBar(StringProperty searchQueryProperty, Consumer<Runnable> fetchAnimeList) {
        MFXTextField searchBar = new MFXTextField();
        searchBar.setFloatMode(FloatMode.BORDER);
        searchBar.setFloatingText("Search");
        searchBar.setTrailingIcon(new MFXFontIcon("mfx-magnifying-glass", 12));
        searchBar.textProperty().bindBidirectional(searchQueryProperty);
        searchBar.setOnAction(event -> {
            searchBar.setDisable(true);
            fetchAnimeList.accept(() -> searchBar.setDisable(false));
        });

        return searchBar;
    }
    public static MFXRectangleToggleNode createListToggle(String text) {
        // icon.getDescription() returns the proper string of the icon
        var wrapper = new MFXIconWrapper(FontResources.FOLDER.getDescription(), 24, 32);
        var toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);

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

    public static MFXRectangleToggleNode createNavToggle(FontResources icon, String text) {
        // icon.getDescription() returns the proper string of the icon
        MFXIconWrapper wrapper = new MFXIconWrapper(icon.getDescription(), 24, 32);

        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);

        return toggleNode;
    }

    public static Hyperlink createPaneLink(String text) {
        var paneLink = new Hyperlink(text);
        paneLink.setTextAlignment(TextAlignment.CENTER);
        paneLink.setAlignment(Pos.CENTER);
        return paneLink;
    }
}
