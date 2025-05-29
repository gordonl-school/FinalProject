import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TileManager {
    GamePanel gamePanel;
    Tile[] tile;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile = new Tile[2];
        getTileImage();
    }

    public void getTileImage() {
        try {
            // Tree
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(new File("src/Tiles/Tree.png"));
            tile[0].collision = true;

            // Grass Block
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(new File("src/Tiles/Grass.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics2D graphics2D) {
        int tileSize = gamePanel.getTileSize();
//        final int maxScreenCol = 15;
//        final int maxScreenRow = 17;
        // Generating all the grass
        for (int r = 0; r < 17; r++) {
            for (int c = 0; c < 15; c++) {
                graphics2D.drawImage(tile[1].image, 48 * r,48 * c, tileSize, tileSize, null);
            }
        }
        for (int r = 0; r < 17; r++) {
            graphics2D.drawImage(tile[0].image, 48 * r, 0, tileSize, tileSize, null);
        }
        for (int r = 0; r < 17; r += 2) {
            graphics2D.drawImage(tile[0].image, 48 * r, 48, tileSize, tileSize, null);
        }

    }
}
