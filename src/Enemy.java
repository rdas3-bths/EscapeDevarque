import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Enemy {
    private BufferedImage image;
    private int row;
    private int column;
    private final String IMAGE_FILE = "sprites/warlock.png";

    public Enemy(int row, int column) {
        this.row = row;
        this.column = column;
        image = loadImage(IMAGE_FILE);
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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String toString() {
        return "Enemy at " + row + "," + column;
    }

    public void moveEnemy(Tile[][] map, int playerRow, int playerColumn) {
        // [ north, south, east, west ]
        boolean[] directions = new boolean[4];
        int rowDifference = Math.abs(row - playerRow);
        int columnDifference = Math.abs(column - playerColumn);

        boolean canSeePlayer = false;
        if (rowDifference <= 3 && columnDifference <= 3) {
            canSeePlayer = true;
        }

        System.out.println("Enemy at: " + row + "," + column + " --> near player: " + canSeePlayer);

        // check north
        try {
            if (map[row-1][column].getTileType() != 1) {
                directions[0] = true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            directions[0] = false;
        }

        // check south
        try {
            if (map[row+1][column].getTileType() != 1) {
                directions[1] = true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            directions[1] = false;
        }

        // check east
        try {
            if (map[row][column+1].getTileType() != 1) {
                directions[2] = true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            directions[2] = false;
        }

        // check west
        try {
            if (map[row][column-1].getTileType() != 1) {
                directions[3] = true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            directions[3] = false;
        }



        int avaiableDirections = 0;
        for (boolean b : directions) {
            if (b)
                avaiableDirections++;
        }

        int[] chooseDirections = new int[avaiableDirections];
        int index = 0;
        for (int i = 0; i < directions.length; i++) {
            if (directions[i]) {
                chooseDirections[index] = i;
                index++;
            }
        }

        int directionPicked = chooseDirections[(int)(Math.random() * chooseDirections.length)];

        // north
        if (directionPicked == 0) {
            row--;
        }
        // south
        if (directionPicked == 1) {
            row++;
        }
        // east
        if (directionPicked == 2) {
            column++;
        }
        // west
        if (directionPicked == 3) {
            column--;
        }

    }


}
