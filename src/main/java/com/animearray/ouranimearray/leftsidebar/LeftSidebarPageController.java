package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class LeftSidebarPageController implements ControllerFX {
    private final Builder<Region> viewBuilder;

    public LeftSidebarPageController(BooleanProperty leftSideBarVisibleProperty) {
        var model = new LeftSidebarPageModel();
        viewBuilder = new LeftSidebarPageViewBuilder(model);
//        leftSideBarVisibleProperty.bind(model.leftSideBarVisibleProperty());
    }
    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
