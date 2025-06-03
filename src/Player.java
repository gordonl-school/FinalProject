import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    // Other Classes
    GameFrame gameFrame;
    TileManager tileManager;

    private final int MOVE_AMOUNT = 4;
    private BufferedImage image;
    private Animation animationDown;
    private Animation animationUp;
    private Animation animationRight;
    private Animation animationLeft;
    private Direction direction;

    // Gun
    BufferedImage gun;
    int gunCoordX;
    int gunCoordY;

    // Player Stats
    private int xCoord;
    private int yCoord;

    static boolean debounce;
    int health;
    int maxHealth;

    public Player(GameFrame gameFrame, GamePanel gamePanel, Enemy enemy) {
        debounce = false;
        tileManager = new TileManager(gamePanel, gameFrame, this, enemy);
        // Player Stats
        health = 100;
        maxHealth = 100;

        direction = Direction.DOWN;
        xCoord = 393;
        yCoord = 324;

        gunCoordX = 370;
        gunCoordY = 340;

        try {
            gun = ImageIO.read(new File("src/OtherSprites/Gun.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

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
        gunCoordX += MOVE_AMOUNT;
    }
    public void moveLeft() {
        direction = Direction.LEFT;
        xCoord -= MOVE_AMOUNT;
        gunCoordX -= MOVE_AMOUNT;
    }
    public void moveUp() {
        direction = Direction.UP;
        yCoord -= MOVE_AMOUNT;
        gunCoordY -= MOVE_AMOUNT;
    }
    public void moveDown() {
        direction = Direction.DOWN;
        yCoord += MOVE_AMOUNT;
        gunCoordY += MOVE_AMOUNT;
    }


    // Getter Methods
    public int getxCoord() {
        return xCoord;
    }
    public int getyCoord() {
        return yCoord;
    }

//    public int getScreenX() {return screenX;}
//    public int getScreenY() {return screenY;}


    // Other important methods

//    public void damageTaken(int damage) throws InterruptedException {
//        health -= damage;
//        Thread.sleep(1000);
//    }

    public BufferedImage getPlayerImage() {
        switch (direction) {
            case UP -> { return animationUp.getActiveFrame(); }
            case DOWN -> { return animationDown.getActiveFrame(); }
            case LEFT -> { return animationLeft.getActiveFrame(); }
            case RIGHT -> { return animationRight.getActiveFrame(); }
            default -> { return null; }
        }
    }

    public Rectangle playerRect() {
        // Calculations
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        Rectangle rect = new Rectangle(xCoord, yCoord, imageWidth, imageHeight);
        return rect;
    }
}
