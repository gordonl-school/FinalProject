import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private boolean[] keyPressed;
    private Player player;
    private GameFrame gameFrame;
    private Timer timer;


    public GamePanel(GameFrame frame) {
        timer = new Timer(2, this);
        timer.start();
        keyPressed = new boolean[128];
        addKeyListener(this);
        player = new Player();
        gameFrame = frame;
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
        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), null);
        if (keyPressed[65]) {
            player.moveLeft();
        }
        if (keyPressed[68]) {
            player.moveRight();
        }
        if (keyPressed[87]) {
            player.moveUp();
        }
        if (keyPressed[83]) {
            player.moveDown();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed[e.getKeyCode()] = false;
    }
}
