package com.animearray.ouranimearray.profile;

import com.animearray.ouranimearray.widgets.DAOs.User;
import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ProfilePageController implements ControllerFX {
    private final Builder<Region> viewBuilder;

    public ProfilePageController(ObjectProperty<User> currentUserProperty) {
        var model = new ProfilePageModel();
        viewBuilder = new ProfilePageViewBuilder(model);
        model.currentUserProperty().bindBidirectional(currentUserProperty);
    }
    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
