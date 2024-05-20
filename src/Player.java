import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {
    private BufferedImage image;
    private BufferedImage bigImage;
    private final String IMAGE_FILE = "sprites/naix-head.png";
    private final String BIG_IMAGE_FILE = "sprites/lifestealer.png";
    private int pickAxeDurability;
    private int gold;
    private final int MAX_HP = 50;
    private int currentHP;

    public Player(int row, int column) {
        super(row, column);
        image = loadImage(IMAGE_FILE);
        bigImage = loadImage(BIG_IMAGE_FILE);
        pickAxeDurability = 10;
        gold = 0;
        currentHP = 50;
    }

    public void heal(int hp) {
        currentHP += hp;
        if (currentHP > MAX_HP) {
            currentHP = MAX_HP;
        }
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

    public BufferedImage getImage(boolean cheat) {

        if (cheat)
            return image;
        else
            return bigImage;
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
