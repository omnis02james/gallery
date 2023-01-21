package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents an iTunes GalleryApp. The app consists of a menu,
 * toolbar, 20 artwork images, and a progress bar. The user is able
 * to search for artwork using a text field and can pause/resume
 * random image replacement. If random image replacement is on,
 * then a random image is replaced with a random image that is currently
 * not shown every 2 seconds.
 */
public class GalleryApp extends Application {

    private static final String DEFAULT_QUERY = "pop";

    VBox root;
    MenuLoader menuLoader;
    Toolbar toolBar;
    ContentLoader contentLoader;
    ProgressArea progressArea;

    /**
     * Entry point for the iTunes gallery application.
     *
     * {@inheritdoc}
     */
    @Override
    public void start(Stage stage) {
        // Initializes components for the scene
        root = new VBox();
        menuLoader = new MenuLoader(this);
        toolBar = new Toolbar(this);
        contentLoader = new ContentLoader(this);
        progressArea = new ProgressArea(this);

        // adds components to the root and sets the root to the scene
        root.getChildren().addAll(menuLoader, toolBar, contentLoader, progressArea);
        Scene scene = new Scene(root);

        // sets properties for the stage and makes it visible
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

        // Loads images from the default query after showing the stage
        String defaultQuery = DEFAULT_QUERY;
        runNow(() -> contentLoader.loadContent(defaultQuery));
    } // start


    /**
     * Creates and starts a daemon thread. The method comes
     * from the Brief Introduction to Java Threads reading.
     *
     * @param target a {@code Runnable} object which has a run method
     * which is executed when the thread is started
     */
    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } // runNow

} // GalleryApp
