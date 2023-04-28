package com.animearray.ouranimearray.widgets;

public record UserAnime(String userId, String animeId, String review, Integer score, Integer watchedEpisodes, WatchStatus watchStatus) {
}
