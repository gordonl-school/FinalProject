import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;


class Sleeper implements Runnable {
    int ms;
    public Sleeper(int ms) {
        this.ms = ms;
    }
    @Override
    public void run() {
        try {
            Enemy.attackDebounce = true;
            Weapon.bulletDebounce = true;
//            if (Weapon.bulletDebounce) {
//                System.out.println("sleep");
//                Thread.sleep(1200);
//            } else if (Weapon.debounce) {
//                Thread.sleep(1000);
//            }
            System.out.println("MS: " + ms);
            Thread.sleep(ms);
            Enemy.attackDebounce = false;
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
    private Timer enemyTimer;
    private TileManager tileM;
    private CollisionChecker checker;
    private Enemy enemy;
    private Weapon weapon;
    private Enemy enemy1;
    private GameFunction gameFunction;
    private static boolean timerPulse = false;
    private boolean gameGoing;
    private static int numTimes = 1;
    private JButton restart;

    int bounds;
    private AnimationController animationController;

    private ArrayList<Enemy> enemies;

    private ArrayList<BufferedImage> bullets;
    private ArrayList<Double> bulletX;
    private ArrayList<Double> bulletY;
    private ArrayList<Double> bulletVX;
    private ArrayList<Double> bulletVY;

    Thread sleeperThreadBullet;
    Thread sleeperThreadEnemy;

    int mouseMotionX;
    int mouseMotionY;
    AffineTransform transform;
    boolean rotated = false;


    // Elapsed time
    // Current time
    // Shoot time -> Last shoot time
    // See if the difference is more than two hundreds


    public GamePanel(GameFrame gameFrame) {

        // Make use of the timer
        animationController = new AnimationController(50);
        timer = new Timer(2, this);
        enemyTimer = new Timer(5000, this);
        timer.start();
        enemyTimer.start();
//        enemyTimer = new Timer(10, this);
//        enemyTimer.start();

        // Variables
        keyPressed = new boolean[128];

        // Implements
        addKeyListener(this);
        addMouseMotionListener(this);

        // Other Classes
        checker = new CollisionChecker(this);

        weapon = new Weapon();
        gameFunction = new GameFunction();
        gameGoing = true;

        player = new Player(gameFrame, this, enemy, weapon, animationController);
        tileM = new TileManager(this, gameFrame, player, enemy);
        enemies = new ArrayList<Enemy>();
        enemy = new Enemy(player, animationController);
        enemies.add(enemy);

//        enemy = new Enemy(player);
//        enemy1 = new Enemy(player);
//        enemy1.setxCordE(700);

        this.gameFrame = gameFrame;


        setFocusable(true);
        requestFocusInWindow();

        bounds = 999999;
        enemies = new ArrayList<>();

        bullets = new ArrayList<>();
        bulletX = new ArrayList<>();
        bulletY = new ArrayList<>();
        bulletVX = new ArrayList<>();
        bulletVY = new ArrayList<>();

        mouseMotionX = 0;
        mouseMotionY = 0;

        transform = new AffineTransform();

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        sleeperThreadEnemy = new Thread(new Sleeper(1000));
        sleeperThreadBullet = new Thread(new Sleeper(200));
        if (player.getHealth() == 0) {
            gameGoing = false;
        }
//        if (gameGoing == false) {
//            restart = new JButton("Restart?");
//            restart.addActionListener(this);
//            add(restart);
//        }

        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        if (gameGoing) {
            tileM.draw(g2);

            g2.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), gameFrame.tileSize, gameFrame.tileSize, null);
            g.drawImage(weapon.gun, weapon.getGunCoordX(), weapon.getGunCoordY(), null);

//        int centerX = weapon.gun.getWidth() / 2;
//        int centerY = weapon.gun.getHeight() / 2;
//        transform.rotate(rotationAngle, centerX, centerY);
            if (!rotated) {
                rotated = true;
    
            }
            g2.rotate(Math.toRadians(45), player.getxCoord(), player.getyCoord());
            g2.drawImage(weapon.gun, player.getxCoord(), player.getyCoord(), this);
            g2.rotate(Math.toRadians(30), 50, 50);
            g2.setTransform(transform);
    
            if (enemy.getxCordE() < player.getxCoord()) {
                enemy.faceRight();
                g.drawImage(enemy.getEnemyImage(), enemy.getxCordE(), enemy.getyCordE(), enemy.getWidth(), gameFrame.tileSize, null);
            } else {
                enemy.faceLeft();
                g.drawImage(enemy.getEnemyImage(), enemy.getxCordE(), enemy.getyCordE(), enemy.getWidth(), gameFrame.tileSize, null);
            }
    
            // Text
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.drawString("Health: " + player.health + "/" + player.maxHealth, 5, 20);
    
            if (!Weapon.bulletDebounce && keyPressed[KeyEvent.VK_V]) {
                // This is to calculate the velocity
                double startX = player.getxCoord() + gameFrame.tileSize / 2.0;
                double startY = player.getyCoord() + gameFrame.tileSize / 2.0;
    
                double dx = mouseMotionX - startX;
                double dy = mouseMotionY - startY;
                double distance = Math.sqrt(dx * dx + dy * dy);
    
                if (distance > 0) {
                    double speed = 8.0;
                    double vx = (dx / distance) * speed;
                    double vy = (dy / distance) * speed;
    
                    // Store things for new bullet
                    bullets.add(weapon.bullet);
                    bulletX.add(startX);
                    bulletY.add(startY);
                    bulletVX.add(vx);
                    bulletVY.add(vy);
    
                    Thread newSleeperThread = new Thread(new Sleeper(200));
                    newSleeperThread.start();
                }
            }
            for (int i = 0; i < bullets.size(); i++) {
                bulletX.set(i, bulletX.get(i) + bulletVX.get(i));
                bulletY.set(i, bulletY.get(i) + bulletVY.get(i));
                g.drawImage(bullets.get(i), (int) Math.round(bulletX.get(i)), (int) Math.round(bulletY.get(i)), null);
            }
            for (int i = bullets.size() - 1; i >= 0; i--) {
                double x = bulletX.get(i);
                double y = bulletY.get(i);
                if (x < -100 || x > 1100 || y < -100 || y > 1100) {
                    bullets.remove(i);
                    bulletX.remove(i);
                    bulletY.remove(i);
                    bulletVX.remove(i);
                    bulletVY.remove(i);
                }
            }
    
            if (player.playerRect().intersects(enemy.enemyRect())) {
                if (!Enemy.attackDebounce && enemy.health > 0) {
                    player.health -= enemy.attack;
                    System.out.println("Touched");
                    sleeperThreadEnemy.start(); // 1s
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
        } else {
            g.setFont(new Font("Times New Roman", Font.BOLD, 36));
            g.drawString("Game Over!", 250, 350);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object sender = e.getSource();
        if (sender == timer) {
            repaint();
        }
        if (sender == enemyTimer) {
            int minX = 0;
            int maxX = 16 * 16;
            int minY = 0;
            int maxY = 16 * 17;
            int i = 0;
            Random random = new Random();
            while (i < numTimes) {
                enemy = new Enemy(player, animationController);
                int randomX = random.nextInt(maxX - minX) + minX;
                int randomY = random.nextInt(maxY - minY) + minY;
                enemy.setxCordE(randomX);
                enemy.setyCordE(randomY);
                enemies.add(enemy);
                i++;
            }
            numTimes++;
        }
        if (sender == restart) {
            gameGoing = true;
        }
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
        mouseMotionX = e.getX();
        mouseMotionY = e.getY();
        System.out.println("Mouse moved to: (" + mouseMotionX + ", " + mouseMotionY + ")");
    }
}