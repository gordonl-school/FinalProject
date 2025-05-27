import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private final int MOVE_AMOUNT = 3;
    private BufferedImage image;
    private Animation animationDown;
    private Animation animationTop;
    private Animation animationSide;
    private Animation currentAnimation;
    private boolean facingRight;
    private boolean facingForward;
    private int xCoord;
    private int yCoord;

    public Player() {
        facingForward = true;
        facingRight = false;
        xCoord = 500;
        yCoord = 500;
        try {
            image = ImageIO.read(new File("src\\PlayerSprites\\tile000.png"));
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }

        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            String fileName = "src\\tile00" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        ///working on adding animatoin
    }

    // Key Interactions
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

    // Getter Methods
    public int getxCoord() {
        return xCoord;
    }
    public int getyCoord() {
        return yCoord;
    }
    public BufferedImage getPlayerImage() {
        return image;
    }
}
