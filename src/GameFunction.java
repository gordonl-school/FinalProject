import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameFunction {
    int wave;
    int enemy;

    int killedX;
    int killedY;

    int killed;

    BufferedImage coin;
    public GameFunction() {
        wave = 1;
        enemy = 1;
        killed = 0;

        try {
            coin = ImageIO.read(new File("src/OtherSprites/Coin.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addWave() {
        wave += 1;
    }
    public void calculateEnemies() {
        enemy = (int) ((enemy + wave) * 1.5);
    }
    public void deathCoords(int x, int y) {
        killedX = x;
        killedY = y;
    }
}
