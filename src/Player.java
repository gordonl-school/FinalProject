import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private final int MOVE_AMOUNT = 3;
    private BufferedImage image;

    private int xCoord;
    private int yCoord;
    public Player() {
        xCoord = 500;
        yCoord = 500;
        try {
            image = ImageIO.read(new File("src\\Image.png"));
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

    public void moveRight() {
        xCoord += MOVE_AMOUNT;
    }
    public void moveLeft() {
        xCoord -= MOVE_AMOUNT;
    }
    public void moveUp() {
        yCoord += MOVE_AMOUNT;
    }
    public void moveDown() {
        yCoord -= MOVE_AMOUNT;
    }
}
