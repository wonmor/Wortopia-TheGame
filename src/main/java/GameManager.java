import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * GameManager class is responsible for managing the game.
 * It is responsible for loading the game data (mainly consisting of a word dictionary)
 * from the JSON file, and also for managing the game state.
 * 
 * @author John Seong
 * @version 1.0
 */

public class GameManager extends MainController {    
    private String[] words;
    private char[] ch;
    
    private String word;

    /**
     * A constructor for the GameManager class.
     * It loads the game data from the JSON file.
     * 
     * @since 1.0
     */

    public GameManager() {
        try {
            words = parseJSON("words.json");

        } catch (IOException | ParseException e) {
            e.printStackTrace();

        } finally {
            String currentWord = words[getRandomNumber(0, words.length - 1)];

            displayContent(currentWord);
        }
    }

    /**
     * This getter method parses the JSON file and returns the words in the form of a String.
     * 
     * @return String - contains the word that is randomly picked from the JSON file.
     * 
     * @since 1.0
     */

    public String getWordAsString() {
        return word;
    }

    /**
     * This getter method parses the JSON file and returns the words in the form of a char array.
     * 
     * @return char[] - contains the word that is randomly picked from the JSON file.
     * 
     * @since 1.0
     */

    public char[] getWordInCharArray() {
        return ch;
    }

    /**
     * This method checks if the input is valid by checking if the word is in the JSON file.
     * 
     * @param word - the word that the user inputs.
     * 
     * @return boolean - true if the word is in the JSON file, false otherwise.
     * 
     * @since 1.0
     */

    public boolean checkIfWordExists(String word) {
        for (String s : words) {
            if (s.equals(word.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method converts the word to a char array and stores it in the ch variable.
     * 
     * @param word - the word that the user inputs.
     * 
     * @since 1.0
     */

    private void displayContent(String word) {
        ch = word.toCharArray();
        this.word = word;
    }

    /**
     * This method randomly picks a number between the min and max values.
     * 
     * @param min - the minimum value.
     * @param max - the maximum value.
     * 
     * @return int - a random number between the min and max values.
     * 
     * @since 1.0
     */

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * This method parses the JSON file and returns the words in the form of a String array.
     * 
     * @param relativePath - the relative path of the JSON file.
     * 
     * @return String[] - contains the words in the JSON file.
     * 
     * @since 1.0
     */

    private String[] parseJSON(String relativePath) throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        
        // toExternalForm() will contain "file:" in front of the path. Therefore, we have to remove it.
        String jsonPath = getResourceFileAsString(relativePath);

        Object obj = parser.parse(new FileReader(jsonPath));

        // Convert the JSON string to a Java String array...
        return obj.toString().replace("[", "").replace("]", "").replaceAll("" + '"', "").toUpperCase().split(",");
    }
}
