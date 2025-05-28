import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private boolean[] keyPressed;

    // Other Classes
    private Player player;
    private GameFrame gameFrame;
    private Timer timer;
    private TileManager tileM;


    public GamePanel(GameFrame gameFrame) {
        timer = new Timer(2, this);
        timer.start();

        // Variables
        keyPressed = new boolean[128];

        // Implements
        addKeyListener(this);

        // Other Classes
        player = new Player();
        this.gameFrame = gameFrame;
        tileM = new TileManager(this);

        setFocusable(true);
        requestFocusInWindow();
    }

    // Getter Methods
    public int getTileSize() {
        return gameFrame.tileSize;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);

        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), null);

        if (keyPressed[KeyEvent.VK_A]) {
            player.moveLeft();
        }
        if (keyPressed[KeyEvent.VK_D]) {
            player.moveRight();
        }
        if (keyPressed[KeyEvent.VK_W]) {
            player.moveUp();
        }
        if (keyPressed[KeyEvent.VK_S]) {
            player.moveDown();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false;
    }
}
