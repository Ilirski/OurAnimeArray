package com.animearray.ouranimearray.widgets.DAOs;

import java.time.LocalDateTime;

public record UserList(String listId, String userId, String name, String description, LocalDateTime createdDate) {
}
