package com.animearray.ouranimearray.home;

import animatefx.animation.SlideInUp;
import com.animearray.ouranimearray.widgets.DAOs.User;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.font.FontResources;
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
    private final Region animeDatabasePage;

    public HomePageViewBuilder(HomePageModel model, Region searchPage, Region loginRegisterPage, Region profilePage,
                               Region rightSideBar, Region leftSideBar, Region listPage, Region animeDatabasePage) {
        this.model = model;
        this.searchPage = searchPage;
        this.loginRegisterPage = loginRegisterPage;
        this.profilePage = profilePage;
        this.rightSideBar = rightSideBar;
        this.leftSideBar = leftSideBar;
        this.listPage = listPage;
        this.animeDatabasePage = animeDatabasePage;
    }

    @Override
    public Region build() {
        MigPane homePane = new MigPane(new LC().fill(), new AC().grow(), new AC().grow());

        // Add animation
        EasyBind.subscribe(searchPage.visibleProperty(), e -> new SlideInUp(searchPage).play());
        EasyBind.subscribe(profilePage.visibleProperty(), e -> new SlideInUp(profilePage).play());
        EasyBind.subscribe(loginRegisterPage.visibleProperty(), e -> new SlideInUp(loginRegisterPage).play());
        EasyBind.subscribe(listPage.visibleProperty(), e -> new SlideInUp(listPage).play());
        EasyBind.subscribe(animeDatabasePage.visibleProperty(), e -> new SlideInUp(animeDatabasePage).play());

        // Side bars
        homePane.add(createNavigationBar(), new CC().dockNorth());
        homePane.add(leftSideBar, new CC().dockWest().width("15%").hideMode(3));
        homePane.add(rightSideBar, new CC().dockEast().width("20%").hideMode(3));

        // Home page
        homePane.add(searchPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(loginRegisterPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(profilePage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(listPage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.add(animeDatabasePage, new CC().grow().minWidth("30mm").hideMode(3));
        homePane.getStyleClass().add("home-page"); // Important for CSS and drag and drop functionality - Do not remove
        return homePane;
    }

    private MigPane createNavigationBar() {
        MigPane topSideBar = new MigPane(new LC(), new AC().gap("push", 0));

        var toggleGroup = new ToggleGroup();
        var myListsToggle = createNavToggle(FontResources.MENU_V3, "My Lists");
        var searchToggle = createNavToggle(FontResources.MAGNIFYING_GLASS, "Search");
        var loginRegisterToggle = createNavToggle(FontResources.USER, "Login / Register");
        var profileToggle = createNavToggle(FontResources.USER, "Profile");
        var animeDatabaseToggle = createNavToggle(FontResources.FILES, "Anime Database");

        // Bind text of profileToggle to username of currentUser
        profileToggle.textProperty().bind(EasyBind.wrapNullable(model.currentUserProperty()).map(User::username).orElse("Profile"));

        toggleGroup.getToggles().addAll(searchToggle, loginRegisterToggle, profileToggle, animeDatabaseToggle); // Add all toggles to toggleGroup
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

        // If user logs out, select searchToggle
        model.currentUserProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                toggleGroup.selectToggle(searchToggle);
            }
        });

        // If list page is selected, unselect all toggles
        model.listPageSelectedProperty().addListener((obsVal, oldVal, newVal) -> {
            if (!oldVal) {
                System.out.println("List page selected");
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

        // Bind visible property to toggle selected property

        // Search page - Bidirectional to allow set
        model.searchPageSelectedProperty().bindBidirectional(searchToggle.selectedProperty());
        searchPage.visibleProperty().bindBidirectional(model.searchPageSelectedProperty());

        // Login/Register page
        model.loginRegisterPageSelectedProperty().bind(loginRegisterToggle.selectedProperty());
        loginRegisterPage.visibleProperty().bind(model.loginRegisterPageSelectedProperty());

        // Profile page
        model.profilePageSelectedProperty().bind(profileToggle.selectedProperty());
        profilePage.visibleProperty().bind(model.profilePageSelectedProperty());

        // Left sidebar
        model.leftSideBarVisibleProperty().bind(myListsToggle.selectedProperty());
        leftSideBar.visibleProperty().bind(model.leftSideBarVisibleProperty());

        // List page
        listPage.visibleProperty().bind(model.listPageSelectedProperty());

        // Anime database page
        model.animeDatabasePageSelectedProperty().bind(animeDatabaseToggle.selectedProperty());
        animeDatabasePage.visibleProperty().bind(model.animeDatabasePageSelectedProperty());

        // Set toggle button visibility based on logged in or not
        loginRegisterToggle.visibleProperty().bind(model.loggedInProperty().not());
        profileToggle.visibleProperty().bind(model.loggedInProperty());
        myListsToggle.visibleProperty().bind(EasyBind.combine(model.loggedInProperty(), model.adminProperty(), (loggedIn, admin) -> loggedIn && !admin));

        // Set toggle button visibility based on admin or not
        animeDatabaseToggle.visibleProperty().bind(model.adminProperty());

        // Add toggles to navbar
        topSideBar.add(myListsToggle, new CC().grow().sizeGroup("toggle").alignX("left"));
        topSideBar.add(animeDatabaseToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));
        topSideBar.add(searchToggle, new CC().grow().sizeGroup("toggle").alignX("right"));
        topSideBar.add(loginRegisterToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));
        topSideBar.add(profileToggle, new CC().grow().sizeGroup("toggle").alignX("right").hideMode(3));

        topSideBar.getStyleClass().add("navbar");

        return topSideBar;
    }
}
