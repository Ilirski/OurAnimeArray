package com.animearray.ouranimearray.model;

import javafx.scene.image.Image;

// Find a good name for this
public record Anime(String id, String title, Image image, int episodes, double score, String synopsis) {
}
