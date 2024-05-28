import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Shop {
    private int row;
    private int col;
    private boolean beingVisited;
    private BufferedImage image;
    private final String IMAGE_FILE = "tiles/shop.png";
    private BufferedImage bigImage;
    private final String BIG_IMAGE_FILE = "tiles/big-shop.png";
    private Rectangle repairButton;
    private Rectangle healButton;
    private Rectangle damageButton;

    public Shop(int row, int col) {
        this.row = row;
        this.col = col;
        beingVisited = false;
        image = loadImage(IMAGE_FILE);
        bigImage = loadImage(BIG_IMAGE_FILE);
        repairButton = new Rectangle(995, 530, 180, 35);
        healButton = new Rectangle(995, 580, 180, 35);
        damageButton = new Rectangle(995, 630, 180, 35);
    }

    public Rectangle getHealButton() { return healButton; }

    public Rectangle getRepairButton() {
        return repairButton;
    }

    public Rectangle getDamageButton() { return damageButton; }

    public BufferedImage getImage(boolean cheat) {
        if (cheat)
            return image;
        else
            return bigImage;
    }

    public void setVisited(boolean visited) {
        this.beingVisited = visited;
    }

    public boolean getBeingVisited() {
        return beingVisited;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
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
}
