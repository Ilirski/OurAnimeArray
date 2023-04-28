package com.animearray.ouranimearray.list;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

public class ListViewBuilder implements Builder<Region> {
    private final ListPageModel model;
    public ListViewBuilder(ListPageModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        var profilePane = new MigPane(
                new LC().align("center", "center")
        );

        var label = new Label("Profile Page");
        profilePane.add(label, new CC().grow());

        return profilePane;
    }
}
