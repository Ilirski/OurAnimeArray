package com.animearray.ouranimearray.loginregister;

import com.animearray.ouranimearray.search.SearchPageInteractor;
import com.animearray.ouranimearray.search.SearchPageModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRegisterPageInteractorTest {

    @Test
    void registerUser() {
    }

    @Test
    void updateRegisterErrorMessage() {
    }

    @Test
    void updateLoginErrorMessage() {
    }

    @Test
    void getUser() {
        var model = new LoginRegisterPageModel();
        var animeInteractor = new LoginRegisterPageInteractor(model);
        model.setUsernameLogin("Boccher");
        model.setPasswordLogin("Bocchi123");
        animeInteractor.getUser();
        animeInteractor.updateCurrentUser();
        assertEquals("1", model.getCurrentUser().id());
    }

    @Test
    void getUsers() {
    }
}