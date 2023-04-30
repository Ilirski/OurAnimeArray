package com.animearray.ouranimearray.profile;

import com.animearray.ouranimearray.widgets.DAOs.User;
import com.tobiasdiez.easybind.EasyBind;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
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

        var pictureLabel = new Label("Profile Picture Link: ");
        var pictureField = new MFXTextField();

        var bioLabel = new Label("Bio: ");
        var bioArea = new MFXTextField();

        // Log out button
        var logoutButton = new MFXButton("Log Out");
        logoutButton.setOnAction(e -> {
            model.currentUserProperty().set(null);
        });

        profilePane.add(label, new CC().grow());
        profilePane.add(usernameLabel, new CC().grow());
        profilePane.add(pictureLabel, new CC().cell(0, 1));
        profilePane.add(pictureField, new CC().cell(1, 1));
        profilePane.add(bioLabel, new CC().cell(0, 2));
        profilePane.add(bioArea, new CC().cell(1, 2).grow());
        profilePane.add(logoutButton, new CC().cell(0, 3).grow());
        return profilePane;
    }
}
