package com.animearray.ouranimearray.widgets.DAOs;

//public record User(String id, String username, String password) {}

import com.animearray.ouranimearray.widgets.DAOs.AccountType;

public record User(String id, String username, String password, AccountType accountType) {
}
