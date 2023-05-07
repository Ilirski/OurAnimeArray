package com.animearray.ouranimearray.report;

import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

public class ReportPageViewBuilder implements Builder<Region> {
    private final ReportPageModel model;

    public ReportPageViewBuilder(ReportPageModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        MigPane searchPane = new MigPane(
                new LC().fill(),
                new AC().grow(),
                new AC().grow());

        return searchPane;
    }
}
