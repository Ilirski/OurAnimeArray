package com.animearray.ouranimearray.loginregister;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoginRegisterPageInteractor {
    private final LoginRegisterPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    private User currentUser;
    private String errorMessage;
    public LoginRegisterPageInteractor(LoginRegisterPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }

    public void registerUser() {
        try {
            databaseFetcher.createUser(viewModel.getUsernameRegister(), viewModel.getPasswordRegister());
            errorMessage = "Account successfully created!"; // No errors
        } catch (SQLException e) {
            var errorCode = e.getErrorCode();

            if (errorCode == 19) {
                errorMessage = "ERROR: Username is already taken";
            }
        }
    }

    public void updateErrorMessage() {
        viewModel.registerErrorMessageProperty().set(errorMessage);
    }

    public void getUser() {
        currentUser = databaseFetcher.getUser(viewModel.getUsernameLogin(), viewModel.getPasswordLogin()).orElse(null);
        // If userid is present, set it. Else, do nothing.
//        user.ifPresentOrElse(viewModel::setCurrentUserID, () -> {});
    }

    public void updateCurrentUser() {
        viewModel.setCurrentUser(currentUser);
    }


    public void getUsers() {
        List<User> users = databaseFetcher.getUsers();
        System.out.println(users);
    }
}
