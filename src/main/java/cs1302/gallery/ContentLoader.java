package cs1302.gallery;

import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import java.net.URLEncoder;
import java.net.URL;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import java.io.IOException;

/**
 * Represents a {@code ContentLoader} that is a {@code VBox}. It contains
 * the tilepane which is meant to hold artwork images taken from
 * the iTunes Search API.
 */
public class ContentLoader extends VBox {

    ImageView[] displayedImages;
    ImageView[] hiddenImages;
    TilePane tile;
    int numUrls;
    int urlCount;
    private GalleryApp app;

    /**
     * Constructor for {@code ContentLoader} that acts as a VBox. It
     * initializes a tile pane and adds it to the content loader. It sets
     * the height and width of the content loader. In addition, it initializes
     * two image view arrays for the displayed images and hidden images.
     *
     * @param app the current application object
     */
    public ContentLoader(GalleryApp app) {
        super();
        this.app = app;

        tile = new TilePane();
        tile.setPrefColumns(5);

        // initializes displayed ImageView objects to an array
        displayedImages = new ImageView[20];
        for (int i = 0; i < 20; i++) {
            displayedImages[i] = new ImageView();
        } // for

        // initializes hidden ImageView objects to an array
        hiddenImages = new ImageView[130];
        for (int j = 0; j < 130; j++) {
            hiddenImages[j] = new ImageView();
        } // for

        // sets height and width of content loader and adds the tilepane
        this.setPrefWidth(500);
        this.setPrefHeight(400);
        this.getChildren().addAll(tile);
    } // ContentLoader

    /**
     * Loads the artwork images into the tilepane. It retrieves results
     * for a query formatted using JSON. It then retrieves information
     * using Gson to acquire the URLs for artwork images. The artwork
     * images are then displayed to the user.
     *
     * @param sUrl the query represented as a string
     */
    public void loadContent(String sUrl) {
        try {
            // runs before loading images
            beforeLoading();

            // sets up string to search the iTunes API
            sUrl = URLEncoder.encode(sUrl, "UTF-8");
            sUrl = "https://itunes.apple.com/search?term=" + sUrl;
            sUrl = sUrl + "&limit=150&media=music";
            // uses the URL from the query and downloads the result
            URL url = new URL(sUrl);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            // parses the JSON response
            JsonElement je = JsonParser.parseReader(reader);
            // converts to datatype using Gson
            JsonObject root = je.getAsJsonObject();
            // interacts with array from iTunes Search API
            JsonArray results = root.getAsJsonArray("results");
            int numResults = results.size();

            // tracks the number of URLs
            numUrls = 0;
            // puts valid urls in a string array
            String[] urlStrings = getUrls(results, numResults);
            // deletes any duplicates
            urlStrings = deleteDuplicates(urlStrings);

            // if number of URLs is less than 21, throw an exception
            if (numUrls < 21) {
                throw new IllegalArgumentException("less than 21 distinct image URLs gathered");
            } // if

            // sets the URL count to the number of URLs if no exception occurs
            urlCount = numUrls;

            // tracks loading images for the progress bar
            app.progressArea.progressCount = 0;
            // takes string URLs, downloads images, and sets imageview arrays
            setImageViews(urlStrings);

            // clears tilepane before adding imageviews
            Platform.runLater(() -> tile.getChildren().clear());
            // adds displayed imageviews to the tilepane
            for (int i = 0; i < 20; i++) {
                addTiles(i);
            } // for

        } catch (IllegalArgumentException | IOException e) {
            // handles alerts
            Runnable showAlert = () -> alert(e);
            Platform.runLater(showAlert);
        } finally {
            // runs after loading images
            afterLoading();
        } // try

    } // loadContent

    /**
     * If an exception is encountered, then an alert is shown to the user.
     *
     * @param e an encountered exception
     */
    private void alert(Exception e) {
        // shows an alert to the user
        Alert alert = new Alert(AlertType.ERROR);
        TextArea alertText = new TextArea(e.toString());
        alert.getDialogPane().setContent(alertText);
        alert.setResizable(true);
        alert.show();
    } // alert

    /**
     * Gets the URLs from the JsonArray and puts them into a string array. Only puts
     * the URL in the string array if JsonElement is not null.
     *
     * @param results the JsonArray from the iTunes Search API
     * @param numResults number of results from the search query
     * @return the string array containing the URLs
     */
    private String[] getUrls(JsonArray results, int numResults) {
        String[] urlStrings = new String[150];

        for (int i = 0; i < numResults; i++) {
            // get each result with the JsonArray
            JsonObject result = results.get(i).getAsJsonObject();
            JsonElement artworkUrl100 = result.get("artworkUrl100");
            // checks if the JsonElement is null
            if (!artworkUrl100.isJsonNull()) {
                String imageUrl = artworkUrl100.getAsString();
                // adds URL string into the array
                urlStrings[numUrls] = imageUrl;
                // keeps track of number of URLs
                numUrls += 1;
            } // if
        } // for

        return urlStrings;
    } // getUrls

    /**
     * Deletes any duplicates from the URL string array.
     *
     * @param urlStrings the string array containing URLs
     * @return the string array without duplicate URLs
     */
    private String[] deleteDuplicates(String[] urlStrings) {
        // do nothing if there are 0 or 1 URLs
        if (urlStrings.length == 0 || urlStrings.length == 1) {
            return urlStrings;
        } // if

        // boolean array to check for duplicates
        boolean[] delete = new boolean[numUrls];
        for (int i = 0; i < delete.length; i++) {
            delete[i] = true;
        } // for

        // loops through and marks duplicates for deletion
        for (int i = 0; i < delete.length; i++) {
            if (delete[i] == true) {
                // checks all following values
                for (int j = i + 1; j < delete.length; j++) {
                    // checks if there are duplicates
                    if (urlStrings[i].equals(urlStrings[j])) {
                        // if there is a duplicate, make it false
                        delete[j] = false;
                        // decrease number of URLs
                        numUrls -= 1;
                    } // if
                } // for
            } // if
        } // for

        String[] newArray = new String[numUrls];
        int count = 0;

        // loops through and adds strings if boolean is true
        for (int i = 0; i < delete.length; i++) {
            // if boolean is true, add string URL to the array
            if (delete[i] == true) {
                newArray[count] = urlStrings[i];
                count += 1;
            } // if
        } // for

        return newArray;
    } // deleteDuplicates

    /**
     * Places image views inside of two arrays. The first array consists
     * of images that will be diplayed in the main content area. The second
     * array consists of images that are not currently visible.
     *
     * @param urlStrings string array that contains the URLs
     */
    private void setImageViews(String[] urlStrings) {
        // loop for displayed images
        for (int i = 0; i < 20; i++) {
            // downloads the image from the URL string
            final Image artImage = new Image(urlStrings[i], 100, 100, false, true);
            // adds 1 to the progress count
            app.progressArea.progressCount += 1;
            // sets progress to progress count divided by URL count
            Platform.runLater(() -> setProgress(app.progressArea.progressCount / urlCount));
            // checks if there is an error getting the image
            if (!artImage.isError()) {
                // modifies displayed image view array
                setDisplayed(i, artImage);
            } // if
        } // for

        // loop for hidden images
        for (int i = 0; i < urlCount - 20; i++) {
            // downloads the image from the URL string
            final Image artImage = new Image(urlStrings[i + 20], 100, 100, false, true);
            // adds 1 to the progress count
            app.progressArea.progressCount += 1;
            // sets progress to progress count divided by URL count
            Platform.runLater(() -> setProgress(app.progressArea.progressCount / urlCount));
            // checks if there is an error getting the image
            if (!artImage.isError()) {
                // modifies hidden image view array
                setHidden(i, artImage);
            } // if
        } // for
    } // setImageViews

    /**
     * Modifies image view array that contains displayed images. Sets an image
     * to each image view.
     *
     * @param count index in the displayed image view array
     * @param artImage artwork to be set in the image view
     */
    private void setDisplayed(final int count, final Image artImage) {
        displayedImages[count].setImage(artImage);
    } // setDisplayed

    /**
     * Modifies image view array that contains hidden images. Sets an image
     * to each image view.
     *
     * @param count index in the hidden image view array
     * @param artImage artwork to be set in the image view
     */
    private void setHidden(final int count, final Image artImage) {
        hiddenImages[count].setImage(artImage);
    } // setHidden

    /**
     * Updates the tile pane. It adds each image view from the displayed array
     * into the tilepane.
     *
     * @param count index in the displayed image view array
     */
    public void addTiles(final int count) {
        Platform.runLater(() -> tile.getChildren().add(displayedImages[count]));
    } // addTiles

    /**
     * Code that executes before images are loaded. Sets the progress
     * bar to 0% complete and disables the update button.
     */
    private void beforeLoading() {
        app.toolBar.updateImages.setDisable(true);
        Platform.runLater(() -> setProgress(0));
    } // beforeLoading

    /**
     * Code that always executes after the try-catch block. It runs even
     * if an exception is thrown. Sets the progress bar to 100% complete
     * and enables the update button.
     */
    private void afterLoading() {
        app.toolBar.updateImages.setDisable(false);
        Platform.runLater(() -> setProgress(1));
    } // afterLoading

    /**
     * Sets the progress of the progress bar. The progress bar fills up
     * as more images are downloaded.
     *
     * @param progress the current progress of downloading the images
     */
    private void setProgress(final double progress) {
        Platform.runLater(() -> app.progressArea.progressBar.setProgress(progress));
    } // setProgress
} // ContentLoader
