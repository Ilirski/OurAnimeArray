package com.animearray.ouranimearray.report;

import com.animearray.ouranimearray.widgets.DAOs.Anime;
import com.animearray.ouranimearray.widgets.DAOs.Genre;
import com.animearray.ouranimearray.widgets.DAOs.ReportType;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReportPageViewBuilder implements Builder<Region> {
    private final ReportPageModel model;
    private final Consumer<Runnable> getMostPopularAnime;
    private final Consumer<Runnable> getGenres;

    public ReportPageViewBuilder(ReportPageModel model, Consumer<Runnable> getMostPopularAnime, Consumer<Runnable> getGenres) {
        this.model = model;
        this.getMostPopularAnime = getMostPopularAnime;
        this.getGenres = getGenres;
    }

    @Override
    public Region build() {
        MigPane mainChartPane = new MigPane(
                new LC().fill()
        );

        mainChartPane.add(createBarChart(), new CC().grow());
        getMostPopularAnime.accept(() -> {});
        getGenres.accept(() -> {});

        return mainChartPane;
    }

    public Region createBarChart() {
        StackPane stackPane = new StackPane();
        MigPane chartPane = new MigPane(
                new LC().fill()
        );

        // Create loading spinner
        MFXProgressSpinner loadingSpinner = new MFXProgressSpinner();
        MigPane loadingPane = new MigPane(new LC().fill());
        loadingPane.add(loadingSpinner, new CC().alignX("center").alignY("center").width("15%").height("15%"));
        loadingPane.visibleProperty().bind(model.loadingProperty());
        loadingSpinner.setId("loading-spinner");
        loadingPane.setId("loading-pane");

        // Create bar chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        var titleProperty = Bindings.createStringBinding(() -> {
            int animeAmount = model.getMostPopularAnimesAmount();
            String reportType;
            if (model.getSelectedReportType() == null) {
                reportType = "Unknown";
            } else {
                switch (model.getSelectedReportType()) {
                    case Popularity -> reportType = "Popular";
                    case Score -> reportType = "Highly Rated";
                    case UserScore -> reportType = "Highly User Rated";
                    default -> reportType = "Unknown";
                }
            }
            String genre = model.getSelectedGenre() != null ? model.getSelectedGenre().genre() : "";
            String format = genre.isEmpty() ? "Top %d Most %s Animes" : "Top %d Most %s %s Animes";
            return String.format(format, animeAmount, reportType, genre);
        }, model.mostPopularAnimesProperty(), model.selectedReportTypeProperty(), model.selectedGenreProperty());

        barChart.titleProperty().bind(titleProperty);
        xAxis.setLabel("Anime");
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        barChart.setAnimated(false);

        model.mostPopularAnimesProperty().addListener((InvalidationListener) observable -> {
            // Clear existing data from bar chart
            barChart.getData().clear();

            // Create a new data series
            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
            dataSeries.setName("Anime");

            // Add data to the new series
            for (Anime anime : model.getMostPopularAnimes()) {
                if (model.getSelectedReportType() == ReportType.Popularity) {
                    dataSeries.getData().add(new XYChart.Data<>(anime.title(), anime.members()));
                    yAxis.setLabel("Members");
                } else if (model.getSelectedReportType() == ReportType.Score) {
                    dataSeries.getData().add(new XYChart.Data<>(anime.title(), anime.score()));
                    yAxis.setLabel("Score");
                } else if (model.getSelectedReportType() == ReportType.UserScore) {
                    dataSeries.getData().add(new XYChart.Data<>(anime.title(), anime.score()));
                    yAxis.setLabel("User Score");
                } else {
                    throw new IllegalStateException("Unexpected value: " + model.getSelectedReportType());
                }
            }

            // Add the new series to the bar chart
            barChart.getData().add(dataSeries);

            // Add tooltip to each bar
            for (final XYChart.Series<String, Number> series : barChart.getData()) {
                for (final XYChart.Data<String, Number> data : series.getData()) {
                    MFXTooltip.of(data.getNode(), String.format("%,d", data.getYValue().longValue())).install();
                }
            }
        });

        Label caption = new Label();
        caption.textProperty().bind(Bindings.createStringBinding(() -> {
            String reportType;
            if (model.getSelectedReportType() == null) {
                reportType = "Unknown";
            } else {
                switch (model.getSelectedReportType()) {
                    case Popularity -> reportType = "Hover over the bars to see the number of members";
                    case Score -> reportType = "Hover over the bars to see the score";
                    case UserScore -> reportType = "Hover over the bars to see the user score";
                    default -> reportType = "Unknown";
                };
            }
            return reportType;
        }, model.selectedReportTypeProperty()));

        caption.setStyle("-fx-font-size: 20px;");

        MFXSlider animeNumSlider = createAnimeNumSlider();
        MFXFilterComboBox<Genre> animeGenreComboBox = createAnimeGenreComboBox();
        MFXComboBox<ReportType> sortComboBox = createSortComboBox();
        MFXButton saveButton = new MFXButton("Save as CSV");
        saveButton.setOnAction(event -> exportDataToCSV(barChart, titleProperty.get()));

        chartPane.add(caption, new CC().grow().wrap().alignX("center"));
        chartPane.add(animeNumSlider, new CC().grow().split(4));
        chartPane.add(animeGenreComboBox, new CC().grow());
        chartPane.add(sortComboBox, new CC().grow());
        chartPane.add(saveButton, new CC().grow().wrap());
        chartPane.add(barChart, new CC().grow().height("90%"));

        // Add loading pane to stack pane
        stackPane.getChildren().addAll(chartPane, loadingPane);
        stackPane.setAlignment(Pos.CENTER);
        return stackPane;
    }

    private MFXComboBox<ReportType> createSortComboBox() {
        MFXComboBox<ReportType> sortComboBox = new MFXComboBox<>();
        sortComboBox.setFloatingText("Sort by");
        sortComboBox.getItems().addAll(ReportType.values());
        sortComboBox.getSelectionModel().selectItem(ReportType.Popularity);
        model.selectedReportTypeProperty().bind(sortComboBox.selectedItemProperty());
        // If sort type changes, fetch new anime list
        model.selectedReportTypeProperty().addListener(observable -> {
            model.setLoading(true);
            getMostPopularAnime.accept(() -> model.setLoading(false));
        });
        return sortComboBox;
    }

    private MFXFilterComboBox<Genre> createAnimeGenreComboBox() {
        MFXFilterComboBox<Genre> animeGenreComboBox = new MFXFilterComboBox<>();
        animeGenreComboBox.setFloatingText("Genre");
        model.genresProperty().addListener((InvalidationListener) observable -> {
            animeGenreComboBox.setItems(model.getGenres());
        });

        // Convert genre to string
        StringConverter<Genre> genreStringConverter = FunctionalStringConverter.to(genre -> (genre == null) ? "" : genre.genre());
        animeGenreComboBox.setConverter(genreStringConverter);

        // Filter anime genres by genre name
        Function<String, Predicate<Genre>> genreFilterFunction = s -> genre -> StringUtils.containsIgnoreCase(genre.genre(), s);
        animeGenreComboBox.setFilterFunction(genreFilterFunction);

        model.selectedGenreProperty().bind(animeGenreComboBox.selectedItemProperty());
        animeGenreComboBox.selectedItemProperty().addListener(observable -> {
            model.setLoading(true);
            getMostPopularAnime.accept(() -> model.setLoading(false));
        });
        return animeGenreComboBox;
    }

    private MFXSlider createAnimeNumSlider() {
        MFXSlider animeNumSlider = new MFXSlider(0, 50, 15);
        animeNumSlider.setMin(2);
        Duration VALUE_CHANGE_DELAY = Duration.seconds(1);
        double EPSILON = 0.000001;
        Timeline valueChangeTimer;

        model.mostPopularAnimesAmountProperty().bind(animeNumSlider.valueProperty());
        valueChangeTimer = new Timeline(new KeyFrame(VALUE_CHANGE_DELAY, event -> {
            // Perform action here after 1 seconds of value not changing
            model.setLoading(true);
            getMostPopularAnime.accept(() -> model.setLoading(false));
        }));

        valueChangeTimer.setCycleCount(1);
        valueChangeTimer.setOnFinished(event -> {
            // Value changed during the 1 seconds, cancel the timer
            valueChangeTimer.stop();
        });

        animeNumSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (Math.abs(newValue.doubleValue() - oldValue.doubleValue()) > EPSILON) {
                // Restart the timer when the value changes
                valueChangeTimer.stop();
                valueChangeTimer.playFromStart();
            }
        });
        return animeNumSlider;
    }

    public void exportDataToCSV(BarChart<String, Number> barChart, String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath + ".csv")) {
            StringBuilder sb = new StringBuilder();

            // Write header row
            sb.append(barChart.getXAxis().getLabel()).append(",").append(barChart.getYAxis().getLabel()).append("\n");

            // Iterate over series and data items
            for (XYChart.Series<String, Number> series : barChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    String category = data.getXValue();
                    Number value = data.getYValue();

                    // Append data to StringBuilder
                    sb.append(category).append(",").append(value).append("\n");
                }
            }

            // Write data to the file
            writer.write(sb.toString());
            writer.flush();

            System.out.println("Data exported to CSV successfully.");
        } catch (IOException e) {
            System.err.println("Error exporting data to CSV: " + e.getMessage());
        }
    }

}
