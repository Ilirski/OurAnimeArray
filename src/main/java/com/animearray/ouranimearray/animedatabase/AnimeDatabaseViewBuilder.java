package com.animearray.ouranimearray.animedatabase;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

public class AnimeDatabaseViewBuilder implements Builder<Region> {
    private final AnimeDatabasePageModel model;
    public AnimeDatabaseViewBuilder(AnimeDatabasePageModel model) {
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
