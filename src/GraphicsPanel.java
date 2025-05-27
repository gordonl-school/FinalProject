import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;


public class GraphicsPanel extends JPanel implements ActionListener, KeyListener {
    private boolean[] keyPressed;

    public GraphicsPanel() {
        keyPressed = new boolean[128];
        addKeyListener(this);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
