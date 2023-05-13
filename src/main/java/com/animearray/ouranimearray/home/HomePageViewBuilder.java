package com.animearray.ouranimearray.home;

import animatefx.animation.SlideInUp;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.User;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.font.FontResources;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import static com.animearray.ouranimearray.widgets.Widgets.createNavToggle;

public class HomePageViewBuilder implements Builder<Region> {
    private final HomePageModel model;
    private final Region searchPage;
    private final Region loginRegisterPage;
    private final Region profilePage;
    private final Region rightSideBar;
    private final Region leftSideBar;
    private final Region listPage;
    private final Region reportPage;

    public HomePageViewBuilder(HomePageModel model, Region searchPage, Region loginRegisterPage, Region profilePage,
                               Region rightSideBar, Region leftSideBar, Region listPage, Region reportPage) {
        this.model = model;
        this.searchPage = searchPage;
        this.loginRegisterPage = loginRegisterPage;
        this.profilePage = profilePage;
        this.rightSideBar = rightSideBar;
        this.leftSideBar = leftSideBar;
        this.listPage = listPage;
        this.reportPage = reportPage;
    }

    @Override
    public Region build() {
        MigPane homePane = new MigPane(new LC().fill(), new AC().grow(), new AC().grow());

        // Add animation
        EasyBind.subscribe(searchPage.visibleProperty(), e -> new SlideInUp(searchPage).play());
        EasyBind.subscribe(profilePage.visibleProperty(), e -> new SlideInUp(profilePage).play());
        EasyBind.subscribe(loginRegisterPage.visibleProperty(), e -> new SlideInUp(loginRegisterPage).play());
        EasyBind.subscribe(listPage.visibleProperty(), e -> new SlideInUp(listPage).play());
        EasyBind.subscribe(reportPage.visibleProperty(), e -> new SlideInUp(reportPage).play());

        // Side bars
        homePane.add(createNavigationBar(), new CC().dockNorth());
        homePane.add(leftSideBar, new CC().dockWest().width("15%").hideMode(3));
        homePane.add(rightSideBar, new CC().dockEast().width("25%").hideMode(3));

        // Home page
        homePane.add(searchPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(loginRegisterPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(profilePage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(listPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(reportPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.getStyleClass().add("home-page"); // Important for CSS and drag and drop functionality - Do not remove
        return homePane;
    }

    private MigPane createNavigationBar() {
        MigPane topSideBar = new MigPane(new LC(), new AC().gap("push", 1));

        var toggleGroup = new ToggleGroup();
        var myListsToggle = createNavToggle(FontResources.MENU_V3, "My Lists");
        var searchToggle = createNavToggle(FontResources.MAGNIFYING_GLASS, "Search");
        var loginRegisterToggle = createNavToggle(FontResources.USER, "Login / Register");
        var profileToggle = createNavToggle(FontResources.USER, "Profile");
        var reportToggle = createNavToggle(FontResources.BARS, "Report");

        // Bind text of profileToggle to username of currentUser
        profileToggle.textProperty().bind(EasyBind.wrapNullable(model.currentUserProperty()).map(User::username).orElse("Profile"));

        // If admin is logged in, change text and icon of searchToggle
        searchToggle.textProperty().bind(Bindings.when(model.adminProperty()).then("Anime Database").otherwise("Search"));
        searchToggle.labelLeadingIconProperty().bind(Bindings.when(model.adminProperty())
                .then(new MFXIconWrapper(FontResources.FILES.getDescription(), 24, 32))
                .otherwise(new MFXIconWrapper(FontResources.MAGNIFYING_GLASS.getDescription(), 24, 32)));

        // Add all toggles to toggleGroup
        toggleGroup.getToggles().addAll(searchToggle, loginRegisterToggle, profileToggle, reportToggle);
        toggleGroup.selectToggle(searchToggle); // Select searchToggle by default

        // Ensure one toggle is always selected
        toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null && !model.isListPageSelected()) {
                oldVal.setSelected(true);
            } else if (newVal != null) {
                model.setListPageSelected(false); // Set list page as not selected if user selects a toggle
                model.setListId(null);
            }
        });

        // If user logs in or out, select searchToggle
        model.currentUserProperty().addListener((obsVal, oldVal, newVal) -> {
            if (oldVal == null) {
                Anime anime = model.animeProperty().get();
                model.setAnime(null);
                model.setAnime(anime);
                toggleGroup.selectToggle(searchToggle);
            } else {
                model.setRightSideBarVisible(true);
                model.setAnime(null);
                toggleGroup.selectToggle(searchToggle);
            }
        });

        // If list page is selected, unselect all toggles
        model.listPageSelectedProperty().addListener((obsVal, oldVal, newVal) -> {
            if (!oldVal) {
                toggleGroup.getToggles().forEach(toggle -> toggle.setSelected(false));
            }
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

        model.profilePageSelectedProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal) {
                model.setRightSideBarVisible(false);
            }
        });

        // Search page - Bidirectional to allow set
        model.searchPageSelectedProperty().bindBidirectional(searchToggle.selectedProperty());
        searchPage.visibleProperty().bindBidirectional(model.searchPageSelectedProperty());

        // Login/Register page
        model.loginRegisterPageSelectedProperty().bind(loginRegisterToggle.selectedProperty());
        loginRegisterPage.visibleProperty().bind(model.loginRegisterPageSelectedProperty());
        loginRegisterToggle.visibleProperty().bind(model.loggedInProperty().not());

        // Profile page
        model.profilePageSelectedProperty().bind(profileToggle.selectedProperty());
        profilePage.visibleProperty().bind(model.profilePageSelectedProperty());
        profileToggle.visibleProperty().bind(model.loggedInProperty());

        // Left sidebar
        model.leftSideBarVisibleProperty().bind(EasyBind.combine(myListsToggle.visibleProperty(), myListsToggle.selectedProperty(), (visible, selected) -> visible && selected));
        leftSideBar.visibleProperty().bind(model.leftSideBarVisibleProperty());

        // Report page
        model.reportPageSelectedProperty().bind(reportToggle.selectedProperty());
        reportPage.visibleProperty().bind(model.reportPageSelectedProperty());
        reportToggle.visibleProperty().bind(model.adminProperty());

        // List page
        listPage.visibleProperty().bind(model.listPageSelectedProperty());

        // My list toggle
        myListsToggle.visibleProperty().bind(EasyBind.combine(model.loggedInProperty(), model.adminProperty(), (loggedIn, admin) -> loggedIn && !admin));

        // OurAnimeArray logo
        Label label = new Label("OurAnimeArray");
        label.getStyleClass().add("logo");

        // Add toggles to navbar
        topSideBar.add(myListsToggle, new CC().grow().sizeGroup("toggle").alignX("left").hideMode(2));
        topSideBar.add(label, new CC().grow().sizeGroup("toggle").alignX("left"));
        topSideBar.add(reportToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));
        topSideBar.add(searchToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));
        topSideBar.add(loginRegisterToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));
        topSideBar.add(profileToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));

        topSideBar.getStyleClass().add("navbar");

        return topSideBar;
    }
}
