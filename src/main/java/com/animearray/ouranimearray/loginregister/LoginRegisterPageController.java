package com.animearray.ouranimearray.loginregister;

import com.animearray.ouranimearray.model.User;
import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class LoginRegisterPageController implements ControllerFX {
    private final LoginRegisterPageInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final BooleanProperty searchPageSelectedProperty;

    public LoginRegisterPageController(ObjectProperty<User> currentUserProperty, BooleanProperty searchPageSelectedProperty) {
        LoginRegisterPageModel model = new LoginRegisterPageModel();
        interactor = new LoginRegisterPageInteractor(model);
        viewBuilder = new LoginRegisterPageViewBuilder(model, this::verifyUser, this::registerUser);
        this.searchPageSelectedProperty = searchPageSelectedProperty;
        model.currentUserProperty().bindBidirectional(currentUserProperty);
    }

    private void verifyUser(Runnable postFetchUsers) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.getUser();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateCurrentUser();
            searchPageSelectedProperty.set(true);
            postFetchUsers.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void registerUser(Runnable postFetchUsers) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.registerUser();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateErrorMessage();
            postFetchUsers.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
