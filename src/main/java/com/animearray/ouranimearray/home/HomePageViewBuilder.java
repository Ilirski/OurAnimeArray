package com.animearray.ouranimearray.home;

import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.font.FontResources;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.animearray.ouranimearray.widgets.Widgets.*;

public class HomePageViewBuilder implements Builder<Region> {
    private final HomePageModel model;
    private final Region searchPage;
    private final Region loginRegisterPage;
    private final Region profilePage;
    private final Region leftSideBar;

    public HomePageViewBuilder(HomePageModel model, Region searchPage, Region loginRegisterPage, Region profilePage) {
        this.model = model;
        this.searchPage = searchPage;
        this.loginRegisterPage = loginRegisterPage;
        this.profilePage = profilePage;
        this.leftSideBar = createLeftSideBar();
    }

    @Override
    public Region build() {
        MigPane basePane = new MigPane(
                new LC().fill(),
                new AC().grow(),
                new AC().grow());
        basePane.setId("basePane");

        basePane.add(createNavigationBar(), new CC().dockNorth());
        basePane.add(searchPage, new CC().grow().minWidth("30mm").hideMode(3));
        basePane.add(loginRegisterPage, new CC().grow().minWidth("30mm").hideMode(3));
        basePane.add(profilePage, new CC().grow().minWidth("30mm").hideMode(3));
        basePane.add(leftSideBar, new CC().dockWest().width("15%").hideMode(3));
        basePane.add(createRightSideBar(), new CC().dockEast().width("20%").hideMode(3));
        return basePane;
    }

    private MigPane createNavigationBar() {
        MigPane topSideBar = new MigPane(
                new LC(),
                new AC().gap("push", 0)
        );

        var toggleGroup = new ToggleGroup();
        var myListsToggle = createNavToggle(FontResources.MENU_V3, "My Lists");
        var searchToggle = createNavToggle(FontResources.MAGNIFYING_GLASS, "Search");
        var loginRegisterToggle = createNavToggle(FontResources.USER, "Login / Register");
        var profileToggle = createNavToggle(FontResources.USER, "Profile");

        toggleGroup.getToggles().addAll(searchToggle, loginRegisterToggle, profileToggle);
        toggleGroup.selectToggle(searchToggle);

        // Ensure one toggle is always selected
        toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });

        // Switching between searchToggle and other toggles will keep rightSideBar visibility
        toggleGroup.selectedToggleProperty().addListener((observable -> {
            if (model.isSearchPageSelected()) {
                model.setRightSideBarVisible(model.animeProperty().isNotNull().get());
            } else {
                model.rightSideBarVisibleProperty().set(false);
            }
        }));

        // Bidirectional to allow set
        model.searchPageSelectedProperty().bindBidirectional(searchToggle.selectedProperty());
        searchPage.visibleProperty().bindBidirectional(model.searchPageSelectedProperty());

        model.loginRegisterPageSelectedProperty().bind(loginRegisterToggle.selectedProperty());
        loginRegisterPage.visibleProperty().bind(model.loginRegisterPageSelectedProperty());

        model.profilePageSelectedProperty().bind(profileToggle.selectedProperty());
        profilePage.visibleProperty().bind(model.profilePageSelectedProperty());

        model.leftSideBarVisibleProperty().bind(myListsToggle.selectedProperty());
        leftSideBar.visibleProperty().bind(model.leftSideBarVisibleProperty());

        // Set toggle visibility based on logged in or not
        loginRegisterToggle.visibleProperty().bind(model.loggedInProperty().not());
        profileToggle.visibleProperty().bind(model.loggedInProperty());
        myListsToggle.visibleProperty().bind(model.loggedInProperty());

        topSideBar.add(myListsToggle, new CC().grow().sizeGroup("toggle").alignX("left"));
        topSideBar.add(searchToggle, new CC().grow().sizeGroup("toggle").alignX("right"));
        topSideBar.add(loginRegisterToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));
        topSideBar.add(profileToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));

        topSideBar.getStyleClass().add("navbar");

        return topSideBar;
    }

    private MigPane createRightSideBar() {
        MigPane rightSideBar = new MigPane(
                new LC().insets("0").fillX(),
                new AC().align("center")
        );

        double targetWidth = 225;
        double targetHeight = 350;

        // Image
        ImageView animePoster = createAnimePoster(model, targetWidth, targetHeight);

        // Title
        Label animeTitle = new Label();
        animeTitle.textProperty().bind(model.animeProperty().titleBinding());
        animeTitle.setWrapText(true);

        // Episode
        Label animeEpisodes = new Label();
        animeEpisodes.textProperty().bind(EasyBind.map(model.animeProperty().episodesBinding(), Object::toString));
        animeEpisodes.setWrapText(true);

        // Score
        Label animeScore = new Label();
        animeScore.textProperty().bind(EasyBind.map(model.animeProperty().scoreBinding(), Object::toString));
        animeScore.setWrapText(true);

        // Synopsis
        Label animeSynopsis = new Label();
        animeSynopsis.textProperty().bind(model.animeProperty().synopsisBinding());
        animeSynopsis.setWrapText(true);

        // Genres
        Label animeGenres = new Label();

        // capitalize each word in a phrase.
        // TODO: change sql schema to have genres be capitalized so this doesn't happen.
        animeGenres.textProperty().bind(EasyBind.map(model.animeProperty().genresBinding(),
                list -> list.stream()
                        .map(phrase -> Arrays.stream(phrase.split(" "))
                                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                                .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining(", "))));
        animeGenres.setWrapText(true);

        MFXButton button = new MFXButton("Exit");
        button.setOnAction(event -> {
            model.setRightSideBarVisible(false);
            model.setAnime(null);
        });

        rightSideBar.visibleProperty().bindBidirectional(model.rightSideBarVisibleProperty());

        // Set invisible at start
        rightSideBar.setVisible(model.isRightSideBarVisible());

        rightSideBar.add(animePoster, new CC().wrap());
        rightSideBar.add(animeTitle, new CC().wrap());
        rightSideBar.add(animeEpisodes, new CC().split(2));
        rightSideBar.add(animeScore, new CC().wrap());
        rightSideBar.add(animeGenres, new CC().wrap());
        rightSideBar.add(animeSynopsis, new CC().wrap());
        rightSideBar.add(button);

        return rightSideBar;
    }

    private MigPane createLeftSideBar() {
        MigPane leftSideBar = new MigPane(
                new LC().insets("0").fill()
        );

        ToggleGroup toggleGroup = new ToggleGroup();

        MigPane migScrollPane = new MigPane(new LC().wrapAfter(1));

        var list1 = createListToggle("My Lists");
        var list2 = createListToggle("Recommended");

        toggleGroup.getToggles().addAll(list1, list2);

        migScrollPane.add(list1, new CC().grow().sizeGroup("toggle"));
        migScrollPane.add(list2, new CC().grow().sizeGroup("toggle"));

        leftSideBar.getStyleClass().add("sidebar");
        var scrollPane = new MFXScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(migScrollPane);

        leftSideBar.add(scrollPane);
        return leftSideBar;
    }
}
