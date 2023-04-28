package com.animearray.ouranimearray.profile;

import com.animearray.ouranimearray.widgets.User;
import com.tobiasdiez.easybind.EasyBind;
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

        var usernameLabel = new Label();
        usernameLabel.textProperty().bind(EasyBind.wrapNullable(model.currentUserProperty()).map(User::username).asOrdinary());

        profilePane.add(label, new CC().grow());
        profilePane.add(usernameLabel, new CC().grow());
        return profilePane;
    }
}
