
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the main class of the application.
 * It is responsible for loading the FXML file and displaying the GUI.
 * 
 * @author John Seong
 * @version 1.0
 */

public class MainActivity extends Application {
    public static Scene scene;

    /**
     * This method loads the FXML file and sets the screen dimensions.
     * Overrides the start method from the Application class.
     * 
     * @param stage - the stage that will be displayed.
     * 
     * @since 1.0
     */

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("layout"), 700, 400);
        
        stage.setTitle("Wortopia");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method loads the FXML file and sets the root node.
     * 
     * @param fxml - the name of the FXML file.
     * 
     * @since 1.0
     */

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * This method loads the FXML file and returns the root node.
     * 
     * @param fxml - the name of the FXML file.
     * 
     * @return Parent - the root node of the FXML file.
     * 
     * @since 1.0
     */

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainActivity.class.getResource(fxml + ".fxml"));
        
        return fxmlLoader.load();
    }

    /**
     * This method is the main method of the application.
     * It launches the application.
     * 
     * @param args - the command line arguments.
     * 
     * @since 1.0
     */

    public static void main(String[] args) {
        launch();
    }
}