import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameFunction {
    int wave;
    int enemy;

    BufferedImage emerald;
    public GameFunction() {
        wave = 1;
        enemy = 1;
    }

    public void addWave() {
        wave += 1;
    }
    public void calculateEnemies() {
        enemy = (int) ((enemy + 1) * 1.5);
    }
}
