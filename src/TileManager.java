import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TileManager {
    GamePanel gamePanel;
    GameFrame gameFrame;
    Player player;
    Tile[] tile;
    int x;
    int y;

    public TileManager(GamePanel gamePanel, GameFrame gameFrame, Player player) {
        this.gamePanel = gamePanel;
        this.gameFrame = gameFrame;
        this.player = player;
        tile = new Tile[2];
        getTileImage();
        x = -gameFrame.tileSize * 5;  // -240
        y = -gameFrame.tileSize * 5;  // -240
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
        int tileSize = gameFrame.tileSize;

        // Extraneous code for now
//        int worldX = gameFrame.maxWorldCol * tileSize;
//        int worldY = gameFrame.maxWorldRow * tileSize;
//        int screenX = worldX - player.getWorldX() + player.getScreenX();
//        int screenY = worldY - player.getWorldY() + player.getScreenY();

        // Generating all the grass
        for (int r = 0; r < gameFrame.maxWorldCol ; r++) {
            int xPos = x + r * gameFrame.tileSize;

            for (int c = 0; c < gameFrame.maxWorldCol; c++) {
                int yPos = y + c * gameFrame.tileSize;
                graphics2D.drawImage(tile[1].image, xPos, yPos, tileSize, tileSize, null);
            }
        }

   

//        }
//        for (int r = 0; r < gameFrame.maxWorldCol; r++) {
//            graphics2D.drawImage(tile[0].image, tileSize * r, 0, tileSize, tileSize, null);
//        }
//        for (int r = 0; r < gameFrame.maxWorldCol; r += 2) {
//            graphics2D.drawImage(tile[0].image, tileSize * r, tileSize, tileSize, tileSize, null);
//        }
    }

    public void moveRight() {
        x += 3;

    }
    public void moveLeft() {
        x -= 3;

    }
    public void moveDown() {
        y -= 3;
    }
    public void moveUp() {
        y += 3;
    }
}
