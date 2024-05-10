import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private BufferedImage image;
    private int row;
    private int column;
    private final String IMAGE_FILE = "sprites/naix-head.png";
    private int pickAxeDurability;
    private int gold;
    private final int MAX_HP = 50;
    private int minDamage;
    private int maxDamage;
    private int currentHP;

    public Player(int row, int column) {
        this.row = row;
        this.column = column;
        image = loadImage(IMAGE_FILE);
        pickAxeDurability = 10;
        gold = 0;
        currentHP = 50;
        minDamage = 1;
        maxDamage = 2;
    }

    private int getMinDamage() {
        return minDamage;
    }

    private int getMaxDamage() {
        return maxDamage;
    }

    public String damageDisplay() {
        return minDamage + "-" + maxDamage;
    }

    public String healthDisplay() {
        return currentHP + " / " + MAX_HP;
    }

    public int getMaxHP() {
        return MAX_HP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void takeDamage(int damage) {
        currentHP = currentHP - damage;
    }

    public int getGold() {
        return gold;
    }

    public void usePickAxe() {
        pickAxeDurability--;
    }

    public int getPickAxeDurability() {
        return pickAxeDurability;
    }

    public void repairPickAxe() {
        pickAxeDurability = 10;
    }

    public BufferedImage loadImage(String fileName) {
        try {
            BufferedImage image;
            image = ImageIO.read(new File(fileName));
            return image;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String toString() {
        return "Player at " + row + "," + column;
    }

    public void collectGold(int value) {
        gold += value;
    }

    public void spendGold(int value) {
        gold -= value;
    }

}
