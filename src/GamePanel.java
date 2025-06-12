import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
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

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener {
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
    private Shop shop;
    private AnimationController animationController;

    ArrayList<Enemy> enemies;
    private ArrayList<BufferedImage> bullets;
    private ArrayList<Double> bulletX;
    private ArrayList<Double> bulletY;
    private ArrayList<Double> bulletVX;
    private ArrayList<Double> bulletVY;
    private ArrayList<GameFunction> coins;


    private static boolean timerPulse = false;
    private boolean gameGoing;
    private static int numTimes = 1;
    int waves = 1;
    private JButton start;
    private boolean started = false;
    private boolean newWave = true;
    boolean mousePressed;


    int bounds;

    int mouseMotionX;
    int mouseMotionY;
    AffineTransform transform;

    private GameState currentState;

    // Shop System
    private JButton continueButton;
    private JButton rerollButton;

    private JButton shopButton1;
    private JButton shopButton2;
    private JButton shopButton3;
    private JButton[] shopButtons;

    private Timer waveTimer;
    private int waveTimerPause;
    private boolean waveBreak;
    private int enemyTrack = 0;
    private int enemiesSpawned = 0;
    private long lastShotTime = 0;
    int rerollAmount;
    int rerollPrice;


    // Shop Buffs
    double gems;
    int defense;
    int fireRate;
    int movementSpeed;
    double bulletSpeed;
    double gemMultiplier;




    public GamePanel(GameFrame gameFrame) {
        start = new JButton("Play!");
        start.addActionListener(this);
        add(start);

        continueButton = new JButton("Continue");
        continueButton.addActionListener(this);
        rerollButton = new JButton("Reroll");
        rerollButton.addActionListener(this);

        shopButton1 = new JButton("Button 1");
        shopButton1.addActionListener(this);
        shopButton2 = new JButton("Button 2");
        shopButton2.addActionListener(this);
        shopButton3 = new JButton("Button 3");
        shopButton3.addActionListener(this);

        add(continueButton);
        add(rerollButton);

        add(shopButton1);
        add(shopButton2);
        add(shopButton3);

        shopButtons = new JButton[3];
        shopButtons[0] = shopButton1;
        shopButtons[1] = shopButton2;
        shopButtons[2] = shopButton3;


        // Make use of the timer
        animationController = new AnimationController(50);
        timer = new Timer(16, this);
        enemyTimer = new Timer((int)((1.0 - (waves - 1) * (0.75 / 19.0)) * .7 * 1000), this); // Spawns enemies every 1s but down to every .25s at wave 20 (30% faster now)
        waveTimer = new Timer(1000, this);

        timer.start();
        enemyTimer.start();
//        enemyTimer = new Timer(10, this);
//        enemyTimer.start();

        // Variables
        keyPressed = new boolean[128];

        // Implements
        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);

        // Other Classes
        checker = new CollisionChecker(this);
        weapon = new Weapon();
        gameGoing = true;
        player = new Player(gameFrame, this, enemy, weapon, animationController);
        tileM = new TileManager(this, gameFrame, player, enemy);
        enemy = new Enemy(player, animationController, 100, 5, 2);
        shop = new Shop(this, player, weapon);
        gameFunction = new GameFunction(enemy, this);

        bullets = new ArrayList<>();
        bulletX = new ArrayList<>();
        bulletY = new ArrayList<>();
        bulletVX = new ArrayList<>();
        bulletVY = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<Enemy>();

        enemies.add(enemy);

        this.gameFrame = gameFrame;

        setFocusable(true);
        requestFocusInWindow();

        bounds = 999999;
        mouseMotionX = 0;
        mouseMotionY = 0;
        gems = 30;
        rerollAmount = 0;
        rerollPrice = 3;

        transform = new AffineTransform();
        mousePressed = false;

        currentState = GameState.MENU;
        waveTimerPause = 60;
        waveBreak = false;

        // Player stats
        defense = 1;
        movementSpeed = player.getMOVE_AMOUNT();
        fireRate = 350;
        bulletSpeed = 8.0;
        gemMultiplier = 1.0;

        playMusic();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        removeAll(); // Removes everything from screen to make usre nothing stays
        switch (currentState) {
            case MENU:
                drawMenu(g);
                add(start);
                break;
            case PLAYING:
                if (player.getHealth() <= 0) {
                    currentState = GameState.GAME_OVER;
                } else {
                    drawGame(g, g2);
                }
                break;
            case WAVE_BREAK:
                drawWaveBreak(g, g2);
                add(continueButton);
                add(rerollButton);
                add(shopButton1);
                add(shopButton2);
                add(shopButton3);
                break;
            case GAME_OVER:
                if (numTimes == 21) {
                    drawEndScreen(g, g2, 1);
                } else {
                    drawEndScreen(g, g2, 0);
                }
                break;
        }
    }

    private void drawGame(Graphics g, Graphics2D g2) {
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

        for (int i = 0; i < coins.size(); i++) {
            GameFunction currentCoin = coins.get(i);
            g.drawImage(gameFunction.coin, currentCoin.coinCoordX, currentCoin.coinCoordY, null);
            if (player.playerRect().intersects(currentCoin.coinRect())) {
                gems += (gemMultiplier);
                coins.remove(i);
                i--;
            }
        }


        // Text
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Health: " + player.health + "/" + player.maxHealth, 5, 20);
        g.drawString("Enemies: " + enemies.size(), 5, 50);
//        g.drawString("Next Wave's Enemies: " + (int)(1.947 * (numTimes + 1) + 1.05), 5, 80);

        g.drawString("Wave " + numTimes + "/20", 650, 20);
        g.drawString("Gems: " + Math.round(gems * 10.0) / 10.0, 350, 20);

        if (mousePressed && System.currentTimeMillis() - lastShotTime >= fireRate) {

            // Store when the last time shot was
            lastShotTime = System.currentTimeMillis();

            // This is to calculate the velocity
            double startX = player.getxCoord() + gameFrame.tileSize / 2.0;
            double startY = player.getyCoord() + gameFrame.tileSize / 2.0;

            double dx = mouseMotionX - startX;
            double dy = mouseMotionY - startY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                double vx = (dx / distance) * bulletSpeed;
                double vy = (dy / distance) * bulletSpeed;

                // Store things for new bullet
                bullets.add(weapon.bullet);
                bulletX.add(startX);
                bulletY.add(startY);
                bulletVX.add(vx);
                bulletVY.add(vy);

//                Thread newSleeperThread = new Thread(new Sleeper(fireRate));
//                newSleeperThread.start();
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

        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy currentEnemy = enemies.get(i);
            if (player.playerRect().intersects(currentEnemy.enemyRect())) {
                if (!Enemy.attackDebounce && currentEnemy.getHealth() > 0) {
                    // CALCULATE DEFENSE
                    double damageReduction = (defense - 1) * 0.01; // 1% damage reduction for every defense
                    double damageMultiplier = 1.0 - damageReduction; // Reduction multi
                    int finalDamage = (int) (currentEnemy.attack * damageMultiplier);
                    if (finalDamage < 1) {
                        finalDamage = 1;
                    }
                    player.health -= finalDamage;

                    Thread sleeperThread = new Thread(new Sleeper(1000));
                    sleeperThread.start();
                }
            }
        }
        // Key interactions
        if (currentState == GameState.PLAYING) {

            boolean movingDiagonally = (keyPressed[KeyEvent.VK_A] || keyPressed[KeyEvent.VK_D]) && (keyPressed[KeyEvent.VK_W] || keyPressed[KeyEvent.VK_S]);

            if (movingDiagonally) {
                player.setMOVE_AMOUNT((int) (movementSpeed * 0.707));
            } else {
                player.setMOVE_AMOUNT(movementSpeed);
            }
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
        }
        checker.checkTile(player);
    }
    private void drawWaveBreak(Graphics g, Graphics2D g2) {

        // GAME Background (irrelevant, for looks)
        tileM.draw(g2);
        g2.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), gameFrame.tileSize, gameFrame.tileSize, null);
        g.drawImage(weapon.gun, weapon.getGunCoordX(), weapon.getGunCoordY(), null);
        for (int i = 0; i < coins.size(); i++) {
            GameFunction currentCoin = coins.get(i);
            g.drawImage(gameFunction.coin, currentCoin.coinCoordX, currentCoin.coinCoordY, null);
        }

        // THE ACTUAL SHOP BACKGROUND
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0,0, gameFrame.screenWidth, gameFrame.screenHeight);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", Font.BOLD, 48));
        g.drawString("Wave " + numTimes + " Complete!", 200, 60);

        g.setFont(new Font("Times New Roman", Font.BOLD, 32));
        g.setColor(Color.GREEN);
        g.drawString("Gems: " + Math.round(gems * 10.0) / 10.0 ,610, 300);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g.drawString("Next wave in " + waveTimerPause + " seconds", 280, 90);
        g.drawString("Health: " + player.health, 610, 350);
        g.drawString("Max Health: " + player.maxHealth, 610, 370);
        g.drawString("Damage: " + weapon.gunDamage, 610, 390);
        g.drawString("Player Speed: " + movementSpeed, 610, 410);
        g.drawString("Defense: " + defense, 610, 430);
        g.drawString("Fire Rate: " + fireRate / 1000.0, 610, 450);
        g.drawString("Bullet Speed: " + bulletSpeed, 610, 470);
        g.drawString("Gem Multi: " + Math.round(gemMultiplier * 100.0) / 100.0, 610, 490);

        g.drawString("Enemies Health: " + enemy.trackerHealth, 560, 580);
        g.drawString("Enemies Attack: " + enemy.attack, 560, 610);
        g.drawString("Enemies Movement: " + enemy.getSPEED(), 560, 640);

        if (rerollAmount == 0) {
            g.drawString("Reroll Price: FREE", 30, 590);
        } else {
            g.drawString("Reroll Price: " + rerollPrice, 30, 590);
        }

        shop.drawShop(g, gems, shopButtons);

        continueButton.setSize(200, 50);
        rerollButton.setSize(150, 50);

        rerollButton.setLocation(25, 600);
        continueButton.setLocation(300, 660);
    }
    private void drawMenu(Graphics g) {
        g.setFont(new Font("EB Garamond", Font.BOLD, 44));
        g.drawString("Welcome to GD's Mow Down!", 85, 300);
        g.setFont(new Font("EB Garamond", Font.BOLD, 14));
        g.drawString("WASD to move, mouse click to shoot at enemies. Good luck!", 200, 350);
        start.setSize(300, 100);
        start.setFont(new Font("EB Garamond", Font.BOLD, 64));
        start.setLocation(250, 375);
    }
    private void drawGameBG(Graphics g, Graphics2D g2) {
        tileM.draw(g2);
        g2.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), gameFrame.tileSize, gameFrame.tileSize, null);
    }
    private void drawEndScreen(Graphics g, Graphics2D g2, int scenario) {
        if (scenario == 1) {
            // GAME Background (irrelevant, for looks)
            tileM.draw(g2);
            g2.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), gameFrame.tileSize, gameFrame.tileSize, null);
            g.drawImage(weapon.gun, weapon.getGunCoordX(), weapon.getGunCoordY(), null);
            for (int i = 0; i < coins.size(); i++) {
                GameFunction currentCoin = coins.get(i);
                g.drawImage(gameFunction.coin, currentCoin.coinCoordX, currentCoin.coinCoordY, null);
            }
            g.setColor(new Color(34,139,34, 80));
            g.fillRect(0,0, gameFrame.screenWidth, gameFrame.screenHeight);
            g.setFont(new Font("Times New Roman", Font.BOLD, 36));
            g.drawString("You win!", 275, 350);
        } else {
            // GAME Background (irrelevant, for looks)
            tileM.draw(g2);
            g2.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), gameFrame.tileSize, gameFrame.tileSize, null);
            g.drawImage(weapon.gun, weapon.getGunCoordX(), weapon.getGunCoordY(), null);
            for (int i = 0; i < coins.size(); i++) {
                GameFunction currentCoin = coins.get(i);
                g.drawImage(gameFunction.coin, currentCoin.coinCoordX, currentCoin.coinCoordY, null);
            }
            g.setColor(new Color(220,20,60, 80));
            g.fillRect(0,0, gameFrame.screenWidth, gameFrame.screenHeight);
            g.setFont(new Font("Times New Roman", Font.BOLD, 36));
            g.drawString("Game Over!", 275, 350);
        }
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        Object sender = e.getSource();
        if (sender == timer) {
            repaint();
        }
//        if (sender == enemyTimer && currentState == GameState.PLAYING) {
//            if (newWave) {
//                enemyTrack = (int)(1.947 * numTimes + 1.05);
//                enemiesSpawned = 0;
//                newWave = false;
//            }
//
//            if (enemiesSpawned < enemyTrack) {
//=                enemiesSpawned++;
//
//                if (enemiesSpawned >= enemyTrack) {
//                    enemyTimer.stop();
//                }
//            }
//        }

        if (sender == enemyTimer && currentState == GameState.PLAYING) {
            if (newWave) {
//                enemyTrack = (int)(1.947 * numTimes + 1.05);
                switch (numTimes) {
                    case 1:
                        enemyTrack = 5;
                        break;
                    case 2:
                        enemyTrack = 7;
                        break;
                    case 3:
                        enemyTrack = 10;
                        break;
                    case 4:
                        enemyTrack = 8;
                        break;
                    case 5:
                        enemyTrack = 13;
                        break;
                    case 6:
                        enemyTrack = 20;
                        break;
                    case 7:
                        enemyTrack = 14;
                        break;
                    case 8:
                        enemyTrack = 18;
                        break;
                    case 9:
                        enemyTrack = 22;
                        break;
                    case 10:
                        enemyTrack = 24;
                        break;
                    case 11:
                        enemyTrack = 28;
                        break;
                    case 12:
                        enemyTrack = 15;
                        break;
                    case 13:
                        enemyTrack = 37;
                        break;
                    case 14:
                        enemyTrack = 25;
                        break;
                    case 15:
                        enemyTrack = 30;
                        break;
                    case 16:
                        enemyTrack = 26;
                        break;
                    case 17:
                        enemyTrack = 43;
                        break;
                    case 18:
                        enemyTrack = 31;
                        break;
                    case 19:
                        enemyTrack = 60;
                        break;
                    case 20:
                        enemyTrack = 73;
                        break;
                    default:
                        enemyTrack = (int) (1.947 * numTimes + 1.05);
                        break;
                }
                enemiesSpawned = 0;
                newWave = false;
            }
            if (enemiesSpawned < enemyTrack) {
                int minX = 0;
                int maxX = 16 * 48;
                int minY = 0;
                int maxY = 48 * 17;
                int i = 0;
                int attempts = 0;
                Random random = new Random();

                int waves = numTimes;
                double health = 100 + (300 - 100) * ((waves - 1) / 19.0); // This will go from 100 health starting to 300 health by wave 20 (I think?)
                double attack = 5 + (30 - 5) * ((waves - 1) / 19.0); // This will go from 5 attack starting to 30 attack by wave 20 (I think?)
                int movement = 2;
                if (waves >= 7) {
                    movement = 3;
                }
                if (waves >= 14) {
                    movement = 4;
                }
                if (waves >= 18) {
                    movement = 5;
                }
                if (waves == 20) {
                    movement = 6;
                }

//            while (i < (int)(1.947 * numTimes + 1.05) && newWave && attempts < 100) {
//                enemy = new Enemy(player, animationController, (int) health, (int) attack, movement);
//                int randomX = random.nextInt(maxX - minX) + minX;
//                int randomY = random.nextInt(maxY - minY) + minY;
//                attempts++;
//
//                enemy.setxCordE(randomX);
//                enemy.setyCordE(randomY);
//                enemies.add(enemy);
//                i++;
//            }
                enemy = new Enemy(player, animationController, (int) health, (int) attack, movement);
                int randomX = random.nextInt(maxX - minX) + minX;
                int randomY = random.nextInt(maxY - minY) + minY;
                while (Math.abs(randomX - player.getxCoord()) < 150 || Math.abs(randomY - player.getyCoord()) < 150) {
                    randomX = random.nextInt(maxX - minX) + minX;
                    randomY = random.nextInt(maxY - minY) + minY;
                }
                enemy.setxCordE(randomX);
                enemy.setyCordE(randomY);
                enemies.add(enemy);
                enemiesSpawned++;
//            newWave = false;
                if (enemies.size() >= enemyTrack) {
                    enemyTimer.stop();
                }
            }
        }

        if (numTimes >= 21) {
            waveTimer.stop();
            enemyTimer.stop();
            newWave = false;
            currentState = GameState.GAME_OVER;
        }

        if (sender == start) {
            started = true;
            currentState = GameState.PLAYING;
            newWave = true;
            enemyTimer.start();
        }

        if (sender == continueButton) {
            waveTimer.stop();
            waveTimerPause = 60;
            currentState = GameState.PLAYING;
            newWave = true;
            enemyTimer.start();
            waves++; // Tracker
            gameFunction.updateEnemy();
//            numTimes++; // Need one for this if the user presses continue
            enemiesSpawned = 0;
            rerollAmount = 0;
            rerollPrice = 3 + (waves - 1) / 2;
        }

        if (sender == waveTimer && currentState == GameState.WAVE_BREAK) {
            waveTimerPause--;
            if (waveTimerPause <= 0) {
                waveTimer.stop();
                waveTimerPause = 60;
                currentState = GameState.PLAYING;
                newWave = true;
                enemyTimer.start();
//                numTimes++; // Need one for this if user waits till time runs out
                waves++; // Tracker
                gameFunction.updateEnemy();
                rerollAmount = 0;
                enemiesSpawned = 0;
                rerollPrice = 3 + (waves - 1) / 2;
            }
        }

        if (currentState == GameState.PLAYING && enemies != null && enemies.isEmpty() && !newWave && enemiesSpawned >= enemyTrack) {
            numTimes++; // Need one for this if the user presses continue
            if (numTimes >= 21) {
                waveTimer.stop();
                enemyTimer.stop();
                newWave = false;
                currentState = GameState.GAME_OVER;
            } else {
                currentState = GameState.WAVE_BREAK;
                waveTimerPause = 60;
                shop.generateShopItems();
                assert waveTimer != null;
                waveTimer.start();
            }
        }

        if (sender == rerollButton) {
            if (rerollAmount == 0) {
                shop.generateShopItems();
                rerollAmount += 1;
            } else if (rerollPrice < gems) {
                shop.generateShopItems();
                gems -= rerollPrice;
                rerollAmount += 1;
                rerollPrice = (int)(rerollPrice * Math.pow(1.3, rerollAmount - 1));
            }
        }

        if (sender == shopButton1) {
            boolean bought = shop.handlePurchase(0, gems);
            if (bought) {
                shopButton1.setEnabled(false);
            }
        }
        if (sender == shopButton2) {
            boolean bought = shop.handlePurchase(1, gems);
            if (bought) {
                shopButton2.setEnabled(false);
            }
        }
        if (sender == shopButton3) {
            boolean bought = shop.handlePurchase(2, gems);
            if (bought) {
                shopButton3.setEnabled(false);
            }
        }

    }

    private void checkBulletCollisions() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            double bX = bulletX.get(i);
            double bY = bulletY.get(i);

            // NEED TO CREATE A NEW RECTANGLE HREE BECAUSE IT HAS DIFFERENT COORDINATES THAN THE ONE IN WEAPON
            Rectangle bulletRect = new Rectangle((int) bX, (int) bY, bullets.get(i).getWidth(), bullets.get(i).getHeight());

            // Loop enemies
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy currentEnemy = enemies.get(j);
                if (bulletRect.intersects(currentEnemy.enemyRect())) {
                    currentEnemy.setHealth(currentEnemy.getHealth() - weapon.gunDamage);
//                    System.out.println("Hit\nEnemy Health: " + currentEnemy.getHealth());
                    bullets.remove(i);
                    bulletX.remove(i);
                    bulletY.remove(i);
                    bulletVX.remove(i);
                    bulletVY.remove(i);

                    if (currentEnemy.getHealth() <= 0) {
//                        System.out.println("Enemy dead");
                        GameFunction coin = new GameFunction(enemy, this);
                        coin.setCoinCoordX(currentEnemy.xCordE);
                        coin.setCoinCoordY(currentEnemy.yCordE);
                        coins.add(coin);
                        enemies.remove(j);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void keyTyped (KeyEvent e){}

    @Override
    public void keyPressed (KeyEvent e){
        keyPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased (KeyEvent e){
        keyPressed[e.getKeyCode()] = false;

        if (!keyPressed[KeyEvent.VK_W] && !keyPressed[KeyEvent.VK_A] && !keyPressed[KeyEvent.VK_S] && !keyPressed[KeyEvent.VK_D]) {
            player.stopMoving();
        }
    }

    // MOUSE MOTION METHODS
    @Override
    public void mouseDragged (MouseEvent e){
        mouseMotionX = e.getX();
        mouseMotionY = e.getY();
    }

    @Override
    public void mouseMoved (MouseEvent e){
        mouseMotionX = e.getX();
        mouseMotionY = e.getY();
    }

    @Override
    public void mouseClicked (MouseEvent e){}

    @Override
    public void mousePressed (MouseEvent e){
        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased (MouseEvent e){
        if (e.getButton() == MouseEvent.BUTTON1) {
            mousePressed = false;
        }
    }

    @Override
    public void mouseEntered (MouseEvent e){}

    @Override
    public void mouseExited (MouseEvent e){}

    private void playMusic() {
        File audioFile = new File("src/SwitchWithMeTheme.wav");
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // repeats song
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}