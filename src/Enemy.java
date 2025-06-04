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
    private final int SPEED = 2;

    int attack;
    int health;

    int xCordE;
    int yCordE;

    Player playerReference;

    BufferedImage enemy1;

    public Enemy(Player player) {
        // Cords
        xCordE = 100;
        yCordE = 100;

        // Enemy Stats
        attack = 5;
        health = 100;

        playerReference = player;
        direction = Direction.RIGHT;

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

    public void killEnemy() {
        xCordE = 100000000;
        yCordE = 100000000;

    }

    public void faceRight() {
        direction = Direction.RIGHT;
    }
    public void faceLeft() {
        direction = Direction.LEFT;
    }

    public int getxCordE() {
        if (direction == Direction.RIGHT) {
            return xCordE;
        } else {
            return (xCordE + (getEnemyImage().getWidth()));
        }
    }

    public int getHeight() {
        return getEnemyImage().getHeight();
    }

    public int getWidth() {
        if (direction == Direction.RIGHT) {
            return getEnemyImage().getWidth();
        } else {
            return getEnemyImage().getWidth() * -1;
        }
    }

    public int getyCordE() {
        return yCordE;
    }

    public void setxCordE(int xCordE) {
        this.xCordE = xCordE;
    }

    public void setyCordE(int yCordE) {
        this.yCordE = yCordE;
    }

    public void move() {
        //Written by Supreme Leader Matthew Rotondi
        double angle = Math.atan2((playerReference.getyCoord() - yCordE), (playerReference.getxCoord() - xCordE));
        xCordE += Math.round(SPEED * Math.cos(angle));
        yCordE += Math.round(SPEED * Math.sin(angle));
    }

    public BufferedImage getEnemyImage() {
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
