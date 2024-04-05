import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Key {
    private int row;
    private int column;
    private boolean collected;
    private BufferedImage image;
    private final String IMAGE_FILE = "sprites/key.png";

    public Key(int row, int column) {
        this.collected = false;
        this.row = row;
        this.column = column;
        image = loadImage(IMAGE_FILE);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected() {
        collected = true;
    }

    public BufferedImage getImage() {
        return image;
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
