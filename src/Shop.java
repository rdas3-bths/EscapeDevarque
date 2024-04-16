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
    private Rectangle repairButton;

    public Shop(int row, int col) {
        this.row = row;
        this.col = col;
        beingVisited = false;
        image = loadImage(IMAGE_FILE);
        repairButton = new Rectangle(995, 530, 139, 35);
    }

    public Rectangle getRepairButton() {
        return repairButton;
    }

    public BufferedImage getImage() {
        return image;
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
