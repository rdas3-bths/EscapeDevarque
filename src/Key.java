import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Key extends Item {

    private BufferedImage bigImage;
    private final String BIG_IMAGE_FILE = "sprites/big-key.png";

    public Key(int row, int column) {
        super(row, column);
        bigImage = loadImage(BIG_IMAGE_FILE);
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
