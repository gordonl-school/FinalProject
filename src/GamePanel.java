import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Sleeper implements Runnable {
    @Override
    public void run() {
        try {
            Weapon.debounce = true;
            Weapon.bulletDebounce = true;
            Thread.sleep(1000);
            Weapon.debounce = false;
            Weapon.bulletDebounce = false;
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
    private Weapon weapon;
    private Enemy enemy1;



    public GamePanel(GameFrame gameFrame) {
        timer = new Timer(2, this);
        timer.start();

        // Variables
        keyPressed = new boolean[128];

        // Implements
        addKeyListener(this);
        addMouseMotionListener(this);

        // Other Classes
        checker = new CollisionChecker(this);

        weapon = new Weapon();

        player = new Player(gameFrame, this, enemy, weapon);
        tileM = new TileManager(this, gameFrame, player, enemy);

        enemy = new Enemy(player);

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
        g.drawImage(weapon.gun, weapon.getGunCoordX(), weapon.getGunCoordY(), null);

//        g.drawImage(enemy.enemy1, enemy.xCordE, enemy.yCordE, null);
//        g.drawImage(enemy.getEnemyImage(), enemy.getxCordE(), enemy.getyCordE(), gameFrame.tileSize, gameFrame.tileSize, null);
        if (enemy.getxCordE() < player.getxCoord()) {
            enemy.faceRight();
            g.drawImage(enemy.getEnemyImage(), enemy.getxCordE(), enemy.getyCordE(), enemy.getWidth(), gameFrame.tileSize, null);
        } else {
            enemy.faceLeft();
            g.drawImage(enemy.getEnemyImage(), enemy.getxCordE(), enemy.getyCordE(), enemy.getWidth(), gameFrame.tileSize, null);
        }


        // Text
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Health: " + player.health + "/" + player.maxHealth, 5,20);

        if (!Weapon.bulletDebounce) {
            weapon.bulletX -= 10;
            g.drawImage(weapon.bullet, weapon.bulletX, weapon.bulletY, null);
        }

        if (player.playerRect().intersects(enemy.enemyRect())) {
            if (!Weapon.debounce) {
                player.health -= enemy.attack;
                System.out.println("Touched");
                sleeperThread.start();
            }
        }

        if (enemy.enemyRect().intersects(weapon.bulletRect())) {
            if (!Weapon.bulletDebounce) {
                enemy.health -= weapon.gunDamage;
                System.out.println("Hit!");
                System.out.println("Enemy Health: " + enemy.health);
                sleeperThread.start();
            }
        }

        enemy.move();
        // Key interactions
        if (keyPressed[KeyEvent.VK_A]) {
            if (player.getxCoord() > 0) {
                player.moveLeft();
            }
        }
        if (keyPressed[KeyEvent.VK_D]) {
            if (player.getxCoord() < gameFrame.screenWidth - 48) {
                player.moveRight();
            }
        }
        if (keyPressed[KeyEvent.VK_W]) {
            if (player.getyCoord() > 0) {
                player.moveUp();
            }
        }
        if (keyPressed[KeyEvent.VK_S]) {
            if (player.getyCoord() < gameFrame.screenHeight - 87) {
                player.moveDown();
            }
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
        if (e.getKeyCode() == KeyEvent.VK_V) {
            weapon.bulletX = weapon.gunCoordX;
            weapon.bulletY = weapon.gunCoordY;
        }
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