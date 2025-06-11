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
    Weapon weapon;

    private int MOVE_AMOUNT = 5;
    private BufferedImage image;
    private Animation animationDown;
    private Animation animationUp;
    private Animation animationRight;
    private Animation animationLeft;
    private Direction direction;
    private AnimationController animationController;

    // Player Stats
    private int xCoord;
    private int yCoord;

    int health;
    int maxHealth;

    public Player(GameFrame gameFrame, GamePanel gamePanel, Enemy enemy, Weapon weapon, AnimationController controller) {
        // Cords
        animationController = controller;
        tileManager = new TileManager(gamePanel, gameFrame, this, enemy);
        this.weapon = weapon;
        // Player Stats
        health = 200;
        maxHealth = 200;

        direction = Direction.DOWN;
        xCoord = 393;
        yCoord = 324;

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
        animationDown = new Animation(images);
        animationController.addAnimation(animationDown);

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
        animationLeft = new Animation(images);
        animationController.addAnimation(animationLeft);

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
        animationRight = new Animation(images);
        animationController.addAnimation(animationRight);

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
        animationUp = new Animation(images);
        animationController.addAnimation(animationUp);

    }

    // Key Interactions
    public void moveRight() {
        direction = Direction.RIGHT;
        xCoord += MOVE_AMOUNT;
        weapon.gunCoordX += MOVE_AMOUNT;
    }
    public void moveLeft() {
        direction = Direction.LEFT;
        xCoord -= MOVE_AMOUNT;
        weapon.gunCoordX -= MOVE_AMOUNT;

    }
    public void moveUp() {
        direction = Direction.UP;
        yCoord -= MOVE_AMOUNT;
        weapon.gunCoordY -= MOVE_AMOUNT;

    }
    public void moveDown() {
        direction = Direction.DOWN;
        yCoord += MOVE_AMOUNT;
        weapon.gunCoordY += MOVE_AMOUNT;

    }


    // Getter Methods
    public int getxCoord() {
        return xCoord;
    }
    public int getyCoord() {
        return yCoord;
    }

    public int getMOVE_AMOUNT() {
        return MOVE_AMOUNT;
    }
    public void setMOVE_AMOUNT(int MOVE_AMOUNT) {
        this.MOVE_AMOUNT = MOVE_AMOUNT;
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

    public int getHealth() {
        return health;
    }
}
