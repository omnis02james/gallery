package cs1302.gallery;

import javafx.scene.control.ToolBar;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Separator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * Creates a {@code Toolbar} that represents a {@code ToolBar}.
 * It is used to allow the user to play and pause random image
 * replacement. It is also used to enter a search query for the
 * iTunes Search API and allow the user to update the displayed images.
 */
public class Toolbar extends ToolBar {
    Button randomReplace;
    Label queryLabel;
    TextField search;
    Button updateImages;
    KeyFrame keyFrame;
    Timeline timeline;
    private GalleryApp app;

    /**
     * Constructs a {@code Toolbar} that is a {@code ToolBar}. It contains
     * a button that allows the user to play/pause random image
     * replacement. It also consists of a textfield that allows the
     * user to input a search query. The user can update the displayed
     * images using the update button.
     *
     * @param app the current application object
     */
    public Toolbar(GalleryApp app) {
        super();
        this.app = app;

        randomReplace = new Button("Play");
        queryLabel = new Label("Search Query:");
        search = new TextField("pop");
        updateImages = new Button("Update Images");

        // event handler to update images in the tile pane
        EventHandler<ActionEvent> handleUpdateImages = event -> {
            app.runNow(() -> updateImages(search.getText()));
        };
        // sets update handler to the update button
        updateImages.setOnAction(handleUpdateImages);

        // event handler to acquire random images and to place in keyframe
        EventHandler<ActionEvent> randomImages = event -> {
            randomReplacement();
        };

        // sets up the timeline to occur every 2 seconds indefinitely
        keyFrame = new KeyFrame(Duration.seconds(2), randomImages);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);

        // event handler to play/pause the timeline using the button
        EventHandler<ActionEvent> handleRandomImages = event -> {
            app.runNow(() -> handleReplacement());
        };
        // sets the random handler to the play/pause button
        randomReplace.setOnAction(handleRandomImages);

        this.getItems().addAll(randomReplace, new Separator(), queryLabel, search, updateImages);
    } // Toolbar

    /**
     * Updates the images in the tilepane. Creates a new thread
     * and uses a search query to start loading artwork images into
     * the tilepane.
     *
     * @param searchQuery the query to the iTunes Search API
     */
    private void updateImages(String searchQuery) {
        app.contentLoader.loadContent(searchQuery);
    } // updateImages

    /**
     * Randomly replaces images on the tilepane. It randomly chooses an
     * imageview from the displayed and hidden imageview arrays.
     * It then swaps the two images so that the hidden imageview
     * appears on the tilepane.
     */
    private void randomReplacement() {
        // chooses a random integers for the displayed and hidden imageviews
        int display = (int) (Math.random() * 20);
        int hide = (int) (Math.random() * (app.contentLoader.urlCount - 20));

        // stores the displayed imageview
        ImageView displayedImage = app.contentLoader.displayedImages[display];
        // sets the current displayed imageview to the hidden imageview
        app.contentLoader.displayedImages[display] = app.contentLoader.hiddenImages[hide];
        // sets the hidden imageview to the stored displayed imageview
        app.contentLoader.hiddenImages[hide] = displayedImage;

        // clears the tilepane of previous imageviews
        app.contentLoader.tile.getChildren().clear();
        // updates the tilepane with the displayed imageviews
        for (int i = 0; i < 20; i++) {
            app.contentLoader.tile.getChildren().add(app.contentLoader.displayedImages[i]);
        } // for
    } // randomReplacement

    /**
     * Provides functionality for the play/pause button.
     * Depending on the state of the button, the method will
     * either play or pause the timeline object and change
     * the button text to the other state.
     */
    private void handleReplacement() {
        // case if the button currently displays Play
        if (randomReplace.getText().equals("Play")) {
            Platform.runLater(() -> {
                // change text to Pause
                randomReplace.setText("Pause");
            });
            // play the timeline
            timeline.play();
        } else { // case if the button currently displays Pause
            Platform.runLater(() -> {
                // change text to Play
                randomReplace.setText("Play");
            });
            // pause the timeline
            timeline.pause();
        } // if
    } // handleReplacement
} // Toolbar
