package com.animearray.ouranimearray.home;

import com.animearray.ouranimearray.loginregister.LoginRegisterPageController;
import com.animearray.ouranimearray.profile.ProfilePageController;
import com.animearray.ouranimearray.search.SearchPageController;
import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HomePageController implements ControllerFX {
    private final Builder<Region> viewBuilder;

    public HomePageController() {
        HomePageModel model = new HomePageModel();
        viewBuilder = new HomePageViewBuilder(model,
                new SearchPageController(model.animeProperty(), model.rightSideBarVisibleProperty()).getViewBuilder(),
                new LoginRegisterPageController(model.currentUserProperty(), model.searchPageSelectedProperty()).getViewBuilder(),
                new ProfilePageController().getViewBuilder()
        );
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
