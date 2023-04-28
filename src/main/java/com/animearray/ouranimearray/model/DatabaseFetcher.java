package com.animearray.ouranimearray.model;

import com.animearray.ouranimearray.widgets.*;
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

public class DatabaseFetcher {
    // Refer to https://github.com/220118-Pega/Quezon-Aldrick-Code/blob/f908d976c4d73cc98a94df137b323bc00730d2de/Zero/ProZero/src/zero/userdata/RequestDAO.java
    // IMPORTANT: Explicit return is needed for all methods, else Task won't recognize it to be completed
    private static final String url = "jdbc:sqlite:C:\\Users\\User\\IdeaProjects\\OurAnimeArray\\src\\main\\java\\com\\animearray\\ouranimearray\\model\\anime.db";
    String ERROR_IMAGE_URL = "https://media.cheggcdn.com/media/d53/d535ce9a-4535-4e56-bd8e-81300a25a4f7/php4KwLCz";

    public void createUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO user(username, password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql);) {

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

    public List<User> getUsers() {

        List<User> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM user LIMIT 30")) {
            while (rs.next()) {
                String id = rs.getString("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String accountType = rs.getString("account_type");

                users.add(new User(id, username, password, AccountType.valueOf(accountType)));
            }

            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Problem with SQL fetching");
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
            System.out.println(animeLists);
            return animeLists;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeException e) {
            System.out.println("Error parsing SQLite DateTime");
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Problem with SQL fetching");
    }

    public Optional<WatchStatus> getAnimeWatchStatus(String animeId, String userId) {
        String sql = """
                SELECT status
                FROM user_anime
                WHERE user_id = ? AND anime_id = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, animeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                var status = rs.getString("status");
                // Map one letter status to WatchStatus Enum
                switch (status) {
                    case "W" -> {
                        return Optional.of(WatchStatus.WATCHING);
                    }
                    case "C" -> {
                        return Optional.of(WatchStatus.COMPLETED);
                    }
                    case "O" -> {
                        return Optional.of(WatchStatus.ON_HOLD);
                    }
                    case "P" -> {
                        return Optional.of(WatchStatus.PLAN_TO_WATCH);
                    }
                    case "D" -> {
                        return Optional.of(WatchStatus.DROPPED);
                    }
                }
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
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
        }

        return;
    }

    public List<Anime> searchAnime(String query) {
        String sql = """
                SELECT *, GROUP_CONCAT(genre.genre) AS genres
                FROM anime
                JOIN anime_genre ON anime.id = anime_genre.anime_id AND anime.title LIKE ?
                JOIN genre ON genre.id = anime_genre.genre_id
                GROUP BY anime.id
                ORDER BY score DESC
                LIMIT 50
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%"); // add wildcards for fuzzy search

            ResultSet rs = ps.executeQuery();
            System.out.println("Executing query...");

            return getAnime(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Problem with SQL fetching");
    }

    private List<Anime> getAnime(ResultSet rs) throws SQLException {
        List<CompletableFuture<Anime>> animeFutures = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString("id");
            String title = rs.getString("title");
            String imageUrl = rs.getString("link");
            int episodes = rs.getInt("episodes");
            double score = rs.getDouble("score");
            String synopsis = rs.getString("synopsis");
            String genres = rs.getString("genres");

            // Split genres into a list
            List<String> gen = List.of(genres.split(","));

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

//                System.out.println("Review: " + review);
//                System.out.println("Score: " + score);
//                System.out.println("Watched Episodes: " + watchedEpisodes);
//                System.out.println("Status: " + status);

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
        // If the url is empty, return a default image
        if (url == null) {
            return new Image(ERROR_IMAGE_URL);
        }
        return new Image(url);
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
        }

        return;
    }

    public int getEpisodeWatched(String animeId, String userId) {
        String sql = """
                SELECT watched_episodes
                FROM user_anime
                WHERE user_id = ? AND anime_id = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, animeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int episode = rs.getInt("watched_episodes");
                return episode;
            } else {
                return 0;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Problem with SQL fetching");
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
            System.out.println(episode);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
}
