package com.animearray.ouranimearray.widgets;

import com.animearray.ouranimearray.widgets.DAOs.Score;
import com.animearray.ouranimearray.widgets.DAOs.UserAnime;
import com.animearray.ouranimearray.widgets.DAOs.WatchStatus;
import javafx.beans.property.*;

public class UserAnimeProperty extends ObjectPropertyBase<UserAnime> {
    private final StringProperty userId;
    private final StringProperty animeId;
    private final StringProperty review;
    private final ObjectProperty<Score> score;
    // ObjectProperty<Integer> because MFXSpinnerModel requires ObjectProperty,
    // however any bindings with .asObject() will actually be garbage collected
    private final ObjectProperty<Integer> watchedEpisodes;
    private final ObjectProperty<WatchStatus> status;

    public UserAnimeProperty(UserAnime userAnime) {
        set(userAnime);

        this.userId = new SimpleStringProperty(userAnime != null ? userAnime.userId() : "");
        this.animeId = new SimpleStringProperty(userAnime != null ? userAnime.animeId() : "");
        this.review = new SimpleStringProperty(userAnime != null ? userAnime.review() : "");
        this.score = new SimpleObjectProperty<>(userAnime != null ? Score.getRating(userAnime.score()) : null);
        this.watchedEpisodes = new SimpleObjectProperty<>(userAnime != null ? userAnime.watchedEpisodes() : 0);
        this.status = new SimpleObjectProperty<>(userAnime != null ? userAnime.watchStatus() : null);

        // Add a listener such that when the current AnimeProperty changes, its properties is updated
        this.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.userId.set(newValue.userId());
                this.animeId.set(newValue.animeId());
                this.review.set(newValue.review());
                this.score.set(Score.getRating(newValue.score()));
                this.watchedEpisodes.set(newValue.watchedEpisodes());
                this.status.set(newValue.watchStatus());
            } else {
                this.userId.set("");
                this.animeId.set("");
                this.review.set("");
                this.score.set(null);
                this.watchedEpisodes.set(0);
                this.status.set(null);
            }
        });
    }

    public UserAnimeProperty() {
        this(null);
    }

    public String getUserId() {
        return userId.get();
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public String getAnimeId() {
        return animeId.get();
    }

    public StringProperty animeIdProperty() {
        return animeId;
    }

    public String getReview() {
        return review.get();
    }

    public StringProperty reviewProperty() {
        return review;
    }

    public Score getScore() {
        return score.get();
    }

    public ObjectProperty<Score> scoreProperty() {
        return score;
    }

    public int getWatchedEpisodes() {
        return watchedEpisodes.get();
    }

    public ObjectProperty<Integer> watchedEpisodesProperty() {
        return watchedEpisodes;
    }

    public WatchStatus getStatus() {
        return status.get();
    }

    public ObjectProperty<WatchStatus> statusProperty() {
        return status;
    }

    @Override
    public String toString() {
        return "UserAnimeProperty{" +
                "userId=" + userId +
                ", animeId=" + animeId +
                ", review=" + review +
                ", score=" + score +
                ", watchedEpisodes=" + watchedEpisodes +
                ", status=" + status +
                '}';
    }

    @Override
    public Object getBean() {
        return this;
    }

    @Override
    public String getName() {
        return "UserAnimeProperty";
    }
}
