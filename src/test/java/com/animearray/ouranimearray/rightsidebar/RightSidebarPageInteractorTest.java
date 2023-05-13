package com.animearray.ouranimearray.rightsidebar;

import com.animearray.ouranimearray.model.DatabaseFetcher;
import com.animearray.ouranimearray.widgets.DAOs.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit5.ApplicationTest;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RightSidebarPageInteractorTest extends ApplicationTest {

    RightSidebarPageModel model;
    RightSidebarPageInteractor interactor;
    DatabaseFetcher databaseFetcher = new DatabaseFetcher();

    @Override
    public void start(Stage stage) throws Exception {
        // Note: Test won't run unless you add
        // --add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED
        // to the VM options in the Run/Debug Configurations.
        // See https://github.com/TestFX/TestFX/issues/638
        super.start(stage);
        model = new RightSidebarPageModel();
        interactor = new RightSidebarPageInteractor(model);
        User user = databaseFetcher.getUser("Horse", "Horse123").orElse(null);
        assertNotNull(user);
        model.setCurrentUser(user);
    }

    @Test
    void setEpisodeWatched() {
        Anime anime = databaseFetcher.searchAnime("FakeAnime", SortType.POPULARITY).stream().filter(a -> a.title().equals("FakeAnime")).findFirst().orElse(null);
        assertNotNull(anime);
        model.setAnime(anime);
        model.setUserAnimeData(new UserAnime(model.getCurrentUser().id(), anime.id(), "", 5, 5, WatchStatus.WATCHING));
        interactor.setEpisodeWatched();
        databaseFetcher.getUserAnimeData(anime.id(), model.getCurrentUser().id());
        assertEquals(5, model.getUserAnimeData().watchedEpisodes());
    }
    @Test
    void setAnimeWatchStatus() {
        Anime anime = databaseFetcher.searchAnime("FakeAnime", SortType.POPULARITY).stream().filter(a -> a.title().equals("FakeAnime")).findFirst().orElse(null);
        assertNotNull(anime);
        model.setAnime(anime);
        model.setUserWatchStatus(WatchStatus.WATCHING);
        model.setUserAnimeData(new UserAnime(model.getCurrentUser().id(), anime.id(), "", 5, 5, WatchStatus.WATCHING));
        databaseFetcher.getUserAnimeData(anime.id(), model.getCurrentUser().id());
        assertEquals(WatchStatus.WATCHING, model.getUserAnimeData().watchStatus());
    }

    @Order(4)
    @Test
    void setUserAnimeScore() {
        Anime anime = databaseFetcher.searchAnime("FakeAnime", SortType.POPULARITY).stream().filter(a -> a.title().equals("FakeAnime")).findFirst().orElse(null);
        assertNotNull(anime);
        model.setAnime(anime);
        model.setUserAnimeData(new UserAnime(model.getCurrentUser().id(), anime.id(), "", 5, 5, WatchStatus.WATCHING));
        interactor.setUserAnimeScore();
        databaseFetcher.getUserAnimeData(anime.id(), model.getCurrentUser().id());
        assertEquals(5, model.getUserAnimeData().score());
    }

    @Test
    void getGenres() {
        List<Genre> genres = databaseFetcher.getGenres();
        assertEquals("1", genres.stream().filter(genre -> genre.genre().equals("Action")).findFirst().get().id());
    }

    @Order(Integer.MAX_VALUE)
    @Test
    void deleteAnime() {
        DatabaseFetcher databaseFetcher = new DatabaseFetcher();
        List<Anime> animes = databaseFetcher.searchAnime("FakeAnime", SortType.POPULARITY);
        Anime anime = animes.stream().filter(a -> a.title().equals("FakeAnime")).findFirst().orElse(null);
        assertNotNull(anime);
        model.setAnime(anime);
        interactor.deleteAnime();
        animes = databaseFetcher.searchAnime("FakeAnime", SortType.POPULARITY);
        assertNull(animes.stream().filter(a -> a.title().equals("FakeAnime")).findFirst().orElse(null));
    }

    @Order(3)
    @Test
    void editAnime() {
        List<Anime> animes = databaseFetcher.searchAnime("FakeAnime", SortType.POPULARITY);
        System.out.println(animes);
        Anime anime = animes.stream().filter(a -> a.title().equals("FakeAnime")).findFirst().orElse(null);
        assertNotNull(anime);
        model.setAnimeToCreateOrModify(new AnimeToSave(anime.id(), "FakeAnime", "", 0, 0, "Edited", anime.genres(),  null, null));
        interactor.editAnime();
        animes = databaseFetcher.searchAnime("FakeAnime", SortType.POPULARITY);
        assertEquals("Edited", animes.stream().filter(a -> a.title().equals("FakeAnime")).findFirst().get().synopsis());
    }

    @Order(1)
    @Test
    void addAnime() {
        model.setAnimeToCreateOrModify(new AnimeToSave("9999999", "FakeAnime", "", 0, 0, "", Collections.singletonList(new Genre("1", "Action")),  null, null));
        interactor.saveAnime();
        List<Anime> animes = databaseFetcher.searchAnime("FakeAnime", SortType.POPULARITY);
        assertEquals("FakeAnime", animes.stream().filter(anime -> anime.title().contains("FakeAnime")).findFirst().get().title());
        System.out.println(animes);
    }
}