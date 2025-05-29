import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private final int MOVE_AMOUNT = 3;
    private BufferedImage image;
    private Animation animationDown;
    private Animation animationUp;
    private Animation animationRight;
    private Animation animationLeft;
    private Direction direction;
//    private boolean facingRight;
//    private boolean facingDown;
    private int xCoord;
    private int yCoord;
    //rectangle for checking collision
    public Rectangle solidArea;
    public boolean collisionOn = false;

    public Player() {
//        facingDown = true;
        direction = Direction.DOWN;
        xCoord = 500;
        yCoord = 500;
        solidArea = new Rectangle(8, 16, 20, 30);


        // Down Loading Animation
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String fileName = "src/PlayerSprites/down00" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animationDown = new Animation(images, 50);

        // Left Loading Animation
        images = new ArrayList<>();
        for (int i = 4; i < 8; i++) {
            String fileName = "src/PlayerSprites/left00" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animationLeft = new Animation(images, 50);

        // Right Loading Animation
        images = new ArrayList<>();
        for (int i = 8; i < 10; i++) {
            String fileName = "src/PlayerSprites/right00" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = 10; i < 12; i++) {
            String fileName = "src/PlayerSprites/right0" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animationRight = new Animation(images, 50);

        // Up Loading Animation
        images = new ArrayList<>();
        for (int i = 12; i < 16; i++) {
            String fileName = "src/PlayerSprites/up0" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animationUp = new Animation(images, 50);
    }

    // Key Interactions
    public void moveRight() {
        direction = Direction.RIGHT;
        xCoord += MOVE_AMOUNT;
    }
    public void moveLeft() {
        direction = Direction.LEFT;
        xCoord -= MOVE_AMOUNT;
    }
    public void moveUp() {
        direction = Direction.UP;
        yCoord -= MOVE_AMOUNT;
    }
    public void moveDown() {
        direction = Direction.DOWN;
        yCoord += MOVE_AMOUNT;
    }

    // Getter Methods
    public int getxCoord() {
        return xCoord;
    }
    public int getyCoord() {
        return yCoord;
    }
    public BufferedImage getPlayerImage() {
        switch (direction) {
            case UP -> { return animationUp.getActiveFrame(); }
            case DOWN -> { return animationDown.getActiveFrame(); }
            case LEFT -> { return animationLeft.getActiveFrame(); }
            case RIGHT -> { return animationRight.getActiveFrame(); }
            default -> { return null; }
        }
    }
}
