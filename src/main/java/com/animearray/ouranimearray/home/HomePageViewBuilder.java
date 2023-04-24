package com.animearray.ouranimearray.home;

import animatefx.animation.SlideInUp;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.font.FontResources;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import static com.animearray.ouranimearray.widgets.Widgets.*;

public class HomePageViewBuilder implements Builder<Region> {
    private final HomePageModel model;
    private final Region searchPage;
    private final Region loginRegisterPage;
    private final Region profilePage;
    private final Region rightSideBar;
    private final Region leftSideBar;

    public HomePageViewBuilder(HomePageModel model, Region searchPage, Region loginRegisterPage, Region profilePage, Region rightSideBar, Region leftSideBar) {
        this.model = model;
        this.searchPage = searchPage;
        this.loginRegisterPage = loginRegisterPage;
        this.profilePage = profilePage;
        this.rightSideBar = rightSideBar;
        this.leftSideBar = leftSideBar;
    }

    @Override
    public Region build() {
        MigPane homePane = new MigPane(
                new LC().fill(),
                new AC().grow(),
                new AC().grow());

        // Add animation
        EasyBind.subscribe(searchPage.visibleProperty(), e -> new SlideInUp(searchPage).play());
        EasyBind.subscribe(profilePage.visibleProperty(), e -> new SlideInUp(profilePage).play());
        EasyBind.subscribe(loginRegisterPage.visibleProperty(), e -> new SlideInUp(loginRegisterPage).play());

        homePane.add(createNavigationBar(), new CC().dockNorth());
        homePane.add(searchPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(loginRegisterPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(profilePage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(leftSideBar, new CC().dockWest().width("15%").hideMode(3));
        homePane.add(rightSideBar, new CC().dockEast().width("20%").hideMode(3));
        homePane.getStyleClass().add("home-page"); // Important for CSS and drag and drop functionality - Do not remove
        return homePane;
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

        myListsToggle.setOnAction(e -> {
//            model.setLeftSideBarVisible(!model.leftSideBarVisibleProperty().get());
        });

        // Ensure one toggle is always selected
        toggleGroup.getToggles().addAll(searchToggle, loginRegisterToggle, profileToggle);
        toggleGroup.selectToggle(searchToggle);
        toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });

        // Switching between searchToggle and other toggles will keep rightSideBar visibility
        model.searchPageSelectedProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal) {
                // If user has viewed an anime page before, show the rightSideBar
                model.setRightSideBarVisible(model.animeProperty().isNotNull().get());
            } else {
                model.rightSideBarVisibleProperty().set(false);
            }
        });

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
}
