import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * This class contains all the constants that will be used in the game.
 * Having centralized static variables allow the developers more control over the code,
 * including improved readability and ease of maintenence.
 * 
 * @author John Seong
 * @version 1.0
 */

public class Constants {
    public static final double BUTTON_HEIGHT = 70.0;

    public static final BackgroundFill RED = new BackgroundFill(Color.rgb(255, 109, 106),
            CornerRadii.EMPTY, Insets.EMPTY);

    public static final BackgroundFill GREEN = new BackgroundFill(Color.rgb(119, 221, 119),
            CornerRadii.EMPTY, Insets.EMPTY);

    public static final BackgroundFill YELLOW = new BackgroundFill(Color.rgb(233, 236, 107),
            CornerRadii.EMPTY, Insets.EMPTY);

    public static final BackgroundFill TRANSPARENT = new BackgroundFill(Color.TRANSPARENT,
            CornerRadii.EMPTY, Insets.EMPTY);
}
