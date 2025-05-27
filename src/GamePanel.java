import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private boolean[] keyPressed;
    private Player player;
    private GameFrame gameFrame;


    public GamePanel() {
        keyPressed = new boolean[128];
        addKeyListener(this);
        player = new Player();
        gameFrame = new GameFrame();
    }

    // Getter Methods
    public int getTileSize() {
        return gameFrame.tileSize;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), null);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

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
