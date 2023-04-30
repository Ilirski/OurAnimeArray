package com.animearray.ouranimearray.widgets.DAOs;

public enum WatchStatus {
    WATCHING,
    COMPLETED,
    ON_HOLD,
    DROPPED,
    PLAN_TO_WATCH;

    @Override
    public String toString() {
        return switch (this) {
            case WATCHING -> "Watching";
            case COMPLETED -> "Completed";
            case ON_HOLD -> "On Hold";
            case DROPPED -> "Dropped";
            case PLAN_TO_WATCH -> "Plan to Watch";
        };
    }

    public static WatchStatus abbreviationToStatus(String abbreviation) {
        return switch (abbreviation) {
            case "W" -> WATCHING;
            case "C" -> COMPLETED;
            case "O" -> ON_HOLD;
            case "D" -> DROPPED;
            case "P" -> PLAN_TO_WATCH;
            default -> null;
        };
    }
}
