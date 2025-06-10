import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameFunction {
    int coinCoordX;
    int coinCoordY;

    BufferedImage coin;
    public GameFunction() {

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
    public Rectangle coinRect() {
        int imageHeight = coin.getHeight();
        int imageWidth = coin.getWidth();
        Rectangle rect = new Rectangle(coinCoordX, coinCoordY, imageWidth, imageHeight);
        return rect;
    }
}
