package cs1302.gallery;

import javafx.scene.layout.HBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;

/**
 * {@code ProgressArea} represents an {@code HBox}. It contains
 * a progress bar to show the progress of downloading images.
 * It also contains a label to show the source of the images.
 */
public class ProgressArea extends HBox {

    ProgressBar progressBar;
    Label courtesy;
    double progressCount;
    private GalleryApp app;

    /**
     * Constructs a new {@code ProgressArea} that is an {@code HBox}.
     * It sets the spacing to 5. It creates a new progress bar that
     * is 0% complete and a new label which tells the user that
     * the images are provided from iTunes.
     *
     * @param app the current application object
     */
    public ProgressArea(GalleryApp app) {
        super(5);
        this.app = app;

        // creates new progress bar with 0 progress
        progressBar = new ProgressBar(0);

        // tracks progress of downloading images; used in ContentLoader
        progressCount = 0;

        // creates the courtesy statement
        courtesy = new Label("Images provided courtesy of iTunes");

        // adds progress bar and courtesy label to the progress area
        this.getChildren().addAll(progressBar, courtesy);
    } // ProgressArea
} // ProgressArea
