import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Enemy {
    // Classes
    GameFrame gameFrame;

    // Animation + Direction
    private Animation animation;
    private boolean facingRight;
    private Direction direction;

    // Enemy Stats
    private final int MOVE_AMOUNT = 3;

    int attack;
    int health;

    int xCordE;
    int yCordE;

    BufferedImage enemy1;

    public Enemy(GameFrame gameFrame) {
        // Cords
        xCordE = 100;
        yCordE = 100;

        // Enemy Stats
        attack = 5;
        health = 100;

        facingRight = true;

        try {
            enemy1 = ImageIO.read(new File("src/Enemy/enemy000.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String fileName = "src/Enemy/enemy00" + i + ".png";
            try {
                images.add(ImageIO.read(new File(fileName)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        animation = new Animation(images, 50);
    }

    public void faceRight() {
        direction = Direction.RIGHT;
    }
    public void faceLeft() {
        direction = Direction.LEFT;
    }

    public BufferedImage getPlayerImage() {
        switch (direction) {
            case LEFT -> { return animation.getActiveFrame(); }
            case RIGHT -> { return animation.getActiveFrame(); }
            default -> { return null; }
        }
    }

    public Rectangle enemyRect() {
        int imageHeight = enemy1.getHeight();
        int imageWidth = enemy1.getWidth();
        Rectangle rect = new Rectangle(xCordE, yCordE, imageWidth, imageHeight);
        return rect;
    }
}
