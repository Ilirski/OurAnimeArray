package com.animearray.ouranimearray.widgets.DAOs;

import java.time.LocalDateTime;

public record AnimeList(String id, String userId, String name, String description, LocalDateTime createdAt) {
}
