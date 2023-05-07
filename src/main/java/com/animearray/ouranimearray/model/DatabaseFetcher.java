package com.animearray.ouranimearray.model;

import com.animearray.ouranimearray.widgets.DAOs.*;
import javafx.scene.image.Image;

import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseFetcher {
    // Refer to https://github.com/220118-Pega/Quezon-Aldrick-Code/blob/f908d976c4d73cc98a94df137b323bc00730d2de/Zero/ProZero/src/zero/userdata/RequestDAO.java
    // IMPORTANT: Explicit return is needed for all methods, else Task won't recognize it to be completed
    private static final String url = "jdbc:sqlite:C:\\Users\\User\\IdeaProjects\\OurAnimeArray\\src\\main\\java\\com\\animearray\\ouranimearray\\model\\anime.db";
    String ERROR_IMAGE_URL = "https://media.cheggcdn.com/media/d53/d535ce9a-4535-4e56-bd8e-81300a25a4f7/php4KwLCz";

    public void createUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO user(username, password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        }

        return;
    }

    public Optional<User> getUser(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String user = rs.getString("username");
                String pass = rs.getString("password");
                String accountType = rs.getString("account_type");
                return Optional.of(new User(id, user, pass, AccountType.valueOf(accountType)));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public List<AnimeList> getAnimeLists(String userId) {
        String sql = """
                SELECT *
                FROM list
                WHERE user_id = ?
                """;

        List<AnimeList> animeLists = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String userID = rs.getString("user_id");
                String name = rs.getString("name");
                String episodes = rs.getString("description");
                String createdAt = rs.getString("created_at");
                LocalDateTime createdAtDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                animeLists.add(new AnimeList(id, userID, name, episodes, createdAtDateTime));
            }
            return animeLists;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeException e) {
            System.out.println("Error parsing SQLite DateTime");
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Problem with SQL fetching");
    }

    public void setAnimeWatchStatus(String animeId, String userId, String status) {
        String sql = """
                INSERT INTO user_anime (user_id, anime_id, status)
                VALUES (?, ?, ?)
                ON CONFLICT (user_id, anime_id) DO
                UPDATE SET status = excluded.status
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, animeId);
            ps.setString(3, status);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }

    public List<Anime> searchAnime(String searchQuery, SortType sortType) {
        String sql = """
                SELECT anime.*, GROUP_CONCAT(genre.id || ' ' || genre.genre) AS genres
                FROM anime
                LEFT JOIN anime_genre ON anime.id = anime_genre.anime_id
                LEFT JOIN genre ON genre.id = anime_genre.genre_id
                WHERE anime.title LIKE ?
                GROUP BY anime.id
                ORDER BY %s
                LIMIT 50
                """;

        // Order by switch (https://stackoverflow.com/a/2857417/17771525)
        String orderBy = switch (sortType) {
            case POPULARITY -> "anime.members DESC";
            case SCORE -> "anime.score DESC";
            case TITLE -> "anime.title ASC";
            case NEWEST -> "anime.created_at DESC";
        };

        sql = String.format(sql, orderBy);

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + searchQuery + "%"); // add wildcards for fuzzy search
            ResultSet rs = ps.executeQuery();
            System.out.println("Executing query...");

            return makeAnime(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Problem with SQL fetching");
    }

    private List<Anime> makeAnime(ResultSet rs) throws SQLException {
        List<CompletableFuture<Anime>> animeFutures = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString("id");
            String title = rs.getString("title");
            String imageUrl = rs.getString("link");
            int episodes = rs.getInt("episodes");
            double score = rs.getDouble("score");
            String synopsis = rs.getString("synopsis");
            String genres = rs.getString("genres");

            List<Genre> gen;

            if (genres == null) {
                gen = new ArrayList<>();
            } else {
                // Split genres into a list of genres
                gen = Stream.of(genres.split(",")).map(s -> {
                    String[] split = s.split(" ");
                    String genreId = split[0];
                    String genreName = split[1];
                    return new Genre(genreId, genreName);
                }).toList();
            }

            // After 5 seconds, if the image has not been loaded, return a default image
            CompletableFuture<Anime> animeFuture = CompletableFuture.supplyAsync(() -> turnUrlToImage(imageUrl))
                    .completeOnTimeout(turnUrlToImage(ERROR_IMAGE_URL), 5, TimeUnit.SECONDS)
                    .thenApply(image -> new Anime(id, title, image, episodes, score, synopsis, gen));

            animeFutures.add(animeFuture);
        }

        return animeFutures.stream()
                .map(CompletableFuture::join) // wait for all futures to complete and collect their results into a list
                .collect(Collectors.toList());
    }

    public UserAnime getUserAnimeData(String animeId, String userId) {
        String sql = """
                SELECT *
                FROM user_anime
                WHERE user_id = ? AND anime_id = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, animeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String review = rs.getString("review");
                int score = rs.getInt("score");
                int watchedEpisodes = rs.getInt("watched_episodes");
                String status = rs.getString("status");

                return new UserAnime(userId, animeId, review, score, watchedEpisodes, WatchStatus.abbreviationToStatus(status));
            } else {

                // User has not interacted with this anime yet
                return new UserAnime(userId, animeId, null, 0, 0, null);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Problem with SQL fetching");
    }

    public Image turnUrlToImage(String url) {
        try {
            // If the url is empty, return a default image
            if (url == null || url.isBlank()) {
                return new Image(ERROR_IMAGE_URL);
            }

            // Check if the url is valid (https://stackoverflow.com/a/5719282/17771525)
            return new Image(url);
        } catch (IllegalArgumentException e) {
            return new Image(ERROR_IMAGE_URL);
        }
    }

    public void addAnimeToList(String animeToAddId, String animeListToAddToId) {
        String sql = """
                INSERT INTO list_anime (list_id, anime_id)
                VALUES (?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, animeListToAddToId);
            ps.setString(2, animeToAddId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }

    public void setEpisodeWatched(String animeId, String userId, int episode) {
        String sql = """
                INSERT INTO user_anime (user_id, anime_id, watched_episodes)
                VALUES (?, ?, ?)
                ON CONFLICT (user_id, anime_id) DO
                UPDATE SET watched_episodes = excluded.watched_episodes
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, animeId);
            ps.setInt(3, episode);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }

    public void setUserAnimeScore(String animeId, String userId, Integer score) {
        String sql = """
                INSERT INTO user_anime (user_id, anime_id, score)
                VALUES (?, ?, ?)
                ON CONFLICT (user_id, anime_id) DO
                UPDATE SET score = excluded.score
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, animeId);
            ps.setInt(3, score);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return;
    }

    public List<Anime> getAnimeFromList(String listId) {
        String sql = """
                SELECT anime.*, GROUP_CONCAT(genre.id || ' ' || genre.genre) AS genres
                FROM anime
                JOIN list_anime ON anime.id = list_anime.anime_id AND list_anime.list_id = ?
                JOIN anime_genre ON anime.id = anime_genre.anime_id
                JOIN genre ON genre.id = anime_genre.genre_id
                GROUP BY anime.id
                ORDER BY score DESC
                LIMIT 50
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, listId);

            ResultSet rs = ps.executeQuery();
            System.out.println("Executing query...");

            return makeAnime(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Problem with SQL fetching");
    }

    public UserList getUserListDetails(String listId) {
        String sql = """
                SELECT *
                FROM list
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, listId);

            ResultSet rs = ps.executeQuery();
            System.out.println("Executing query...");

            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                String userId = rs.getString("user_id");
                String createdAt = rs.getString("created_at");
                LocalDateTime createdDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                return new UserList(listId, userId, name, description, createdDateTime);
            } else {
                throw new RuntimeException("List not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Problem with SQL fetching");
    }

    public void deleteAnimeList(String listId) {
        String sql = """
                DELETE FROM list
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, listId);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return;
    }

    public void createNewAnimeList(String userId, String name) throws SQLException {
        String sql = """
                INSERT INTO list (user_id, name)
                VALUES (?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, name);

            ps.executeUpdate();
        }

        return;
    }

    public List<Genre> getGenres() {
        String sql = """
                SELECT *
                FROM genre
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            System.out.println("Executing query...");

            List<Genre> genres = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("genre");

                genres.add(new Genre(id, name));
            }

            return genres;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Problem with SQL fetching");

    }

    public void removeAnimeFromList(String animeId, String listId) {
        String sql = """
                DELETE FROM list_anime
                WHERE list_id = ? AND anime_id = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, listId);
            ps.setString(2, animeId);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }

    public void editUserAnimeList(String listId, String name, String description) {
        String sql = """
                UPDATE list
                SET name = ?, description = ?
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, listId);

            System.out.println(listId + " " + name + " " + description);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }

    public void editAnime(AnimeToSave animeToEdit) {
        String animeSql = """
                UPDATE anime
                SET title = ?, synopsis = ?, score = ?, link = ?, episodes = ?
                WHERE id = ?
                """;

        String deleteGenresSQL = """
                DELETE FROM anime_genre WHERE anime_id = ?
                """;

        String insertGenresSQL = """
                INSERT INTO anime_genre (anime_id, genre_id)
                VALUES (?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(animeSql);
             PreparedStatement ps2 = conn.prepareStatement(deleteGenresSQL);
             PreparedStatement ps3 = conn.prepareStatement(insertGenresSQL)) {

            // Edit anime
            ps.setString(1, animeToEdit.title());
            ps.setString(2, animeToEdit.synopsis());
            ps.setDouble(3, animeToEdit.score());
            ps.setString(4, animeToEdit.imageURL());
            ps.setInt(5, animeToEdit.episodes());
            ps.setString(6, animeToEdit.id());
            ps.executeUpdate();

            // Delete old genres
            ps2.setString(1, animeToEdit.id());
            ps2.executeUpdate();

            // Insert new genres
            for (Genre genre : animeToEdit.genres()) {
                ps3.setString(1, animeToEdit.id());
                ps3.setString(2, genre.id());
                ps3.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }

    public void deleteAnime(String id) {
        String sql = """
                DELETE FROM anime
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, id);

                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return;
            }

        return;
    }

    public void addAnime(AnimeToSave animeToSave) {
        String animeSql = """
                INSERT INTO anime (title, synopsis, score, link, episodes)
                VALUES (?, ?, ?, ?, ?)
                """;

        String getAnimeTitleSql = """
                SELECT id
                FROM anime
                WHERE title = ?
                """;

        String insertGenresSQL = """
                INSERT INTO anime_genre (anime_id, genre_id)
                VALUES (?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(animeSql);
             Statement stmt = conn.createStatement();
             PreparedStatement ps2 = conn.prepareStatement(insertGenresSQL)) {

            // Add anime
            ps.setString(1, animeToSave.title());
            ps.setString(2, animeToSave.synopsis());
            ps.setDouble(3, animeToSave.score());
            ps.setString(4, animeToSave.imageURL());
            ps.setInt(5, animeToSave.episodes());
            ps.executeUpdate();

            // Get anime id
            ResultSet rs = stmt.executeQuery(getAnimeTitleSql);
            String animeId;
            if (rs.next()) {
                animeId = rs.getString("id");
            } else {
                throw new RuntimeException("Problem with SQL fetching");
            }

            // Insert new genres
            for (Genre genre : animeToSave.genres()) {
                ps2.setString(1, animeId);
                ps2.setString(2, genre.id());
                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        return;
    }
}
