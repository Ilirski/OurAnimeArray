package com.animearray.ouranimearray.mylists;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

public class MyListsViewBuilder implements Builder<Region> {
    private final MyListsPageModel model;
    public MyListsViewBuilder(MyListsPageModel model) {
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
