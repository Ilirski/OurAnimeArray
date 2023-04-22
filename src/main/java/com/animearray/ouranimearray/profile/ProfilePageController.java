package com.animearray.ouranimearray.profile;

import com.animearray.ouranimearray.loginregister.LoginRegisterPageInteractor;
import com.animearray.ouranimearray.loginregister.LoginRegisterPageModel;
import com.animearray.ouranimearray.loginregister.LoginRegisterPageViewBuilder;
import com.animearray.ouranimearray.model.User;
import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ProfilePageController implements ControllerFX {
    private final Builder<Region> viewBuilder;

    public ProfilePageController() {
        var model = new ProfilePageModel();
        viewBuilder = new ProfilePageViewBuilder(model);
    }
    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
