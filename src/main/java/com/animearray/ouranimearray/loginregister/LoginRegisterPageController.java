package com.animearray.ouranimearray.loginregister;

import com.animearray.ouranimearray.widgets.DAOs.User;
import com.animearray.ouranimearray.widgets.ControllerFX;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.sql.SQLException;

public class LoginRegisterPageController implements ControllerFX {
    private final LoginRegisterPageInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final LoginRegisterPageModel model = new LoginRegisterPageModel();
    private final BooleanProperty searchPageSelectedProperty;

    public LoginRegisterPageController(ObjectProperty<User> currentUserProperty, BooleanProperty searchPageSelectedProperty) {
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
            if (!model.isLoggedIn()) {
                interactor.updateLoginErrorMessage("ERROR: Username or password is incorrect.");
            } else {
                // clear the login fields
                model.setUsernameLogin("");
                model.setPasswordLogin("");
                interactor.updateLoginErrorMessage("");
            }
            postFetchUsers.run();
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    private void registerUser(Runnable postFetchUsers) {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                interactor.registerUser();
                return null;
            }
        };
        fetchTask.setOnSucceeded(event -> {
            interactor.updateRegisterErrorMessage("Account successfully created!");
            model.setUsernameRegister("");
            model.setPasswordRegister("");
            interactor.updateRegisterErrorMessage("");
            postFetchUsers.run();
        });
        fetchTask.setOnFailed(event -> {
            // Casting to SQLException to get the error code
            SQLException sqlException = (SQLException) fetchTask.getException();
            if (sqlException.getErrorCode() == 19) {
                interactor.updateRegisterErrorMessage("ERROR: Username already taken.");
            }
        });
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    @Override
    public Region getViewBuilder() {
        return viewBuilder.build();
    }
}
