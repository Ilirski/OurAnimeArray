package com.animearray.ouranimearray.widgets;

import com.animearray.ouranimearray.widgets.DAOs.Genre;
import com.dlsc.gemsfx.TagsField;
import javafx.util.StringConverter;

import java.util.Comparator;

public class GenreTagsField extends TagsField<Genre> {
    public GenreTagsField() {
        setConverter(new StringConverter<>() {
            @Override
            public String toString(Genre object) {
                return object == null ? "" : object.genre();
            }

            @Override
            public Genre fromString(String string) {
                // Find genre with the same name
                return getTags().stream().filter(genre -> genre.genre().equals(string)).findFirst().orElse(null);
            }
        });
        setMatcher((genre, searchText) -> genre.genre().toLowerCase().startsWith(searchText.toLowerCase()));
        setComparator(Comparator.comparing(Genre::genre));
        getEditor().setPromptText("Start typing genres…");
    }
}
