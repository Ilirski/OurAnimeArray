package com.animearray.ouranimearray.widgets.DAOs;

import java.util.List;
public record AnimeDAO(String id, String title, String imageURL, int episodes, double score, String synopsis, List<Genre> genres) {
}
