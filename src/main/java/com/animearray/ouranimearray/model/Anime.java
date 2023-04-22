package com.animearray.ouranimearray.model;

import javafx.scene.image.Image;

import java.util.List;

public record Anime(String id, String title, Image image, int episodes, double score, String synopsis, List<String> genres) {}
