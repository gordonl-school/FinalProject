import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Weapon {
    static boolean debounce;
    static boolean bulletDebounce;
    // Gun
    BufferedImage gun;
    BufferedImage bullet;

    int gunCoordX;
    int gunCoordY;
    int bulletX;
    int bulletY;


    // Damage
    int gunDamage;

    public Weapon() {
        debounce = false;
        bulletDebounce = false;
        gunCoordX = 370;
        gunCoordY = 340;

        gunDamage = 50;

        try {
            gun = ImageIO.read(new File("src/OtherSprites/Gun.png"));
            bullet = ImageIO.read(new File("src/OtherSprites/Bullet.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getGunCoordX() {
        return gunCoordX;
    }

    public int getGunCoordY() {
        return gunCoordY;
    }

//    public int getBulletX() {
//        return bulletX;
//    }
//
//    public int getBulletY() {
//        return bulletY;
//    }

    public Rectangle bulletRect() {
        int imageHeight = bullet.getHeight();
        int imageWidth = bullet.getWidth();
        Rectangle rect = new Rectangle(bulletX, bulletY, imageWidth, imageHeight);
        return rect;
    }
}
