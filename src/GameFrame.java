import javax.swing.*;

public class GameFrame {
    final int originalTileSize = 16;
    final int scale = 3;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 15;
    final int maxScreenRow = 17;
    final int screenHeight = tileSize * maxScreenCol;
    final int screenWidth = tileSize * maxScreenRow;

    public GameFrame() {
        JFrame frame = new JFrame("Brotato Clone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight); // 720 x 960
        frame.setLocationRelativeTo(null);

        GraphicsPanel panel = new GraphicsPanel();

        frame.add(panel);
        frame.setVisible(true);
    }
}
