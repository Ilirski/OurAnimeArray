package com.animearray.ouranimearray.loginregister;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.DAOs.User;

import java.sql.SQLException;
import java.util.List;

public class LoginRegisterPageInteractor {
    private final LoginRegisterPageModel viewModel;
    private final DatabaseFetcher databaseFetcher;
    private User currentUser;
    public LoginRegisterPageInteractor(LoginRegisterPageModel viewModel) {
        this.viewModel = viewModel;
        this.databaseFetcher = new DatabaseFetcher();
    }

    public void registerUser() throws SQLException {
        databaseFetcher.createUser(viewModel.getUsernameRegister(), viewModel.getPasswordRegister());
    }

    public void updateRegisterErrorMessage(String errorMessage) {
        viewModel.registerErrorMessageProperty().set(errorMessage);
    }

    public void updateLoginErrorMessage(String errorMessage) {
        viewModel.loginErrorMessageProperty().set(errorMessage);
    }

    public void getUser() {
        currentUser = databaseFetcher.getUser(viewModel.getUsernameLogin(), viewModel.getPasswordLogin()).orElse(null);
    }

    public void updateCurrentUser() {
        viewModel.setCurrentUser(currentUser);
    }


    public void getUsers() {
        List<User> users = databaseFetcher.getUsers();
        System.out.println(users);
    }
}
