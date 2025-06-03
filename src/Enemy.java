import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Enemy {
    int attack;
    int health;

    int xCordE;
    int yCordE;

    BufferedImage enemy1;

    public Enemy() {
        // Cords
        xCordE = 100;
        yCordE = 100;

        // Enemy Stats
        attack = 5;
        health = 100;

        try {
            enemy1 = ImageIO.read(new File("src\\EnemySprites//Grass.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Rectangle enemyRect() {
        int imageHeight = enemy1.getHeight();
        int imageWidth = enemy1.getWidth();
        Rectangle rect = new Rectangle(xCordE, yCordE, imageWidth, imageHeight);
        return rect;
    }

}
