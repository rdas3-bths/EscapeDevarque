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
    private boolean hasItem;
    private boolean visible;
    private boolean hasPlayer;
    private boolean hasEnemy;
    private int row;
    private int column;


    public Tile(int tileType, int row, int column) {
        this.hasItem = false;
        this.tileType = tileType;
        this.hasPlayer = false;
        this.row = row;
        this.column = column;
        this.hasEnemy = false;

        if (tileType == 0 || tileType == 2) {
            mainPath = true;
        }
        else {
            mainPath = false;
        }
        visible = false;
        this.setTileType(tileType);

    }

    public void setPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    public boolean hasPlayer() {
        return hasPlayer;
    }

    public boolean hasItem() {
        return hasItem;
    }

    public void setItem() {
        this.hasItem = true;
    }

    public void setVisible() {
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setEnemy(boolean enemyPresent) {
        this.hasEnemy = enemyPresent;
    }

    public boolean hasEnemy() {
        return hasEnemy;
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
        return row + " " + column;
    }

    public boolean isMainPath() {
        return mainPath;
    }

    public void setMainPath() {
        mainPath = true;
        setTileType(this.tileType);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}