package com.animearray.ouranimearray.loginregister;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.search.SearchPageInteractor;
import com.animearray.ouranimearray.search.SearchPageModel;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginRegisterPageInteractorTest {
    LoginRegisterPageModel model = new LoginRegisterPageModel();
    LoginRegisterPageInteractor interactor = new LoginRegisterPageInteractor(model);

    @Test
    void registerUser() {
        // Assume that Boccher is in the database.
        model.setUsernameRegister("Boccher");
        model.setPasswordRegister("Bocchi123");
        try {
            interactor.registerUser();
        } catch (SQLException e) {
            assertEquals(19, e.getErrorCode());
        }
    }

    @Test
    void getUser() {
        model.setUsernameLogin("Boccher");
        model.setPasswordLogin("Bocchi123");
        interactor.getUser();
        interactor.updateCurrentUser();
        assertEquals("1", model.getCurrentUser().id());
    }
}