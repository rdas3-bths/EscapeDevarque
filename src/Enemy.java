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

    public boolean moveNorth(Tile[][] map) {
        try {
            if (map[row-1][column].getTileType() != 1) {
                row--;
                return true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    public boolean moveSouth(Tile[][] map) {
        try {
            if (map[row+1][column].getTileType() != 1) {
                row++;
                return true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    public boolean moveEast(Tile[][] map) {
        try {
            if (map[row][column+1].getTileType() != 1) {
                column++;
                return true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    public boolean moveWest(Tile[][] map) {
        try {
            if (map[row][column-1].getTileType() != 1) {
                column--;
                return true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    private void doRandomMove(Tile[][] map) {
        boolean[] directions = new boolean[4];

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

    public void moveEnemy(Tile[][] map, int playerRow, int playerColumn) {
        // [ north, south, east, west ]
        int rowDifference = Math.abs(row - playerRow);
        int columnDifference = Math.abs(column - playerColumn);

        boolean canSeePlayer = false;
        if (rowDifference <= 3 && columnDifference <= 3) {
            canSeePlayer = true;
        }

        if (canSeePlayer) {
            //System.out.print("Enemy at: " + row + "," + column + " ");
            boolean[] playerLocation = getRelativePlayerLocation(row - playerRow, column - playerColumn);
            //System.out.println(Arrays.toString(playerLocation));
            for (int i = 0; i < playerLocation.length; i++) {
                if (playerLocation[i]) {
                    if (i == 0) {
                        boolean moved = moveNorth(map);
                        if (moved)
                            break;
                    }
                    if (i == 1) {
                        boolean moved = moveSouth(map);
                        if (moved)
                            break;
                    }
                    if (i == 2) {
                        boolean moved = moveEast(map);
                        if (moved)
                            break;
                    }
                    if (i == 3) {
                        boolean moved = moveWest(map);
                        if (moved)
                            break;
                    }
                }

            }
        }

        else
            doRandomMove(map);

        map[row][column].setEnemy(true);
    }

    private boolean[] getRelativePlayerLocation(int rowDifference, int columnDifference) {
        // north, south, east, west
        boolean[] playerPosition = new boolean[4];

        if (rowDifference > 0)
            playerPosition[0] = true;
        else if (rowDifference < 0)
            playerPosition[1] = true;

        if (columnDifference < 0) {
            playerPosition[2] = true;
        }
        else if (columnDifference > 0) {
            playerPosition[3] = true;
        }

        System.out.println(rowDifference + " " + columnDifference);
        return playerPosition;
    }


}
