package com.animearray.ouranimearray.widgets;

public enum Score {
    // MyAnimeList user rating
    APPALLING(1),
    HORRIBLE(2),
    VERY_BAD(3),
    BAD(4),
    AVERAGE(5),
    FINE(6),
    GOOD(7),
    VERY_GOOD(8),
    GREAT(9),
    MASTERPIECE(10);

    private final int rating;

    Score(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public static Score getRating(int rating) {
        return switch (rating) {
            case 1 -> APPALLING;
            case 2 -> HORRIBLE;
            case 3 -> VERY_BAD;
            case 4 -> BAD;
            case 5 -> AVERAGE;
            case 6 -> FINE;
            case 7 -> GOOD;
            case 8 -> VERY_GOOD;
            case 9 -> GREAT;
            case 10 -> MASTERPIECE;
            default -> null;
        };
    }

    public static Score getRating(String rating) {
        return switch (rating) {
            case "Appalling" -> APPALLING;
            case "Horrible" -> HORRIBLE;
            case "Very Bad" -> VERY_BAD;
            case "Bad" -> BAD;
            case "Average" -> AVERAGE;
            case "Fine" -> FINE;
            case "Good" -> GOOD;
            case "Very Good" -> VERY_GOOD;
            case "Great" -> GREAT;
            case "Masterpiece" -> MASTERPIECE;
            default -> null;
        };
    }

    public static String getRatingString(int rating) {
        return switch (rating) {
            case 1 -> "Appalling";
            case 2 -> "Horrible";
            case 3 -> "Very Bad";
            case 4 -> "Bad";
            case 5 -> "Average";
            case 6 -> "Fine";
            case 7 -> "Good";
            case 8 -> "Very Good";
            case 9 -> "Great";
            case 10 -> "Masterpiece";
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case APPALLING -> "(1) Appalling";
            case HORRIBLE -> "(2) Horrible";
            case VERY_BAD -> "(3) Very Bad";
            case BAD -> "(4) Bad";
            case AVERAGE -> "(5) Average";
            case FINE -> "(6) Fine";
            case GOOD -> "(7) Good";
            case VERY_GOOD -> "(8) Very Good";
            case GREAT -> "(9) Great";
            case MASTERPIECE -> "(10) Masterpiece";
        };
    }
}

