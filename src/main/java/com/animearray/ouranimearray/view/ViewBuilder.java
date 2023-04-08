package com.animearray.ouranimearray.view;

import animatefx.animation.FadeIn;
import animatefx.animation.SlideInUp;
import com.animearray.ouranimearray.model.Anime;
import com.animearray.ouranimearray.model.AnimeGridCell;
import com.animearray.ouranimearray.model.Model;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
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
    private final Consumer<Runnable> userRegister;

    public ViewBuilder(Model model, Consumer<Runnable> animeFetcher, Consumer<Runnable> userFetcher, Consumer<Runnable> userRegister) {
        this.model = model;
        this.animeFetcher = animeFetcher;
        this.userFetcher = userFetcher;
        this.userRegister = userRegister;
    }

    @Override
    public Region build() {
        MigPane basePane = new MigPane(new LC().fill(), new AC().grow(), new AC().grow());
        basePane.setId("basePane");

        // Setup panes
        MigPane searchPane = setupSearchPane(animeFetcher);
        MigPane loginPane = setupLoginPane(userFetcher);
        MigPane registerPane = setupRegisterPane(userRegister);
        MigPane loginRegisterPane = setupLoginRegisterPane(loginPane, registerPane);

        // User sees search pane on startup
        model.currentMainPaneProperty().set(searchPane);
        // When user logs in, switch user view to searchPane
        model.isLoggedInProperty().addListener(observable -> {
            if (model.isLoggedInProperty().getValue()) {
                model.currentMainPaneProperty().set(searchPane);
            }
        });

        // Main pane switches according to user menu
        model.currentMainPaneProperty().addListener((observable, oldPane, newPane) -> {
            oldPane.setVisible(false);
            newPane.setVisible(true);
            new SlideInUp(newPane).play();
        });

        // Switch between panes
        basePane.add(model.getCurrentMainPane(), new CC().grow().minWidth("30mm").hideMode(3));
        basePane.add(setupNavigationBar(searchPane, loginRegisterPane), new CC().dockNorth());
        basePane.add(setupLeftSideBar(loginPane), new CC().dockWest().width("15%").hideMode(3));
        basePane.add(setupRightSideBar(), new CC().dockEast().width("20%").hideMode(3));

        // Don't forget to add the other panes to the basePane
        basePane.add(loginRegisterPane, new CC().grow().minWidth("30mm").hideMode(3));

        return basePane;
    }

    private MigPane setupLoginRegisterPane(MigPane loginPane, MigPane registerPane) {
        var loginRegisterPane = new MigPane(
                new LC().align("center", "center")
        );

        loginRegisterPane.add(loginPane, new CC().hideMode(3));
        loginRegisterPane.add(registerPane, new CC().hideMode(3));

        var registerPaneLink = new Hyperlink("Don't have an account?");
        registerPaneLink.setTextAlignment(TextAlignment.CENTER);
        registerPaneLink.setAlignment(Pos.CENTER);
        registerPaneLink.setOnAction(event -> {
            loginPane.setVisible(false);
            registerPane.setVisible(true);
            new FadeIn(registerPane).play();
        });

        loginPane.add(registerPaneLink, new CC().grow());

        var loginPaneLink = new Hyperlink("Already have an account?");
        loginPaneLink.setTextAlignment(TextAlignment.CENTER);
        loginPaneLink.setAlignment(Pos.CENTER);
        loginPaneLink.setOnAction(event -> {
            registerPane.setVisible(false);
            loginPane.setVisible(true);
            new FadeIn(loginPane).play();
        });

        registerPane.add(loginPaneLink, new CC().grow());

        loginPane.setVisible(true);
        loginRegisterPane.setVisible(false);
        return loginRegisterPane;
    }

    private MigPane setupMyListPane(Consumer<Runnable> userFetcher) {
        MigPane myListPane = new MigPane(
                new LC().align("center", "center")
        );

        return myListPane;
    }

    private MigPane setupLoginPane(Consumer<Runnable> userFetcher) {
        MigPane loginPane = new MigPane(
                new LC().align("center", "center").wrap()
        );

        var validationLabel = createValidationLabel();
        validationLabel.setVisible(false);

        var usernameField = new MFXTextField();
        usernameField.setFloatingText("Username");
        usernameField.textProperty().bindBidirectional(model.usernameLoginProperty());

        var passwordField = new MFXPasswordField();
        passwordField.setFloatingText("Password");
        passwordField.textProperty().bindBidirectional(model.passwordLoginProperty());

        MFXButton loginButton = new MFXButton("Login");

        var isInvalid = EasyBind.combine(usernameField.textProperty(), passwordField.textProperty(),
                (username, password) -> username.isEmpty() || password.isEmpty());

        BooleanProperty isFetchingUser = new SimpleBooleanProperty(false);

        var disableRegisterButtonBinding = EasyBind.combine(isInvalid, isFetchingUser,
                (invalid, fetching) -> invalid || fetching);

        loginButton.disableProperty().bind(disableRegisterButtonBinding);

        loginButton.setOnAction(event -> {
            isFetchingUser.set(true);
            userFetcher.accept(() -> {
                isFetchingUser.set(false);
                if (!model.isLoggedInProperty().getValue()) {
                    validationLabel.setVisible(true);
                    validationLabel.setText("Wrong username or password, please try again");
                }
            });
        });

        loginPane.add(usernameField, new CC().sizeGroup("login").grow().width("100mm"));
        loginPane.add(passwordField, new CC().sizeGroup("login").grow());
        loginPane.add(loginButton, new CC().grow());
        loginPane.add(validationLabel, new CC().maxWidth("100mm").alignX("center").hideMode(3));

        loginPane.setVisible(false);
        return loginPane;
    }

    private MigPane setupRegisterPane(Consumer<Runnable> userFetcher) {
        MigPane registerPane = new MigPane(
                new LC().align("center", "center").wrap()
        );

        Label validationLabel = createValidationLabel();

        var usernameField = createRegisterUsernameField(validationLabel);
        usernameField.textProperty().bindBidirectional(model.usernameRegisterProperty());

        var passwordField = createRegisterPasswordField(validationLabel);
        passwordField.textProperty().bindBidirectional(model.passwordRegisterProperty());

        var registerButton = new MFXButton("Register");

        var isInvalid = EasyBind.combine(usernameField.getValidator().validProperty(), passwordField.getValidator().validProperty(),
                (username, password) -> !username || !password);

        BooleanProperty isFetchingUser = new SimpleBooleanProperty(false);

        var disableRegisterButtonBinding = EasyBind.combine(isInvalid, isFetchingUser,
                (invalid, fetching) -> invalid || fetching);

        registerButton.disableProperty().bind(disableRegisterButtonBinding);

        registerButton.setOnAction(event -> {
            isFetchingUser.set(true);
            userFetcher.accept(() -> {
                isFetchingUser.set(false);
                model.setUsernameRegister("");
                model.setPasswordRegister("");
            });
        });

        registerPane.add(usernameField, new CC().sizeGroup("login").grow().width("100mm"));
        registerPane.add(passwordField, new CC().sizeGroup("login").grow());
        registerPane.add(registerButton, new CC().grow());
        registerPane.add(validationLabel, new CC().maxWidth("100mm").alignX("center").hideMode(3));

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

    private MigPane setupNavigationBar(MigPane searchPane, MigPane loginRegisterPane) {
        MigPane topSideBar = new MigPane(
                new LC(),
                new AC().gap("push", 0)
        );

        var toggleGroup = new ToggleGroup();
        // Ensure one toggle is always selected
        toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });

        var myListButton = createMyListToggle(model, "mfx-menu-v3", "My List", new ToggleGroup());
        var searchButton = createNavToggle(model, "mfx-magnifying-glass", "Search", toggleGroup, searchPane);
        searchButton.setSelected(true);
        var loginRegisterButton = createNavToggle(model, "mfx-user", "Login / Register", toggleGroup, loginRegisterPane);
        var myProfileButton = createNavToggle(model, "mfx-user", "My Profile", toggleGroup, loginRegisterPane);

        myProfileButton.visibleProperty().bind(model.isLoggedInProperty());
        myListButton.visibleProperty().bind(model.isLoggedInProperty());
        // When model.isLoggedInProperty is equal to false
        loginRegisterButton.visibleProperty().bind(EasyBind.map(model.isLoggedInProperty(), Boolean.FALSE::equals));

        topSideBar.add(myListButton, new CC().grow().sizeGroup("toggle").alignX("left"));
        topSideBar.add(searchButton, new CC().grow().sizeGroup("toggle").alignX("right"));
        topSideBar.add(loginRegisterButton, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));
        topSideBar.add(myProfileButton, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));

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