package com.animearray.ouranimearray.widgets;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.tobiasdiez.easybind.EasyBind;
import javafx.beans.binding.Binding;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.image.Image;

import java.util.Collections;
import java.util.List;

public class AnimeProperty extends ObjectPropertyBase<Anime> {
    String imageUnavailableUrl = "https://media.cheggcdn.com/media/d53/d535ce9a-4535-4e56-bd8e-81300a25a4f7/php4KwLCz";
    private final Binding<String> id;
    private final Binding<String> title;
    private final Binding<Image> image;
    private final Binding<Integer> episodes;
    private final Binding<Double> score;
    private final Binding<String> synopsis;
    private final Binding<List<String>> genres;

    public AnimeProperty(Anime anime) {
        set(anime);

        // If property is unavailable, set default
        this.id = EasyBind.wrapNullable(this).map(Anime::id).orElse("ID Not Found");
        this.title = EasyBind.wrapNullable(this).map(Anime::title).orElse("Title Not Found");
        this.image = EasyBind.wrapNullable(this).map(Anime::image).orElse(new Image(imageUnavailableUrl));
        this.episodes = EasyBind.wrapNullable(this).map(Anime::episodes).orElse(0);
        this.score = EasyBind.wrapNullable(this).map(Anime::score).orElse(0.0);
        this.synopsis = EasyBind.wrapNullable(this).map(Anime::synopsis).orElse("Synopsis Not Found");
        this.genres = EasyBind.wrapNullable(this).map(Anime::genres).orElse(Collections.singletonList("Genres Not Found"));
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

    public AnimeProperty() {
        this(null);
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
