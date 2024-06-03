import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Coin extends Item {

    private BufferedImage bigImage;
    private final String BIG_IMAGE_FILE = "sprites/big-coin.png";
    private int value;

    public Coin(int row, int column) {
        super(row, column);
        bigImage = loadImage(BIG_IMAGE_FILE);
        this.value = 1;
    }

    public int getValue() {
        return value;
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
        return bigImage;
    }
}
