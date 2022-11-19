import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * This class is the controller class of the application.
 * It is responsible for handling all the events that occur in the GUI.
 * 
 * @author John Seong
 * @version 1.0
 */

public class MainController {
    @FXML
    ImageView background, statusImage;

    @FXML
    Button startButton, rescueButton;

    @FXML
    AnchorPane mainContainer;

    @FXML
    Label errorMessage;

    @FXML
    HBox wordBox, textFieldBox;

    private boolean isStartMenu;
    private int backgroundImageCounter = 0;

    private ArrayList<TextField> textFields = new ArrayList<TextField>();
    private ArrayList<Boolean> isCorrect = new ArrayList<Boolean>();

    private String generatedString, input;

    private char[] currentGeneratedWord;

    private GameManager game;

    /**
     * This method is called when the application is launched.
     * It is responsible for setting the background image,
     * and configuring the visibilities of different GUI elements.
     * Not only that, it sets up an array composed of all the text fields.
     * 
     * @since 1.0
     */

    @FXML
    public void initialize() {
        // Starts the background animation thread...
        isStartMenu = true;

        textFieldBox.getChildren().forEach((node) -> {
            TextField tf = (TextField) node;
            textFields.add(tf);
            
            tf.setDisable(false);
            addTextLimiter(tf, 1);
        });

        // Load the CSS...
        mainContainer.getStylesheets().add(getResourceFileAsString("/styles.css", true));

        cycleBackgroundImage();
        setButtonImage();

        textFieldBox.setVisible(false);
        rescueButton.setVisible(false);
        errorMessage.setVisible(false);
        statusImage.setVisible(false);
        startButton.setVisible(true);
    }

    /**
     * This method sets up a new asynchronous thread that cycles through the background images.
     * This happens with a delay of 0.15 seconds.
     * 
     * @since 1.0
     */

    private void cycleBackgroundImage() {
        // Creates a new non-blocking thread that runs asyncronously...
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i <= 6; i++) {
                    setBackgroundImage("screens/Basic_Stickman_" + i + ".png");

                    try {
                        // Delay for 0.15 seconds...
                        Thread.sleep(150);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Closes the thread...
                    if (!isStartMenu) {
                        // Reset the background image to default...
                        setBackgroundImage("screens/Basic_Stickman_0.png");
                        return;
                    }

                    // Otherwise keep going...
                    if (i == 6)
                        i = 0;
                }
            }
        }).start();
    }

    /**
     * This method sets the background image of the application.
     * 
     * @param path The path of the image to be set.
     * 
     * @since 1.0
     */

    private void setBackgroundImage(String path) {
        Image image = new Image(getResourceFileAsStream(path));

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        background.setImage(image);
        background.resize(screenBounds.getWidth(), screenBounds.getHeight());
    }

    /**
     * This method sets the image of the start button.
     * 
     * @since 1.0
     */

    private void setButtonImage() {
        ImageView view = new ImageView(new Image((getResourceFileAsStream("assets/PlayButton.png"))));

        view.setFitHeight(Constants.BUTTON_HEIGHT);
        view.setPreserveRatio(true);

        startButton.setGraphic(view);
        startButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * This method sets the image of the statusImage,
     * which shows whether the player has won or lost the game.
     * 
     * @param path The path of the image to be set.
     * 
     * @since 1.0
     */

    private void setStatusImage(String path) {
        Image image = new Image(getResourceFileAsStream(path));

        statusImage.setImage(image);
        statusImage.setPreserveRatio(true);
    }

    /**
     * This protected method converts a path to a file into a InputStream.
     * 
     * @param path The path of the file to be converted.
     * 
     * @return The InputStream of the file.
     * 
     * @since 1.0
     */

    protected InputStream getResourceFileAsStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    /**
     * This protected method converts a path to a file into a String.
     * 
     * @param path The path of the file to be converted.
     * 
     * @return The String of the file.
     * 
     * @since 1.0
     */

    protected String getResourceFileAsString(String path) {
        return getClass().getResource(path).toExternalForm().replace("file:", "");
    }

    /**
     * This protected method converts a path to a file into a String.
     * 
     * @param path The path of the file to be converted.
     * @param isCSSFile Whether the file is a CSS file.
     * 
     * @return The String of the file.
     * 
     * @since 1.0
     */

    protected String getResourceFileAsString(String path, boolean isCSSFile) {
        if (isCSSFile == false)
            return null;

        return getClass().getResource(path).toExternalForm();
    }

    /**
     * This method is called upon the change of the text in the text fields.
     * It limits the text to only one character.
     * Depending on whether the user has correctly guessed the word,
     * it will set the visibility of different UI elements.
     * 
     * TUTORIAL USED FOR ADDING A LISTENER TO A TEXT FIELD:
     * https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength
     * 
     * @param textField The text field that is being listened to.
     * @param maxLength The maximum length of the text field.
     * 
     * @since 1.0
     */

    public void addTextLimiter(final TextField textField, final int maxLength) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue,
                    final String newValue) {
                String s = textField.getText();
                textField.setText(s.toUpperCase());

                if (textField.getText().length() > maxLength) {
                    String sub = s.substring(0, maxLength);
                    textField.setText(sub);
                }

                if (textFields.stream().allMatch(t -> !t.getText().isBlank())) {
                    // Reset the input string to prevent past value and present value concentration
                    // issue...
                    input = "";

                    textFieldBox.getChildren().forEach((node) -> {
                        TextField tf = (TextField) node;
                        input += tf.getText();
                    });

                    if (game.checkIfWordExists(input)) {
                        errorMessage.setVisible(false);
                        rescueButton.setVisible(true);

                    } else {
                        // Not a vocabulary that exists in the JSON array...
                        errorMessage.setVisible(true);
                        rescueButton.setVisible(false);
                    }

                } else {
                    errorMessage.setVisible(false);
                    rescueButton.setVisible(false);
                }
            }
        });
    }

    /**
     * This method is called upon the click of different buttons.
     * It specifies the behaviour of the buttons.
     * 
     * @param event The event that is being listened to.
     * 
     * @since 1.0
     */

    @FXML
    protected void onClick(ActionEvent event) {
        Button button = (Button) event.getSource();

        switch (button.getId()) {
            default:
            case "startButton":
                // Closes the background animation thread...
                isStartMenu = false;

                startGame();
                break;

            case "rescueButton":
                if (button.getText() == "TRY AGAIN...") {
                    textFieldBox.getChildren().forEach((node) -> {
                        TextField tf = (TextField) node;

                        // Clear the textfields...
                        tf.setBackground(new Background(Constants.TRANSPARENT));
                        tf.setText("");
                    });

                    button.setText("RESCUE MR. STICK!");

                } else {
                    wordEntered();
                }

                break;
        }
    }

    /**
     * This method is called upon the click of the start button.
     * It sets the visibility of different UI elements, as well as initialising the GameManager class.
     * It also retrieves a random word from the JSON array by calling one of the methods in the GameManager class.
     * 
     * @since 1.0
     */

    private void startGame() {
        startButton.setVisible(false);
        textFieldBox.setVisible(true);

        game = new GameManager();

        currentGeneratedWord = game.getWordInCharArray();
        generatedString = game.getWordAsString();

        System.out.println("Answer: " + generatedString);
    }

    /**
     * This method is called upon the click of the rescue button.
     * It checks whether the user has correctly guessed the word.
     * 
     * If the user has guessed the word correctly, it will highlight the text fields green and set the status image to a win image.
     * If the user has guessed the character correctly but is in a different position, it will highlight the text fields yellow.
     * If the user has guessed the word incorrectly, it will highlight the text fields red and trigger the animation of the stick figure.
     * 
     * @since 1.0
     */

    private void wordEntered() {
        isCorrect.clear();

        List<Character> cList = new ArrayList<Character>();

        char[] cArray = generatedString.toCharArray();

        for (char c : cArray) {
            cList.add(c);
        }

        for (int i = 0; i < textFields.size(); i++) {
            String gw = "" + currentGeneratedWord[i];
            String t = textFields.get(i).getText();

            Label label = (Label) wordBox.getChildren().get(i);

            if (gw.equals(t)) {
                isCorrect.add(true);
                label.setText("" + currentGeneratedWord[i]);

                // Set it to an empty char...
                cList.set(i, ' ');

                textFields.get(i).setBackground(new Background(Constants.GREEN));

                // Check if character exists at other places...
            } else if (cList.contains(t.charAt(0))) {
                isCorrect.add(false);
                textFields.get(i).setBackground(new Background(Constants.YELLOW));

            } else {
                isCorrect.add(false);
                textFields.get(i).setBackground(new Background(Constants.RED));
            }
        }

        // If all items in the array are set to true...
        if (isCorrect.stream().allMatch(t -> t == true)) {
            // SUCCESS!
            rescueButton.setVisible(false);
            statusImage.setVisible(true);

            setStatusImage("assets/SuccessText.png");

            textFieldBox.getChildren().forEach((node) -> {
                TextField tf = (TextField) node;
                tf.setDisable(true);
            });

        } else {
            // FAILURE!
            rescueButton.setText("TRY AGAIN...");

            backgroundImageCounter++;
            setBackgroundImage("screens/Basic_Stickman_" + backgroundImageCounter + ".png");

            if (backgroundImageCounter >= 7) {
                // ALL ATTEMPTS EXHAUSTED...
                rescueButton.setVisible(false);
                textFieldBox.setVisible(false);
            }
        }
    }
}