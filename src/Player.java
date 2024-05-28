import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    private BufferedImage image;
    private BufferedImage bigImage;
    private final String IMAGE_FILE = "sprites/naix-head.png";
    private final String BIG_IMAGE_FILE = "sprites/lifestealer.png";
    private final String BIG_IMAGE_FRAMES = "sprites/knight";
    private ArrayList<BufferedImage> playerFrames;
    private int pickAxeDurability;
    private int gold;
    private final int MAX_HP = 50;
    private int currentHP;
    private int currentFrame;

    public Player(int row, int column) {
        super(row, column, true);
        playerFrames = new ArrayList<>();
        image = loadImage(IMAGE_FILE);
        bigImage = loadImage(BIG_IMAGE_FILE);
        for (int i = 1; i < 5; i++) {
            String file = BIG_IMAGE_FRAMES + "_" + i + ".png";
            loadFrames(file);
        }
        pickAxeDurability = 10;
        gold = 0;
        currentHP = 50;
        currentFrame = 0;
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
            e.printStackTrace();
            return null;
        }
    }

    public void loadFrames(String fileName) {
        playerFrames.add(loadImage(fileName));
    }

    public BufferedImage getImage(boolean cheat) {

        if (cheat)
            return image;
        else
            return playerFrames.get(currentFrame);
    }

    public void nextFrame() {
        currentFrame++;
        if (currentFrame == playerFrames.size()) {
            currentFrame = 0;
        }
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
