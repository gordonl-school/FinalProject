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

    private final int MOVE_AMOUNT = 3;
    private BufferedImage image;
    private Animation animationDown;
    private Animation animationUp;
    private Animation animationRight;
    private Animation animationLeft;
    private Direction direction;
    // Camera + World Movement
    private int xCoord;
    private int yCoord;

    // Eectangle for checking collision
    public Rectangle solidArea;
    public boolean collisionOn = false;

//    private final int screenX;
//    private final int screenY;

    // Player Stats
    int health;
    int maxHealth;

    public Player(GameFrame gameFrame, GamePanel gamePanel, Enemy enemy) {
        tileManager = new TileManager(gamePanel, gameFrame, this, enemy);
        // Player Stats
        health = 100;
        maxHealth = 100;

        direction = Direction.DOWN;
        xCoord = 393;
        yCoord = 324;
//        solidArea = new Rectangle(8, 16, 20, 30);
//
//        screenX = gameFrame.screenWidth/2 - (gameFrame.tileSize/2);
//        screenY = gameFrame.screenHeight/2 - (gameFrame.tileSize/2);
//        this.tileManager = tileManager;

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

//    public int getScreenX() {return screenX;}
//    public int getScreenY() {return screenY;}


    // Other important methods

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
        System.out.println("player: " + rect);
        return rect;
    }
}
