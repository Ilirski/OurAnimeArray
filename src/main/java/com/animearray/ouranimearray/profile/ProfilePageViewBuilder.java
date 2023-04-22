package com.animearray.ouranimearray.profile;

import com.animearray.ouranimearray.loginregister.LoginRegisterPageModel;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

public class ProfilePageViewBuilder implements Builder<Region> {
    private final ProfilePageModel model;
    public ProfilePageViewBuilder(ProfilePageModel model) {
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
