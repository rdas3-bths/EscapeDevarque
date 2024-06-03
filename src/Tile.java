import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tile {
    private BufferedImage bigImage;
    private int tileType;
    private final String BIG_FLOOR_IMAGE = "tiles/big_floor_tile.png";
    private final String BIG_WALL_IMAGE = "tiles/big_wall_tile.png";
    private final String BIG_STAIR_IMAGE = "tiles/big_floor_stairs.png";
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

    public void collectItem() {
        this.hasItem = false;
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
            bigImage = loadImage(BIG_FLOOR_IMAGE);
        }
        if (tileType == 1) {
            bigImage = loadImage(BIG_WALL_IMAGE);
        }
        if (tileType == 2) {
            bigImage = loadImage(BIG_STAIR_IMAGE);
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

    public BufferedImage getImage(boolean cheat) {
        return bigImage;
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