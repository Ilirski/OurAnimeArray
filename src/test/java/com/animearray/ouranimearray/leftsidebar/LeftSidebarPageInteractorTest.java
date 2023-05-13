package com.animearray.ouranimearray.leftsidebar;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.DAOs.AccountType;
import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.AnimeList;
import com.animearray.ouranimearray.widgets.DAOs.User;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeftSidebarPageInteractorTest extends ApplicationTest {
    LeftSidebarPageModel model = new LeftSidebarPageModel();
    LeftSidebarPageInteractor interactor = new LeftSidebarPageInteractor(model);

    @Override
    public void start(Stage stage) throws Exception {
        // Note: Test won't run unless you add
        // --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED
        // to the VM options in the Run/Debug Configurations.
        // See https://github.com/TestFX/TestFX/issues/638
        super.start(stage);
    }

    @BeforeAll
    static void setUp() {
        // Assume that Boccher is in the database.
        DatabaseFetcher databaseFetcher = new DatabaseFetcher();
        try {
            databaseFetcher.createUser("Horse", "Horse123");
        } catch (SQLException e) {
            assertEquals(19, e.getErrorCode());
        }
    }

    @Test
    void loadUserAnimeLists() {
        model.setCurrentUser(new User("2", "Horse", "Horse123", AccountType.USER));
        interactor.loadUserAnimeLists();
        interactor.updateAnimeList();
        assertEquals("1", model.getUserAnimeLists().get(0).id());
    }

    @Test
    void addAnimeToList() {
        DatabaseFetcher databaseFetcher = new DatabaseFetcher();
        model.setCurrentUser(new User("2", "Horse", "Horse123", AccountType.USER));
        model.setAnimeToAdd(new Anime("19", "Bocchi", null, 0, 0, null, null, null, null, 0));
        model.setAnimeListToAddTo(new AnimeList("1", "2", "Test", "Test", null));
        try {
            interactor.addAnimeToList();
        } catch (SQLException ignored) {}
        finally {
            List<Anime> animes = databaseFetcher.getAnimeFromList("1");
            assertEquals("19", animes.stream().filter(anime -> anime.id().equals("19")).findFirst().get().id());
        }
    }

    @Test
    void createNewAnimeList() {
        model.setCurrentUser(new User("2", "Horse", "Horse123", AccountType.USER));
        model.setAnimeListToAddTo(new AnimeList("1", "2", "Test", "Test", null));
        try {
            interactor.createNewAnimeList();
        } catch (SQLException e) {
            assertEquals(19, e.getErrorCode());
        }
    }
}