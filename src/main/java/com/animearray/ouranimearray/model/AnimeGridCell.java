package com.animearray.ouranimearray.model;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.controlsfx.control.GridCell;
import org.tbee.javafx.scene.layout.MigPane;

import static com.animearray.ouranimearray.model.MouseClickNotDragDetector.clickNotDragDetectingOn;

public class AnimeGridCell extends GridCell<Anime> {
    private final ImageView imageView;
    private final Label imageLabel;
    private final boolean preserveImageProperties;
    private final MigPane gridCell;
    double targetWidth = 225;
    double targetHeight = 350;

    public AnimeGridCell(Model model, boolean preserveImageProperties) {

        this.preserveImageProperties = preserveImageProperties;
        imageView = new ImageView();
        imageLabel = new Label();
        imageLabel.setFont(Font.font("Source Code Pro", 12));

        gridCell = new MigPane(new LC().insets("0"));
        gridCell.add(imageView, new CC().wrap());
        gridCell.add(imageLabel, new CC().alignX("center"));
        gridCell.getStyleClass().add("animeGridCell");

        // IMPORTANT AS NODES OF SUBCLASS REGIONS DO NOT GET CALCULATED
        // BY THE GEOMETRIC SHAPE OF THE NODE
        // See https://stackoverflow.com/questions/24607969/mouse-events-get-ignored-on-the-underlying-layer
        setPickOnBounds(false);

        // https://stackoverflow.com/questions/27064975/rotate-a-dragboard-javafx-8
        // https://stackoverflow.com/questions/22424082/drag-and-drop-vbox-element-with-show-snapshot-in-javafx
        clickNotDragDetectingOn(this)
                .withPressedDurationThreshold()
                .setOnMouseClickedNotDragged(event -> {
                    if (event.isPrimaryButtonDown()) {
                        this.getScene().setCursor(Cursor.DEFAULT);
                    }

                    // Item can be null
                    Anime item = getItem();
                    if (item != null) {
                        model.setAnime(item);
                        if (!model.isRightSideBarVisible()) {
                            model.setRightSideBarVisible(true);
                        }
                    }
                });

        // https://stackoverflow.com/questions/26610660/how-to-drag-a-javafx-node-and-detect-a-drop-event-outside-the-javafx-windows

        setOnDragDetected(event -> {
            getScene().setCursor(Cursor.CLOSED_HAND);
            startFullDrag();
            addPreview();
        });

        setOnMouseReleased(e -> {
            getScene().setCursor(Cursor.DEFAULT);
            removePreview();
        });

        setOnMouseEntered(me -> {
            if (!me.isPrimaryButtonDown()) {
                this.getScene().setCursor(Cursor.HAND);
            }
        });

        setOnMouseExited(me -> {
            if (!me.isPrimaryButtonDown()) {
                this.getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    public static Rectangle2D calculateImageCrop(Image image, double targetWidth, double targetHeight) {
        // Calculate the crop dimensions to fit the aspect ratio
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();
        double targetAspectRatio = targetWidth / targetHeight;
        double originalAspectRatio = originalWidth / originalHeight;
        double cropX = 0;
        double cropY = 0;
        double cropWidth;
        double cropHeight;

        if (originalAspectRatio >= targetAspectRatio) {
            // The original image is wider than the target aspect ratio,
            // so we need to crop and center the width
            cropHeight = originalHeight;
            cropWidth = cropHeight * targetAspectRatio;
            cropX = (originalWidth - cropWidth) / 2;
        } else {
            // The original image is taller than the target aspect ratio,
            // so we need to crop the height and center the width
            cropWidth = originalWidth;
            cropHeight = cropWidth / targetAspectRatio;
            cropY = (originalHeight - cropHeight) / 2;
        }

        // Create a new cropped image using the calculated dimensions
        return new Rectangle2D(cropX, cropY, cropWidth, cropHeight);
    }

    @Override
    protected void updateItem(Anime item, boolean empty) {

        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            Image animePoster = item.image();
            String animeTitle = item.title();

            if (preserveImageProperties) {

                Rectangle2D cropRect = calculateImageCrop(animePoster, targetWidth, targetHeight);

                imageView.setViewport(cropRect);
                imageView.setFitWidth(targetWidth);
                imageView.setFitHeight(targetHeight);
                imageView.setSmooth(true);
                imageView.setImage(animePoster);
            }

            double leftPaddingRequired = calculateLeftPaddingRequired(targetWidth);
            setPadding(new Insets(0, 0, 0, leftPaddingRequired));
            imageLabel.setText(animeTitle);
            setGraphic(gridCell);
        }
    }

    private void addPreview() {
        ImageView preview = new ImageView(gridCell.snapshot(null, null));
        preview.setFitWidth(targetWidth / 1.2);
        preview.setFitHeight(targetHeight / 1.2);
        preview.setManaged(false);
        preview.setMouseTransparent(true);
        preview.setVisible(false);
        MigPane basePane = (MigPane) getScene().lookup("#basePane");
        basePane.add(preview);
        setUserData(preview);
        setOnMouseDragged(event -> {
            preview.setVisible(true);
            preview.relocate(event.getSceneX(), event.getSceneY());
        });
    }

    private void removePreview() {
        MigPane basePane = (MigPane) getScene().lookup("#basePane");
        setOnMouseDragged(null);
        basePane.getChildren().remove((ImageView) getUserData());
    }

    private double calculateLeftPaddingRequired(double targetWidth) {
        int SCROLLBAR_WIDTH = 18;
        int PADDING = (int) getGridView().getHorizontalCellSpacing();
        double gridViewWidthHalf = (getGridView().getWidth() - SCROLLBAR_WIDTH);
        // To prevent countItems from reaching zero
        double widthItems = (Math.max(Math.min(countItemsInRow(targetWidth), getGridView().getItems().size()), 1) * (targetWidth + (2 * PADDING)));
        return (gridViewWidthHalf - widthItems) / 2;
    }

    private int countItemsInRow(double targetWidth) {
        // WIDTH - SCROLLBAR_WIDTH / (PADDING + ITEM WIDTH)
        int SCROLLBAR_WIDTH = 18;
        int PADDING = (int) getGridView().getHorizontalCellSpacing();
        return (int) Math.floor((getGridView().getWidth() - SCROLLBAR_WIDTH) / (targetWidth + (2 * PADDING)));
    }
}

