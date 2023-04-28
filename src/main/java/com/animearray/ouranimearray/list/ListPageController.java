package com.animearray.ouranimearray.list;

import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ListPageController implements ControllerFX {
    private final Builder<Region> viewBuilder;

    public ListPageController(BooleanProperty listPageSelectedProperty) {
        var model = new ListPageModel();
        viewBuilder = new ListViewBuilder(model);
        model.listPageSelectedProperty.bindBidirectional(listPageSelectedProperty);
    }
    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
