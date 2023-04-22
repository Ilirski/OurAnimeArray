package com.animearray.ouranimearray.search;

import com.animearray.ouranimearray.model.Anime;
import com.animearray.ouranimearray.widgets.AnimeGridCell;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.controlsfx.control.GridView;
import org.tbee.javafx.scene.layout.MigPane;

import java.util.function.Consumer;

import static com.animearray.ouranimearray.widgets.Widgets.createSearchBar;

public class SearchPageViewBuilder implements Builder<Region> {
    private final SearchPageModel model;
    private final Consumer<Runnable> fetchAnimeList;

    public SearchPageViewBuilder(SearchPageModel model, Consumer<Runnable> fetchAnimeList) {
        this.model = model;
        this.fetchAnimeList = fetchAnimeList;
    }
    @Override
    public Region build() {
        MigPane searchPane = new MigPane(
                new LC().fill(),
                new AC().grow(),
                new AC().grow());

        searchPane.add(createAnimeGridView(fetchAnimeList), new CC().dockNorth());
        return searchPane;
    }

    private MigPane createAnimeGridView(Consumer<Runnable> fetchAnimeList) {
        MigPane animeGridPane = new MigPane(new LC().fill());
        animeGridPane.setBackground(Background.fill(Paint.valueOf("#2b2b2b")));

        GridView<Anime> animeGridView = new GridView<>(model.animeListProperty());
        animeGridView.setCellFactory(gridView -> new AnimeGridCell(model, true));
        // We want image width : height -> 227.0 : 350.0
        animeGridView.setCellWidth(225);
        animeGridView.setCellHeight(350);
        animeGridView.setHorizontalCellSpacing(10);
        animeGridView.setVerticalCellSpacing(20);

        // So that not on the window edge
        animeGridPane.add(createSearchBar(model.searchQueryProperty(), fetchAnimeList),
                new CC().wrap().width("50%").height("10").alignX("center").alignY("top").gap("0", "0", "10", "5"));
        animeGridPane.add(animeGridView, new CC().grow());
        return animeGridPane;
    }
}
