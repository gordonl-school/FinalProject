import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameFunction {
    int coinCoordX;
    int coinCoordY;

    BufferedImage coin;

    private Enemy enemy;
    private GamePanel gamePanel;
    public GameFunction(Enemy enemy, GamePanel gamePanel) {
        this.enemy = enemy;
        this.gamePanel = gamePanel;
        try {
            coin = ImageIO.read(new File("src/OtherSprites/Coin.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCoinCoordX(int coinCoordX) {
        this.coinCoordX = coinCoordX;
    }
    public void setCoinCoordY(int coinCoordY) {
        this.coinCoordY = coinCoordY;
    }
    public void updateEnemy() {
        int wave = gamePanel.waves;
        double health = 100 + (300 - 100) * ((wave - 1) / 19.0); // This will go from 100 health starting to 300 health by wave 20 (I think?)
        double attack = 5 + (30 - 5) * ((wave - 1) / 19.0); // This will go from 5 attack starting to 30 attack by wave 20 (I think?)
        int movement = 2;
        if (wave >= 7) {
            movement = 3;
        }
        if (wave >= 14) {
            movement = 4;
        }
        if (wave >= 18) {
            movement = 5;
        }
        for (int i = 0; i < gamePanel.enemies.size(); i++) {
            enemy.setHealth((int) health);
            enemy.attack = (int) attack;
            enemy.setSPEED(movement);
        }
    }
    public Rectangle coinRect() {
        int imageHeight = coin.getHeight();
        int imageWidth = coin.getWidth();
        Rectangle rect = new Rectangle(coinCoordX, coinCoordY, imageWidth, imageHeight);
        return rect;
    }

}
