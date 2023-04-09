package com.animearray.ouranimearray.model;

import com.tobiasdiez.easybind.EasyBind;
import javafx.beans.binding.Binding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.util.Collections;
import java.util.List;

public class SimpleAnimeProperty extends SimpleObjectProperty<Anime> {
    private final ObjectProperty<Anime> anime;
    private final Binding<String> id;
    private final Binding<String> title;
    private final Binding<Image> image;
    private final Binding<Integer> episodes;
    private final Binding<Double> score;
    private final Binding<String> synopsis;
    private final Binding<List<String>> genres;

    public SimpleAnimeProperty(Anime anime) {
        String imageUnavailableUrl = "https://media.cheggcdn.com/media/d53/d535ce9a-4535-4e56-bd8e-81300a25a4f7/php4KwLCz";

        this.anime = new SimpleObjectProperty<>(anime);
        this.id = EasyBind.wrapNullable(this.anime).map(Anime::id).orElse("ID Not Found");
        this.title = EasyBind.wrapNullable(this.anime).map(Anime::title).orElse("Title Not Found");
        this.image = EasyBind.wrapNullable(this.anime).map(Anime::image).orElse(new Image(imageUnavailableUrl));
        this.episodes = EasyBind.wrapNullable(this.anime).map(Anime::episodes).orElse(0);
        this.score = EasyBind.wrapNullable(this.anime).map(Anime::score).orElse(0.0);
        this.synopsis = EasyBind.wrapNullable(this.anime).map(Anime::synopsis).orElse("Synopsis Not Found");
        this.genres = EasyBind.wrapNullable(this.anime).map(Anime::genres).orElse(Collections.singletonList("Genres Not Found"));
    }

    public SimpleAnimeProperty() {
        String imageUnavailableUrl = "https://media.cheggcdn.com/media/d53/d535ce9a-4535-4e56-bd8e-81300a25a4f7/php4KwLCz";

        this.anime = new SimpleObjectProperty<>();
        this.id = EasyBind.wrapNullable(this.anime).map(Anime::id).orElse("ID Not Found");
        this.title = EasyBind.wrapNullable(this.anime).map(Anime::title).orElse("Title Not Found");
        this.image = EasyBind.wrapNullable(this.anime).map(Anime::image).orElse(new Image(imageUnavailableUrl));
        this.episodes = EasyBind.wrapNullable(this.anime).map(Anime::episodes).orElse(0);
        this.score = EasyBind.wrapNullable(this.anime).map(Anime::score).orElse(0.0);
        this.synopsis = EasyBind.wrapNullable(this.anime).map(Anime::synopsis).orElse("Synopsis Not Found");
        this.genres = EasyBind.wrapNullable(this.anime).map(Anime::genres).orElse(Collections.singletonList("Genres Not Found"));
    }

    public ObjectProperty<Anime> animeProperty() {
        return anime;
    }

    public Binding<String> idBinding() {
        return id;
    }

    public Binding<String> titleBinding() {
        return title;
    }

    public Binding<Image> imageBinding() {
        return image;
    }

    public Binding<Integer> episodesBinding() {
        return episodes;
    }

    public Binding<Double> scoreBinding() {
        return score;
    }

    public Binding<String> synopsisBinding() {
        return synopsis;
    }
    public Binding<List<String>> genresBinding() {
        return genres;
    }

    @Override
    public Anime get() {
        return super.get();
    }

    @Override
    public void set(Anime anime) {
        this.anime.set(anime);
    }
}

