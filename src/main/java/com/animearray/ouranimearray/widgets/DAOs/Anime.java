package com.animearray.ouranimearray.widgets.DAOs;

import javafx.scene.image.Image;

import java.util.List;

public record Anime(String id, String title, Image image, int episodes, double score, String synopsis, List<Genre> genres) {

    public Anime() {
        this("Not set", "", null, 0, 0, "", List.of());
    }
}
