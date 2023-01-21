package cs1302.gallery;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Modality;

/**
 * A MenuLoader is a custom component that represents a {@code MenuBar}. It
 * contains a File menu and an Help menu. The File menu contains an Exit menu
 * item that exits the app gracefully when clicked. The Help menu contains
 * an About menu item that displays the image, name, and email of the auothor
 * of this program and the version number of the app.
 */
public class MenuLoader extends MenuBar {

    Menu fileMenu;
    MenuItem exit;

    Menu helpMenu;
    MenuItem about;

    private GalleryApp app;

    /**
     * Constructs a MenuLoader that represents a {@code MenuBar}. It contains
     * a {@code Menu} which is labeled File. File contains a {@code MenuItem}
     * which is labeled Exit and exits the program gracefully if clicked.
     * Additionally, it contains a {@code Menu} which is labeled Help. Help
     * contains a {@code MenuItem} which is labeled About which provides info on
     * the author of the program and the version number when clicked.
     *
     * @param app the current application object
     */
    public MenuLoader(GalleryApp app) {
        super();
        this.app = app;

        fileMenu = new Menu("File");
        exit = new MenuItem("Exit");
        // event handler that exits the app
        EventHandler<ActionEvent> exitHandler = event -> Platform.exit();
        // sets event handler to the exit item
        exit.setOnAction(exitHandler);
        // adds exit to the file menu
        fileMenu.getItems().add(exit);

        helpMenu = new Menu("Help");
        about = new MenuItem("About");

        EventHandler<ActionEvent> aboutHandler = event -> {
            // sets up the root of the scene
            VBox aboutRoot = new VBox();

            // initializes the components for the scene
            Image aboutImage = new Image("file:resources/img.png", 250, 250, true, false);
            ImageView aboutView = new ImageView();
            aboutView.setImage(aboutImage);
            Text aboutText = new Text();
            aboutText.setFont(new Font(20));
            aboutText.setText("Name: James Yu\nEmail: jy67291@uga.edu\nVersion 1.0");

            // adds the components to the root of the scene
            aboutRoot.getChildren().addAll(aboutView, aboutText);

            // creates a new stage/window
            Stage aboutStage = new Stage();
            // sets the root to the scene
            Scene aboutScene = new Scene(aboutRoot);

            // sets the properties of the stage and makes the stage visible
            aboutStage.setMaxWidth(1280);
            aboutStage.setMaxHeight(720);
            aboutStage.setTitle("About James Yu");
            aboutStage.initModality(Modality.APPLICATION_MODAL);
            aboutStage.setScene(aboutScene);
            aboutStage.sizeToScene();
            aboutStage.show();
        };
        about.setOnAction(aboutHandler);
        helpMenu.getItems().add(about);

        // adds menus to the menu loader
        this.getMenus().addAll(fileMenu, helpMenu);
    } // MenuLoader

} // MenuLoader
