package com.animearray.ouranimearray.model;

import javafx.scene.image.Image;

public record Anime(String id, String title, Image image, int episodes, double score, String synopsis) {
}
