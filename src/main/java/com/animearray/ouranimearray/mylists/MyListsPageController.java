package com.animearray.ouranimearray.mylists;

import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MyListsPageController implements ControllerFX {
    private final Builder<Region> viewBuilder;

    public MyListsPageController() {
        var model = new MyListsPageModel();
        viewBuilder = new MyListsViewBuilder(model);
    }
    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
