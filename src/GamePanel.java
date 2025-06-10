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
    private JButton start;
    private boolean started = false;
    private boolean newWave = true;


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
        start = new JButton("Play!");
        start.addActionListener(this);
        add(start);
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
        if (player.getHealth() == 0) {
            gameGoing = false;
        }

        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        if (started) {
            start.setVisible(false);
            if (gameGoing) {
                tileM.draw(g2);

                g2.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), gameFrame.tileSize, gameFrame.tileSize, null);
                g.drawImage(weapon.gun, weapon.getGunCoordX(), weapon.getGunCoordY(), null);

                for (int i = 0; i < enemies.size(); i++) {
                    Enemy currentEnemy = enemies.get(i);
                    currentEnemy.move();

                    if (currentEnemy.getxCordE() < player.getxCoord()) {
                        currentEnemy.faceRight();
                    } else {
                        currentEnemy.faceLeft();
                    }
                    g.drawImage(currentEnemy.getEnemyImage(), currentEnemy.getxCordE(), currentEnemy.getyCordE(), currentEnemy.getWidth(), gameFrame.tileSize, null);

                }


                // Text
                g.setFont(new Font("Courier New", Font.BOLD, 24));
                g.drawString("Health: " + player.health + "/" + player.maxHealth, 5, 20);
                g.drawString("Enemies: " + enemies.size(), 5, 50);

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

                checkBulletCollisions();

                for (int i = enemies.size()-1; i >= 0; i--) {
                    Enemy currentEnemy = enemies.get(i);
                    if (player.playerRect().intersects(currentEnemy.enemyRect())) {
                        if (!Enemy.attackDebounce && currentEnemy.getHealth() > 0) {
                            player.health -= currentEnemy.attack;
                            Thread sleeperThread = new Thread(new Sleeper(1000));
                            sleeperThread.start();
                        }
                    }
                }

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
                if (numTimes == 21) {
                    g.setFont(new Font("Times New Roman", Font.BOLD, 36));
                    g.drawString("You win!", 275, 350);
                } else {
                    g.setFont(new Font("Times New Roman", Font.BOLD, 36));
                    g.drawString("Game Over!", 275, 350);
                }
            }
        } else {
            start.setSize(300, 100);
            start.setFont(new Font("EB Garamond", Font.BOLD, 64));
            start.setLocation(250, 300);
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
            int maxX = 16 * 48;
            int minY = 0;
            int maxY = 48 * 17;
            int i = 0;
            Random random = new Random();
            while (i < numTimes && newWave) {
                enemy = new Enemy(player, animationController);
                int randomX = random.nextInt(maxX - minX) + minX;
                int randomY = random.nextInt(maxY - minY) + minY;
                enemy.setxCordE(randomX);
                enemy.setyCordE(randomY);
                enemies.add(enemy);
                i++;
            }
            newWave = false;
            if (enemies.isEmpty()) {
                newWave = true;
                numTimes++;
            }
            if (numTimes == 21) {
                newWave = false;
                gameGoing = false;
            }
        }
        if (sender == start) {
            started = true;
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
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseMotionX = e.getX();
        mouseMotionY = e.getY();
//        System.out.println("Mouse moved to: (" + mouseMotionX + ", " + mouseMotionY + ")");
    }

    private void checkBulletCollisions() {
        for (int i = bullets.size() -1; i >= 0; i--) {
            double bX = bulletX.get(i);
            double bY = bulletY.get(i);

            // NEED TO CREATE A NEW RECTANGLE HREE BECAUSE IT HAS DIFFERENT COORDINATES THAN THE ONE IN WEAPON
            Rectangle bulletRect = new Rectangle((int) bX, (int) bY, bullets.get(i).getWidth(), bullets.get(i).getHeight());

            // Loop enemies
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy currentEnemy = enemies.get(j);
                if (bulletRect.intersects(currentEnemy.enemyRect())) {
                    currentEnemy.setHealth(currentEnemy.getHealth() - weapon.gunDamage);
                    System.out.println("Hit\nEnemy Health: " + currentEnemy.getHealth());

                    bullets.remove(i);
                    bulletX.remove(i);
                    bulletY.remove(i);
                    bulletVX.remove(i);
                    bulletVY.remove(i);

                    if (currentEnemy.getHealth() <= 0) {
                        System.out.println("Enemy dead");
                        enemies.remove(j);
                    }
                    break;
                }
            }
        }
    }
}