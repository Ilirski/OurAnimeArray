package com.animearray.ouranimearray.profile;

import com.animearray.ouranimearray.widgets.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProfilePageModel {
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

    public User getCurrentUser() {
        return currentUser.get();
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }
}
