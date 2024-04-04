import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tile {
    private BufferedImage image;
    private int tileType;
    private final String FLOOR_IMAGE = "tiles/floor.png";
    private final String WALL_IMAGE = "tiles/wall.png";
    private final String START_FLOOR = "tiles/start_floor.png";
    private boolean mainPath;

    public Tile(int tileType) {
        this.tileType = tileType;
        this.setTileType(tileType);
        if (tileType == 0 || tileType == 2) {
            mainPath = true;
        }
        else {
            mainPath = false;
        }

    }

    public void setTileType(int tileType) {
        this.tileType = tileType;
        if (tileType == 0) {
            image = loadImage(FLOOR_IMAGE);
        }
        if (tileType == 1) {
            image = loadImage(WALL_IMAGE);
        }
        if (tileType == 2) {
            image = loadImage(START_FLOOR);
        }
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

    public int getTileType() {
        return tileType;
    }

    public String toString() {
        return tileType + " " + image;
    }

    public boolean isMainPath() {
        return mainPath;
    }
}