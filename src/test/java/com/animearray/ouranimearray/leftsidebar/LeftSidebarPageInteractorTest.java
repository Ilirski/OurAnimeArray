package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.widgets.DAOs.AccountType;
import com.animearray.ouranimearray.widgets.DAOs.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeftSidebarPageInteractorTest {

    @Test
    void loadAnimeList() {
        var model = new LeftSidebarPageModel();
        var interactor = new LeftSidebarPageInteractor(model);
        model.setCurrentUser(new User("1", "Boccher", "Bocchi123", AccountType.USER));
        interactor.loadAnimeList();
        interactor.updateAnimeList();
        assertEquals("1", model.getAnimeList().get(0).id());
    }
}