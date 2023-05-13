package com.animearray.ouranimearray.widgets.DAOs;

public enum SortType {
    POPULARITY("Popularity"),
    SCORE("Score"),
    TITLE("Title"),
    NEWEST("Newly Added"),
    RECENT("Recently Aired");

    private final String sortType;

    SortType(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return sortType;
    }
}
