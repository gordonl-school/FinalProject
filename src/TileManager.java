import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class TileManager {
    GamePanel gamePanel;
    Tile[] tile;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile = new Tile[2];
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("src\\Tiles\\Tree.png")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(tile[0].image, 0,0, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }
}
