package com.animearray.ouranimearray.widgets;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.Genre;
import com.animearray.ouranimearray.widgets.DAOs.Score;
import com.tobiasdiez.easybind.EasyBind;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class AnimeProperty extends ObjectPropertyBase<Anime> {

    private final StringProperty id;
    private final StringProperty title;
    private final ObjectProperty<Image> image;
    private final ObjectProperty<Integer> episodes;
    private final DoubleProperty score;
    private final StringProperty synopsis;
    private final ListProperty<Genre> genres;
    private final ObjectProperty<LocalDate> airedStartDate;
    private final ObjectProperty<LocalDate> airedEndDate;
    private final String imageUnavailableUrl = "https://media.cheggcdn.com/media/d53/d535ce9a-4535-4e56-bd8e-81300a25a4f7/php4KwLCz";

    public AnimeProperty(Anime anime) {
        set(anime);

        this.id = new SimpleStringProperty(anime != null ? anime.id() : "");
        this.title = new SimpleStringProperty(anime != null ? anime.title() : "");
        this.image = new SimpleObjectProperty<>(anime != null ? anime.image() : new Image(imageUnavailableUrl));
        this.episodes = new SimpleObjectProperty<>(anime != null ? anime.episodes() : 0);
        this.score = new SimpleDoubleProperty(anime != null ? anime.score() : 0.0);
        this.synopsis = new SimpleStringProperty(anime != null ? anime.synopsis() : "");
        this.genres = new SimpleListProperty<>(anime != null ? FXCollections.observableArrayList(anime.genres()) : FXCollections.singletonObservableList(new Genre(null, null)));
        this.airedStartDate = new SimpleObjectProperty<>(anime != null ? anime.airedStartDate() : null);
        this.airedEndDate = new SimpleObjectProperty<>(anime != null ? anime.airedEndDate() : null);

        // Add a listener such that when the current AnimeProperty changes, its properties is updated
        this.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.id.set(newValue.id());
                this.title.set(newValue.title());
                this.image.set(newValue.image());
                this.episodes.set(newValue.episodes());
                this.score.set(newValue.score());
                this.synopsis.set(newValue.synopsis());
                this.genres.set(FXCollections.observableArrayList(newValue.genres()));
                this.airedStartDate.set(newValue.airedStartDate());
                this.airedEndDate.set(newValue.airedEndDate());
            } else {
                this.id.set("");
                this.title.set("");
                this.image.set(new Image(imageUnavailableUrl));
                this.episodes.set(0);
                this.score.set(0.0);
                this.synopsis.set("");
                this.genres.set(FXCollections.singletonObservableList(new Genre(null, null)));
                this.airedStartDate.set(null);
                this.airedEndDate.set(null);
            }
        });

    }

        private void setDefaultValues() {
            this.id.set("ID Not Found");
            this.title.set("Title Not Found");
            this.image.set(new Image(imageUnavailableUrl));
            this.episodes.set(0);
            this.score.set(0.0);
            this.synopsis.set("Synopsis Not Found");
            this.genres.set(FXCollections.singletonObservableList(new Genre(null, null)));
            this.airedStartDate.set(null);
            this.airedEndDate.set(null);
        }


    public AnimeProperty() {
        this(null);
    }

    @Override
    public void set(Anime newValue) {
        super.set(newValue);
    }

    @Override
    protected void invalidated() {
        super.invalidated();
    }

    @Override
    public Anime get() {
        return super.get();
    }
    public StringProperty idBinding() {
        return id;
    }

    public StringProperty titleBinding() {
        return title;
    }

    public ObjectProperty<Image> imageBinding() {
        return image;
    }

    public ObjectProperty<Integer> episodesBinding() {
        return episodes;
    }

    public DoubleProperty scoreBinding() {
        return score;
    }

    public StringProperty synopsisBinding() {
        return synopsis;
    }

    public ListProperty<Genre> genresBinding() {
        return genres;
    }

    public ObjectProperty<LocalDate> airedStartDateBinding() {
        return airedStartDate;
    }

    public ObjectProperty<LocalDate> airedEndDateBinding() {
        return airedEndDate;
    }

    @Override
    public Object getBean() {
        return this;
    }

    @Override
    public String getName() {
        return "AnimeProperty";
    }

    @Override
    public String toString() {
        return "AnimeProperty{" +
                "imageUnavailableUrl='" + imageUnavailableUrl + '\'' +
                ", id=" + id +
                ", title=" + title +
                ", image=" + image +
                ", episodes=" + episodes +
                ", score=" + score +
                ", synopsis=" + synopsis +
                ", genres=" + genres +
                '}';
    }
}
