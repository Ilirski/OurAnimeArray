package com.animearray.ouranimearray.report;

import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ReportPageController implements ControllerFX {
    private final Builder<Region> viewBuilder;

    public ReportPageController() {
        var model = new ReportPageModel();
        viewBuilder = new ReportPageViewBuilder(model);
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
