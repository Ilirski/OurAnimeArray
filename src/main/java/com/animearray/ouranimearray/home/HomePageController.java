package com.animearray.ouranimearray.home;

import com.animearray.ouranimearray.leftsidebar.LeftSidebarPageController;
import com.animearray.ouranimearray.list.ListPageController;
import com.animearray.ouranimearray.loginregister.LoginRegisterPageController;
import com.animearray.ouranimearray.profile.ProfilePageController;
import com.animearray.ouranimearray.rightsidebar.RightSidebarPageController;
import com.animearray.ouranimearray.search.SearchPageController;
import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HomePageController implements ControllerFX {
    private final Builder<Region> viewBuilder;
    private final HomePageInteractor interactor;

    public HomePageController() {
        HomePageModel model = new HomePageModel();
        interactor = new HomePageInteractor(model);
        viewBuilder = new HomePageViewBuilder(model,
                new SearchPageController(
                        model.animeProperty(),
                        model.rightSideBarVisibleProperty(),
                        model.adminProperty(),
                        model.editingProperty()
                ).getViewBuilder(),
                new LoginRegisterPageController(
                        model.currentUserProperty(),
                        model.searchPageSelectedProperty()
                ).getViewBuilder(),
                new ProfilePageController(
                        model.currentUserProperty()
                ).getViewBuilder(),
                new RightSidebarPageController(
                        model.animeProperty(),
                        model.currentUserProperty(),
                        model.adminProperty(),
                        model.rightSideBarVisibleProperty(),
                        model.editingProperty()
                ).getViewBuilder(),
                new LeftSidebarPageController(
                        model.currentUserProperty(),
                        model.leftSideBarVisibleProperty(),
                        model.listPageSelectedProperty(),
                        model.listIdProperty()
                ).getViewBuilder(),
                new ListPageController(
                        model.listPageSelectedProperty(),
                        model.animeProperty(),
                        model.rightSideBarVisibleProperty(),
                        model.listIdProperty()
                ).getViewBuilder()
        );
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
