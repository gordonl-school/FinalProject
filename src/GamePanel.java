import javax.crypto.spec.PSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;


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

    int bounds;

    private ArrayList<Enemy> enemies;



    public GamePanel(GameFrame gameFrame) {
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

        player = new Player(gameFrame, this, enemy, weapon);
        tileM = new TileManager(this, gameFrame, player, enemy);
        enemies = new ArrayList<Enemy>();
        enemy = new Enemy(player);
        enemies.add(enemy);

//        enemy = new Enemy(player);
//        enemy1 = new Enemy(player);
//        enemy1.setxCordE(700);

        this.gameFrame = gameFrame;


        setFocusable(true);
        requestFocusInWindow();

        bounds = 999999;
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

        /*
        1. The first step is to create an arraylist of type Enemy
        2. Create a new boolean that detects whether it is a new wave. If it is a new wave then the enemies will be added to the arraylist
        3. after the enemies is added to the arraylist, the boolean variable will be set to false. This then means that after the first iteration, it wouldn't go through the loop again
        4. AFter this, I would add an if statement that checks if the arraylist of type enemy is 0. if the arraylist has a length of 0, then it means there are no enemies there.
        5. If there are no enemies there, then it means it's dead. After activating the if statment, it will call the method addWave.
        6. After calling add wave, the number of waves will increase by 1 and set the boolean variable to true again to indicate the start of a new wave

       - Within the code, I need to make it so after each bullet collsiion with an enemy, I will find the correct enemy that recieved the collision and then do
       something such as enemy.health -= weapon.gunDamagae.
       - At the end of the code, I will add another for loop that loops through each element of the arraylist of type enemy. It will then check each of the enemy's
       health to see if it's greater than 0. if the enemy's health is not greater than 0 meaning it's 0 or less, then it means the enemy is dead. if the enemy is dead
       then i will remove it fromt he arraylist of type enemy.
         */

//        g.drawImage(enemy.enemy1, enemy.xCordE, enemy.yCordE, null);
//        if (enemy.health > 0) {
//            g.drawImage(enemy.getEnemyImage(), enemy.getxCordE(), enemy.getyCordE(), gameFrame.tileSize, gameFrame.tileSize, null);
//        } else {
//            gameFunction.deathCoords(enemy.getxCordE(), enemy.getyCordE());
//            g.drawImage(gameFunction.coin, gameFunction.killedX, gameFunction.killedY, gameFrame.tileSize, gameFrame.tileSize, null);
//            enemy.killEnemy();
//        }



        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getxCordE() < player.getxCoord()) {
                enemies.get(i).faceRight();
                g.drawImage(enemies.get(i).getEnemyImage(), enemies.get(i).getxCordE(), enemies.get(i).getyCordE(), enemies.get(i).getWidth(), gameFrame.tileSize, null);
            } else {
                enemies.get(i).faceLeft();
                g.drawImage(enemies.get(i).getEnemyImage(), enemies.get(i).getxCordE(), enemies.get(i).getyCordE(), enemies.get(i).getWidth(), gameFrame.tileSize, null);
            }
        }




        // Text
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Health: " + player.health + "/" + player.maxHealth, 5,20);

        if (!Weapon.bulletDebounce) {
            weapon.bulletX -= 10;
            g.drawImage(weapon.bullet, weapon.bulletX, weapon.bulletY, null);
        }

        if (player.playerRect().intersects(enemy.enemyRect())) {
            if (!Weapon.debounce && enemy.health > 0) {
                player.health -= enemy.attack;
                System.out.println("Touched");
                sleeperThread.start();
            }
        }

        if (enemy.enemyRect().intersects(weapon.bulletRect())) {
            if (!Weapon.bulletDebounce && enemy.health > 0) {
                enemy.health -= weapon.gunDamage;
                System.out.println("Hit!");
                System.out.println("Enemy Health: " + enemy.health);
                weapon.bulletX = bounds;
                sleeperThread.start();
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).move();
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
                enemy = new Enemy(player);
                int randomX = random.nextInt(maxX - minX) + minX;
                int randomY = random.nextInt(maxY - minY) + minY;
                enemy.setxCordE(randomX);
                enemy.setyCordE(randomY);
                enemies.add(enemy);
                i++;
            }
            numTimes++;
        }
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