import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Sleeper implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Sleeper thread is starting");
            Player.debounce = true;
            Thread.sleep(1000);
            Player.debounce = false;
            System.out.println("Sleeper thread has finished sleeping");
        } catch (InterruptedException e) {
            System.out.println("Sleeper thread was interrupted");
        }
    }
}

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener {
    private boolean[] keyPressed;

    // Other Classes
    private Player player;
    private GameFrame gameFrame;
    private Timer timer;
    private TileManager tileM;
    private CollisionChecker checker;
    private Enemy enemy;


    public GamePanel(GameFrame gameFrame) {
        timer = new Timer(2, this);
        timer.start();

        // Variables
        keyPressed = new boolean[128];

        // Implements
        addKeyListener(this);
        addMouseMotionListener(this);

        // Other Classes
        enemy = new Enemy(gameFrame);
        checker = new CollisionChecker(this);

        player = new Player(gameFrame, this, enemy);
        tileM = new TileManager(this, gameFrame, player, enemy);

        this.gameFrame = gameFrame;

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

        Thread sleeperThread = new Thread(new Sleeper());

        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);

        g2.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), gameFrame.tileSize, gameFrame.tileSize, null);
        g.drawImage(enemy.enemy1, enemy.xCordE, enemy.yCordE, null);


        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Health: " + player.health + "/" + player.maxHealth, 5,20);

        if (player.playerRect().intersects(enemy.enemyRect())) {
            if (!Player.debounce) {
                player.health -= enemy.attack;
                System.out.println("Touched");
                sleeperThread.start();
            }
        }


        // Key interactions
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
        checker.checkTile(player);
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


    // MOUSE MOTION METHODS
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}