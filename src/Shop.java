import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Shop {
    private ArrayList<ShopItem> currentShopItems;
    private GamePanel gamePanel;
    private Player player;
    private Weapon weapon;

    public Shop(GamePanel gamePanel, Player player, Weapon weapon) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.weapon = weapon;
        currentShopItems = new ArrayList<>();
    }


    public void generateShopItems() {
        currentShopItems.clear();

        ShopItem healthBoost = new ShopItem("Health Boost", "+60 Health", 5, 60);
        ShopItem maxHealthBoost = new ShopItem("Max Health Boost", "+50 Max Health", 10, 50);
        ShopItem damageUpgrade = new ShopItem("Damage Upgrade", "+10 Damage", 8, 10);
        ShopItem speedBoost = new ShopItem("Speed Boost", "+1 Movement Speed", 10, 1);
        ShopItem defense = new ShopItem("Defense Boost", "+5 Defense", 8, 5);
        ShopItem fireRate = new ShopItem("Fire Rate Boost", "+.02 Fire Rate", 12, 20);
        ShopItem bulletSpeed = new ShopItem("Bullet Speed Boost", "+1 Bullet Speed", 18, 1);
        ShopItem gemMultiplier = new ShopItem("Gem Multiplier", "20% Extra Gems", 20, 1.2);

        for (int i = 0; i < 3; i++) {
            Random rng = new Random();
            int roll = rng.nextInt(8) + 1; // 1 to 7 inclusive

            if (roll == 1) {
                if (currentShopItems.contains(healthBoost)) {
                    i--;
                    continue;
                }
                currentShopItems.add(healthBoost);
            }
            if (roll == 2) {
                if (currentShopItems.contains(maxHealthBoost)) {
                    i--;
                    continue;
                }
                currentShopItems.add(maxHealthBoost);
            }
            if (roll == 3) {
                if (currentShopItems.contains(damageUpgrade)) {
                    i--;
                    continue;
                }
                currentShopItems.add(damageUpgrade);
            }
            if (roll == 4) {
                if (currentShopItems.contains(speedBoost)) {
                    i--;
                    continue;
                }
                currentShopItems.add(speedBoost);
            }
            if (roll == 5) {
                if (currentShopItems.contains(defense)) {
                    i--;
                    continue;
                }
                currentShopItems.add(defense);
            }
            if (roll == 6) {
                if (currentShopItems.contains(fireRate)) {
                    i--;
                    continue;
                }
                currentShopItems.add(fireRate);
            }
            if (roll == 7) {
                if (currentShopItems.contains(bulletSpeed)) {
                    i--;
                    continue;
                }
                currentShopItems.add(bulletSpeed);
            }
            if (roll == 8) {
                if (currentShopItems.contains(gemMultiplier)) {
                    i--;
                    continue;
                }
                currentShopItems.add(gemMultiplier);
            }
        }
    }

    public void drawShop(Graphics g, double gems, JButton[] shopButtons) {
        // Shop Button
        g.setFont(new Font("Times New Roman", Font.BOLD, 50));
        g.setColor(Color.YELLOW);
        g.drawString("SHOP", 250, 220);

        // The 3 rectangles
        g.setColor(new Color(30, 30, 50, 180));
        g.fillRect(20, 250, 180, 300);
        g.fillRect(220, 250, 180, 300);
        g.fillRect(420, 250, 180, 300);

        // Name
        g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        g.setColor(Color.WHITE);
        g.drawString(currentShopItems.getFirst().name, 25, 280);
        g.drawString(currentShopItems.get(1).name, 225, 280);
        g.drawString(currentShopItems.get(2).name, 425, 280);

        // Item Description
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        g.setColor(Color.cyan);
        g.drawString(currentShopItems.getFirst().description, 45, 380);
        g.drawString(currentShopItems.get(1).description, 245, 380);
        g.drawString(currentShopItems.get(2).description, 445, 380);

        // Price
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.setColor(Color.YELLOW);
        g.drawString(("Cost " + currentShopItems.getFirst().cost + " Gems"), 60, 440);
        g.drawString(("Cost " + currentShopItems.get(1).cost + " Gems"), 260, 440);
        g.drawString(("Cost " + currentShopItems.get(2).cost + " Gems"), 460, 440);

        for (int i = 0; i < currentShopItems.size(); i++) {
            ShopItem item = currentShopItems.get(i);
            JButton button = shopButtons[i];
            button.setSize(140,70);
            button.setLocation(40 + (i * 200),450);

            button.setFont(new Font("Arial", Font.BOLD, 14));
            if (item.purchased) {
                button.setText("SOLD OUT");
                button.setBackground(new Color(169, 169, 169));
            } else if (gems < item.cost){
                button.setBackground(new Color(220, 20, 60));
                button.setText("NOT ENOUGH");
            } else {
                button.setBackground(new Color(34, 139, 34));
                button.setText("BUY");
            }
            button.setEnabled(!item.purchased);

        }
    }

    public boolean handlePurchase(int itemIndex, double gems) {
        ShopItem item = currentShopItems.get(itemIndex);
        if (!item.purchased && gems >= item.cost) {
            item.purchased = true;
            gamePanel.gems -= item.cost;
            applyUpgrade(item);
        }
        return item.purchased;
    }


    public void applyUpgrade(ShopItem item) {
        switch (item.name) {
            case "Health Boost":
                player.health += (int) item.upgradeValue;
                if (player.health > player.maxHealth) {
                    player.health = player.maxHealth;
                }
                break;
            case "Max Health Boost":
                player.maxHealth += (int) item.upgradeValue;
                player.health += (int) item.upgradeValue;
                break;
            case "Damage Upgrade":
                weapon.gunDamage += (int) item.upgradeValue;
                break;
            case "Speed Boost":
                player.setMOVE_AMOUNT(gamePanel.movementSpeed += (int) item.upgradeValue);
                break;
            case "Defense Boost":
                gamePanel.defense += (int) item.upgradeValue;
                break;
            case "Fire Rate Boost":
                gamePanel.fireRate -= (int) item.upgradeValue;
                if (gamePanel.fireRate < 100) {
                    gamePanel.fireRate = 100;
                }
                break;
            case "Bullet Speed Boost":
                gamePanel.bulletSpeed += item.upgradeValue;
                break;
            case "Gem Multiplier":
                gamePanel.gemMultiplier *= item.upgradeValue;
                break;
        }
    }





}
