package com.animearray.ouranimearray.view;

import animatefx.animation.SlideInUp;
import com.animearray.ouranimearray.model.Anime;
import com.animearray.ouranimearray.model.AnimeGridCell;
import com.animearray.ouranimearray.model.Model;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.controlsfx.control.GridView;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.function.Consumer;

import static com.animearray.ouranimearray.view.Widgets.*;

public class ViewBuilder implements Builder<Region> {
    private final Model model;
    private final Consumer<Runnable> animeFetcher;
    private final Consumer<Runnable> userFetcher;

    public ViewBuilder(Model model, Consumer<Runnable> animeFetcher, Consumer<Runnable> userFetcher) {
        this.model = model;
        this.animeFetcher = animeFetcher;
        this.userFetcher = userFetcher;
    }

    @Override
    public Region build() {
        MigPane basePane = new MigPane(new LC().fill(), new AC().grow(), new AC().grow());

        // Very important, do not change!!!
        basePane.setId("basePane");
        MigPane searchPane = setupSearchPane(animeFetcher);
        MigPane loginPane = setupLoginPane(userFetcher);
        MigPane registerPane = setupRegisterPane(userFetcher);
        MigPane myListPane = setupMyListPane(userFetcher);

        // User sees search pane on startup
        model.currentMainPaneProperty().set(searchPane);

        // Main pane switches according to user menu
        model.currentMainPaneProperty().addListener((observable, oldPane, newPane) -> {
            oldPane.setVisible(false);
            newPane.setVisible(true);
            new SlideInUp(newPane).play();
        });

        // Switch between panes
        basePane.add(model.getCurrentMainPane(), new CC().grow().minWidth("30mm").hideMode(3));
        basePane.add(setupNavigationBar(searchPane, loginPane, registerPane, myListPane), new CC().dockNorth());
        basePane.add(setupLeftSideBar(loginPane), new CC().dockWest().width("15%").hideMode(3));
        basePane.add(setupRightSideBar(), new CC().dockEast().width("20%").hideMode(3));

        // Don't forget to add the other panes to the basePane
        basePane.add(loginPane, new CC().grow().minWidth("30mm").hideMode(3));
        basePane.add(registerPane, new CC().grow().minWidth("30mm").hideMode(3));


        return basePane;
    }

    private MigPane setupMyListPane(Consumer<Runnable> userFetcher) {
        MigPane myListPane = new MigPane(
                new LC().align("center", "center")
        );

        return myListPane;
    }

    private MigPane setupLoginPane(Consumer<Runnable> userFetcher) {
        MigPane loginPane = new MigPane(
                new LC().align("center", "center")
        );

        MFXTextField usernameField = new MFXTextField();
        usernameField.setFloatingText("Username");
        MFXPasswordField passwordField = new MFXPasswordField();
        passwordField.setFloatingText("Password");
        MFXButton loginButton = new MFXButton("Login");

        loginButton.setOnAction(event -> {
            loginButton.setDisable(true);
            userFetcher.accept(() -> loginButton.setDisable(false));
        });

        loginPane.add(usernameField, new CC().wrap().sizeGroup("login").grow().width("100mm"));
        loginPane.add(passwordField, new CC().wrap().sizeGroup("login").grow());
        loginPane.add(loginButton, new CC().wrap().grow());

        loginPane.setVisible(false);
        return loginPane;
    }

    private MigPane setupRegisterPane(Consumer<Runnable> userFetcher) {
        MigPane registerPane = new MigPane(
                new LC().align("center", "center")
        );

        var usernameField = new MFXTextField();
        var validationLabel = new Label();
        validationLabel.setWrapText(true);
        validationLabel.setTextAlignment(TextAlignment.CENTER);
        MFXPasswordField passwordField = Widgets.createPasswordField(validationLabel);

        usernameField.setFloatingText("Username");
        usernameField.getValidator().constraint("The username must be at least 6 characters long", usernameField.textProperty().length().greaterThanOrEqualTo(6));


        MFXButton registerButton = new MFXButton("Register");

//        registerButton.disableProperty().bind();

        registerButton.setOnAction(event -> {
            registerButton.setDisable(true);
            userFetcher.accept(() -> registerButton.setDisable(false));
        });

        registerPane.add(usernameField, new CC().wrap().sizeGroup("login").grow().width("100mm"));
        registerPane.add(passwordField, new CC().wrap().sizeGroup("login").grow());
        registerPane.add(registerButton, new CC().grow().wrap());
        registerPane.add(validationLabel, new CC().maxWidth("100mm").alignX("center"));

        registerPane.setVisible(false);
        return registerPane;
    }

    private MigPane setupSearchPane(Consumer<Runnable> fetchAnimeList) {
        MigPane searchPane = new MigPane(new LC().fill());

        GridView<Anime> animeGridView = new GridView<>(model.animeListProperty());
        animeGridView.setCellFactory(gridView -> new AnimeGridCell(model, true));
        // We want image width : height -> 227.0 : 350.0
        animeGridView.setCellWidth(225);
        animeGridView.setCellHeight(350);
        animeGridView.setHorizontalCellSpacing(10);
        animeGridView.setVerticalCellSpacing(20);

        // So that not on the window edge
        searchPane.add(createSearchBar(model, fetchAnimeList),
                new CC().wrap().width("50%").height("10").alignX("center").alignY("top").gap("0", "0", "10", "5"));
        searchPane.add(animeGridView, new CC().grow());
        return searchPane;
    }

    private MigPane setupNavigationBar(MigPane searchPane, MigPane loginPane, MigPane registerPane, MigPane myListPane) {
        MigPane topSideBar = new MigPane(
                new LC(),
                new AC().gap("push", 0)

        );

        var toggleGroup = new ToggleGroup();

        topSideBar.add(createMyListToggle(model, "mfx-menu-v3", "My List", new ToggleGroup()), new CC().grow().sizeGroup("toggle").alignX("left"));
        topSideBar.add(createNavToggle(model, "mfx-magnifying-glass", "Search", toggleGroup, searchPane), new CC().grow().sizeGroup("toggle").alignX("right"));
        topSideBar.add(createNavToggle(model, "mfx-user", "Login", toggleGroup, loginPane), new CC().grow().sizeGroup("toggle").alignX("right"));
        topSideBar.add(createNavToggle(model, "mfx-user", "Register", toggleGroup, registerPane), new CC().grow().sizeGroup("toggle").alignX("right"));
        topSideBar.getStyleClass().add("navbar");

        return topSideBar;
    }

    private MigPane setupRightSideBar() {
        MigPane rightSideBar = new MigPane(
                new LC().insets("0").fillX(),
                new AC().align("center")
                );

        double targetWidth = 225;
        double targetHeight = 350;

        ImageView animePoster = createAnimePoster(model, targetWidth, targetHeight);

        Label animeTitle = new Label();
        animeTitle.textProperty().bind(model.animeProperty().titleBinding());
        animeTitle.setWrapText(true);

        Label animeSynopsis = new Label();
        animeSynopsis.textProperty().bind(model.animeProperty().synopsisBinding());
        animeSynopsis.setWrapText(true);

        MFXButton button = new MFXButton("Exit");
        button.setOnAction(event -> model.setRightSideBarVisible(false));

        rightSideBar.visibleProperty().bindBidirectional(model.rightSideBarVisibleProperty());

        // Set invisible at start
        rightSideBar.setVisible(model.isRightSideBarVisible());

        rightSideBar.add(animePoster, new CC().wrap());
        rightSideBar.add(animeTitle, new CC().wrap());
        rightSideBar.add(animeSynopsis, new CC().wrap());
        rightSideBar.add(button);

        return rightSideBar;
    }

    private MigPane setupLeftSideBar(MigPane mainPane2) {
        final ToggleGroup toggleGroup = new ToggleGroup();
        MigPane leftSideBar = new MigPane(new LC().insets("0").fill());

        MigPane migScrollPane = new MigPane(new LC().wrapAfter(1));

        migScrollPane.add(createListToggle(model, "mfx-user", "My Lists", toggleGroup, mainPane2), new CC().grow().sizeGroup("toggle"));
        migScrollPane.add(createListToggle(model, "mfx-user", "Recommended", toggleGroup, mainPane2), new CC().grow().sizeGroup("toggle"));

        leftSideBar.visibleProperty().bindBidirectional(model.leftSideBarVisibleProperty());

        leftSideBar.getStyleClass().add("sidebar");
        MFXScrollPane scrollPane = new MFXScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(migScrollPane);

        leftSideBar.add(scrollPane);
        return leftSideBar;
    }
}

