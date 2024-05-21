import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Rectangle;

public class Enemy extends Entity {
    private BufferedImage image;
    private BufferedImage bigImage;
    private final String IMAGE_FILE = "sprites/warlock.png";
    private final String BIG_IMAGE_FILE = "sprites/big-warlock.png";
    private final int MAX_HP = 10;
    private int currentHP;
    private boolean canSeePlayer;

    public Enemy(int row, int column) {
        super(row, column, false);
        image = loadImage(IMAGE_FILE);
        bigImage = loadImage(BIG_IMAGE_FILE);
        currentHP = 10;
        canSeePlayer = false;
    }


    public String healthDisplay() {
        return currentHP + " / " + MAX_HP;
    }

    public int getMaxHP() {
        return MAX_HP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void takeDamage(int damage) {
        currentHP = currentHP - damage;
        if (currentHP <= 0) {
            canSeePlayer = false;
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
        if (cheat)
            return image;
        else
            return bigImage;
    }

    public boolean moveNorth(Tile[][] map, Player p) {
        try {
            if (map[row-1][column].hasPlayer()) {
                int damage = (int)(Math.random()*2) + 1;
                p.takeDamage(damage);
                return true;
            }
            if (map[row-1][column].getTileType() != 1 && !map[row-1][column].hasEnemy()) {
                row--;
                return true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    public boolean moveSouth(Tile[][] map, Player p) {
        try {
            if (map[row+1][column].hasPlayer()) {
                int damage = (int)(Math.random()*2) + 1;
                p.takeDamage(damage);
                return true;
            }
            if (map[row+1][column].getTileType() != 1 && !map[row+1][column].hasEnemy()) {
                row++;
                return true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    public boolean moveEast(Tile[][] map, Player p) {
        try {
            if (map[row][column+1].hasPlayer()) {
                int damage = (int)(Math.random()*2) + 1;
                p.takeDamage(damage);
                return true;
            }
            if (map[row][column+1].getTileType() != 1 && !map[row][column+1].hasEnemy()) {
                column++;
                return true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    public boolean moveWest(Tile[][] map, Player p) {
        try {
            if (map[row][column-1].hasPlayer()) {
                int damage = (int)(Math.random()*2) + 1;
                p.takeDamage(damage);
                return true;
            }
            if (map[row][column-1].getTileType() != 1 && !map[row][column-1].hasEnemy()) {
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
            if (map[row-1][column].getTileType() != 1 && !map[row-1][column].hasEnemy()) {
                directions[0] = true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            directions[0] = false;
        }

        // check south
        try {
            if (map[row+1][column].getTileType() != 1 && !map[row+1][column].hasEnemy()) {
                directions[1] = true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            directions[1] = false;
        }

        // check east
        try {
            if (map[row][column+1].getTileType() != 1 && !map[row][column+1].hasEnemy()) {
                directions[2] = true;
            }
        }
        catch (IndexOutOfBoundsException e) {
            directions[2] = false;
        }

        // check west
        try {
            if (map[row][column-1].getTileType() != 1 && !map[row][column-1].hasEnemy()) {
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

        if (avaiableDirections != 0) {
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

    public void moveEnemy(Tile[][] map, Player p) {
        int playerRow = p.getRow();
        int playerColumn = p.getColumn();
        map[row][column].setEnemy(false);
        // [ north, south, east, west ]
        int rowDifference = Math.abs(row - playerRow);
        int columnDifference = Math.abs(column - playerColumn);

        canSeePlayer = false;
        if (rowDifference <= 3 && columnDifference <= 3) {
            canSeePlayer = true;
        }

        if (canSeePlayer) {
            boolean[] playerLocation = getRelativePlayerLocation(row - playerRow, column - playerColumn);
            for (int i = 0; i < playerLocation.length; i++) {
                if (playerLocation[i]) {
                    if (i == 0) {
                        boolean moved = moveNorth(map, p);
                        if (moved)
                            break;
                    }
                    if (i == 1) {
                        boolean moved = moveSouth(map, p);
                        if (moved)
                            break;
                    }
                    if (i == 2) {
                        boolean moved = moveEast(map, p);
                        if (moved)
                            break;
                    }
                    if (i == 3) {
                        boolean moved = moveWest(map, p);
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

    public boolean getCanSeePlayer() {
        return canSeePlayer;
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

        return playerPosition;
    }


}
