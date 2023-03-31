package com.animearray.ouranimearray.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DatabaseFetcher {
    // Refer to https://github.com/220118-Pega/Quezon-Aldrick-Code/blob/f908d976c4d73cc98a94df137b323bc00730d2de/Zero/ProZero/src/zero/userdata/RequestDAO.java
    private static final String url = "jdbc:sqlite:C:\\Users\\User\\IdeaProjects\\OurAnimeArray\\src\\main\\java\\com\\animearray\\ouranimearray\\model\\anime.db";
    String ERROR_IMAGE_URL = "https://media.cheggcdn.com/media/d53/d535ce9a-4535-4e56-bd8e-81300a25a4f7/php4KwLCz";

    public List<User> getUsers() {

        List<User> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, * FROM user LIMIT 30")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                users.add(new User(id, username, password));
            }

            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(" i should not be calld");
        return List.of(new User(0, "", ""));
    }


    public List<Anime> getListOfAnimes() {

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT rowid, * FROM anime ORDER BY score DESC LIMIT 30")) {

            return getAnime(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return List.of(new Anime("0", "", new Image(ERROR_IMAGE_URL), 0, 0f, ""));
    }

    public List<Anime> searchAnime(String query) {
        String sql = "SELECT rowid, * FROM anime WHERE title LIKE ? ORDER BY score DESC LIMIT 30";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + query + "%"); // add wildcards for fuzzy search

            ResultSet rs = stmt.executeQuery();

            return getAnime(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("I shouldn't be called");
        return List.of(new Anime("0", "", new Image(ERROR_IMAGE_URL), 0, 0f, ""));
    }

    private List<Anime> getAnime(ResultSet rs) throws SQLException {
        List<CompletableFuture<Anime>> animeFutures = new ArrayList<>(); // list of future anime objects

        while (rs.next()) {
            String id = rs.getString("rowid");
            String title = rs.getString("title");
            String imageUrl = rs.getString("link");
            int episodes = rs.getInt("episodes");
            double score = rs.getDouble("score");
            String synopsis = rs.getString("synopsis");

            CompletableFuture<Anime> animeFuture = CompletableFuture.supplyAsync(() -> turnUrlToImage(imageUrl))
                    .completeOnTimeout(turnUrlToImage(ERROR_IMAGE_URL), 5, TimeUnit.SECONDS)
                    .thenApply(image -> new Anime(id, title, image, episodes, score, synopsis));

            animeFutures.add(animeFuture);
        }

        return animeFutures.stream()
                .map(CompletableFuture::join) // wait for all futures to complete and collect their results into a list
                .collect(Collectors.toList());
    }


    public Image turnUrlToImage(String url) {
        if (url == null) {
            return new Image(ERROR_IMAGE_URL);
        }
        return new Image(url);
    }
}
