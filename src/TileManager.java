import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Objects;

public class TileManager {
    GamePanel gamePanel;
    GameFrame gameFrame;
    Player player;
    Tile[] tile;
    int[][] stoneMap;
    int x;
    int y;

    public TileManager(GamePanel gamePanel, GameFrame gameFrame, Player player) {
        this.gamePanel = gamePanel;
        this.gameFrame = gameFrame;
        this.player = player;
        tile = new Tile[4];
        stoneMap = new int[gameFrame.maxWorldCol][gameFrame.maxWorldCol];
        getTileImage();
//        x = -gameFrame.tileSize * 5;  // -240
//        y = -gameFrame.tileSize * 5;  // -240
        x = 0;
        y = 0;
    }

    public void generateRandomStoneMap() {
        Random rand = new Random();
        for (int r = 0; r < gameFrame.maxWorldCol; r++) {
            for (int c = 0; c < gameFrame.maxWorldCol; c++) {
                if (rand.nextBoolean()) {
                    stoneMap[r][c] = 0;
                } else {
                    stoneMap[r][c] = 1;
                }
             }
        }
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

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(new File("src/Tiles/Stone1.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(new File("src/Tiles/Stone2.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void draw(Graphics2D graphics2D) {
        int tileSize = gameFrame.tileSize;
        Random rand = new Random();

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

                Tile stoneTile;
                if (stoneMap[r][c] == 0) {
                    stoneTile = tile[2];
                } else {
                    stoneTile = tile[3];
                }
                graphics2D.drawImage(stoneTile.image, xPos, yPos, tileSize, tileSize, null);
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