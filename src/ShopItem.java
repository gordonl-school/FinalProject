public class ShopItem {
    String name;
    String description;
    int cost;
    double upgradeValue;
    boolean purchased;

    public ShopItem(String name, String description, int cost, double upgradeValue) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.upgradeValue = upgradeValue;
        this.purchased = false;
    }
}
