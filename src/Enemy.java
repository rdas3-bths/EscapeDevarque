import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Enemy extends Entity {
    private BufferedImage bigImage;
    private final String BIG_IMAGE_FILE = "sprites/big-warlock.png";
    private final String BIG_IMAGE_FRAMES = "sprites/skeleton";
    private ArrayList<BufferedImage> enemyFrames;
    private int maxHP;
    private int currentHP;
    private boolean canSeePlayer;
    private int currentFrame;
    private boolean boss;

    public Enemy(int row, int column, boolean boss) {
        super(row, column, false);
        this.boss = boss;
        if (boss) {
            maxHP = 30;
            this.setMinDamage(3);
            this.setMaxDamage(6);
            currentHP = 0;
        }
        else {
            maxHP = 10;
            currentHP = maxHP;
        }

        enemyFrames = new ArrayList<>();
        bigImage = loadImage(BIG_IMAGE_FILE);
        for (int i = 1; i < 5; i++) {
            String file = BIG_IMAGE_FRAMES + "_" + i + ".png";
            loadFrames(file);
        }

        canSeePlayer = false;
        currentFrame = 0;
    }

    public void setCurrentHP(int HP) {
        currentHP = HP;
    }

    public void loadFrames(String fileName) {
        enemyFrames.add(loadImage(fileName));
    }

    public String healthDisplay() {
        return currentHP + " / " + maxHP;
    }

    public int getMaxHP() {
        return maxHP;
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

    public BufferedImage getImage() {
        if (boss)
            return bigImage;
        else
            return enemyFrames.get(currentFrame);
    }

    public boolean moveNorth(Tile[][] map, Player p) {
        try {
            if (map[row-1][column].hasPlayer()) {
                int damage = (int)(Math.random()*(this.maxDamage-this.minDamage+1)) + this.minDamage;
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
                int damage = (int)(Math.random()*(this.maxDamage-this.minDamage+1)) + this.minDamage;
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
                int damage = (int)(Math.random()*(this.maxDamage-this.minDamage+1)) + this.minDamage;
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
                int damage = (int)(Math.random()*(this.maxDamage-this.minDamage+1)) + this.minDamage;
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

    public void nextFrame() {
        currentFrame++;
        if (currentFrame == enemyFrames.size()) {
            currentFrame = 0;
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
