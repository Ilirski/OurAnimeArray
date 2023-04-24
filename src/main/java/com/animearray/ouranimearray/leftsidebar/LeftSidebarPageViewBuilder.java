package com.animearray.ouranimearray.leftsidebar;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import static com.animearray.ouranimearray.widgets.Widgets.createListToggle;

public class LeftSidebarPageViewBuilder implements Builder<Region> {
    private final LeftSidebarPageModel model;
    public LeftSidebarPageViewBuilder(LeftSidebarPageModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        MigPane leftSideBar = new MigPane(
                new LC().insets("0").fill()
        );

        ToggleGroup toggleGroup = new ToggleGroup();

        MigPane migScrollPane = new MigPane(new LC().wrapAfter(1).fill());

        var list1 = createListToggle("My Lists");
        var list2 = createListToggle("Recommended");

        toggleGroup.getToggles().addAll(list1, list2);

        migScrollPane.add(list1, new CC().grow().span());
        migScrollPane.add(list2, new CC().grow().span());

        leftSideBar.getStyleClass().add("left-sidebar");
        var scrollPane = new MFXScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(migScrollPane);

        leftSideBar.add(scrollPane, new CC().grow().push().maxWidth("100%"));
        return leftSideBar;
    }
}
