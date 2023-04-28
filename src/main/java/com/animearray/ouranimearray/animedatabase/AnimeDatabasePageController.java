package com.animearray.ouranimearray.animedatabase;

import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class AnimeDatabasePageController implements ControllerFX {
    private final Builder<Region> viewBuilder;

    public AnimeDatabasePageController() {
        var model = new AnimeDatabasePageModel();
        viewBuilder = new AnimeDatabaseViewBuilder(model);
    }
    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
