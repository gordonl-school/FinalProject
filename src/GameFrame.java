import javax.swing.*;

public class GameFrame {
    public GameFrame() {
        JFrame frame = new JFrame("Brotato Clone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700); // 540 height of image + 40 for window menu bar
        frame.setLocationRelativeTo(null);
        GraphicsPanel panel = new GraphicsPanel();
        frame.add(panel);
        frame.setVisible(true);
    }
}
