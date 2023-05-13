package com.animearray.ouranimearray.widgets.DAOs;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.List;

public record Anime(String id, String title, Image image, int episodes, double score, String synopsis,
                    List<Genre> genres, LocalDate airedStartDate, LocalDate airedEndDate, int members) {

    public Anime() {
        this("Not set", "", null, 0, 0, "", List.of(), null, null, 0);
    }
}
