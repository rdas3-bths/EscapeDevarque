import java.awt.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class World {

    private Tile[][] map;
    private Player p;
    private boolean cheatMode;
    private Key key;
    private boolean gameOver;
    private Coin[] coins;
    private Shop shop;
    private Enemy[] enemies;
    private final int AMOUNT_OF_ENEMIES = 8;

    public World() {
        generateWorld();
        cheatMode = false;
        gameOver = false;
    }

    public void endGame() {
        gameOver = true;
    }

    public Tile[][] getTiles() {

        return map;
    }

    public int getCoinsCollected() {
        int collected = 0;
        for (Coin c : coins) {
            if (c.isCollected())
                collected++;
        }
        return collected;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public boolean cheatMode() {
        return cheatMode;
    }

    public void flipCheat() {
        cheatMode = !cheatMode;
    }

    public Player getPlayer() {
        return p;
    }

    public Enemy getEnemy(int row, int column) {
        for (Enemy e : enemies) {
            if (e.getRow() == row && e.getColumn() == column) {
                return e;
            }
        }
        return null;
    }

    public void movePlayer(String direction) {
        int currentPlayerRow = p.getRow();
        int currentPlayerColumn = p.getColumn();

        if (direction.equals("N")) {
            // if row is greater than 0
            if (currentPlayerRow > 0) {
                // if Tile above is not tileType 1
                if (map[currentPlayerRow - 1][currentPlayerColumn].hasEnemy()) {
                    Enemy attackThis = getEnemy(currentPlayerRow - 1, currentPlayerColumn);
                    attackEnemy(attackThis);
                    moveEnemies();
                    return;
                }
                if (map[currentPlayerRow - 1][currentPlayerColumn].getTileType() != 1) {
                    p.setRow(currentPlayerRow - 1);
                } else {
                    if (p.getPickAxeDurability() != 0) {
                        map[currentPlayerRow - 1][currentPlayerColumn].setTileType(0);
                        p.usePickAxe();
                    }
                }
            }
        }

        if (direction.equals("E")) {
            // if column is less than last column - 1
            if (currentPlayerColumn < map[0].length - 1) {
                // if Tile to the right is not tileType 1
                if (map[currentPlayerRow][currentPlayerColumn + 1].hasEnemy()) {
                    Enemy attackThis = getEnemy(currentPlayerRow, currentPlayerColumn + 1);
                    attackEnemy(attackThis);
                    moveEnemies();
                    return;
                }
                if (map[currentPlayerRow][currentPlayerColumn + 1].getTileType() != 1) {
                    p.setColumn(currentPlayerColumn + 1);
                }
                else {
                    if (p.getPickAxeDurability() != 0) {
                        map[currentPlayerRow][currentPlayerColumn + 1].setTileType(0);
                        p.usePickAxe();
                    }
                }
            }
        }

        if (direction.equals("S")) {
            // if row is less than last row - 1
            if (currentPlayerRow < map.length - 1) {
                if (map[currentPlayerRow + 1][currentPlayerColumn].hasEnemy()) {
                    Enemy attackThis = getEnemy(currentPlayerRow + 1, currentPlayerColumn);
                    attackEnemy(attackThis);
                    moveEnemies();
                    return;
                }
                // if Tile below is not tileType 1
                if (map[currentPlayerRow + 1][currentPlayerColumn].getTileType() != 1) {
                    p.setRow(currentPlayerRow + 1);
                } else {
                    if (p.getPickAxeDurability() != 0) {
                        map[currentPlayerRow + 1][currentPlayerColumn].setTileType(0);
                        p.usePickAxe();
                    }
                }
            }
        }

        if (direction.equals("W")) {
            // if column is greater than 0
            if (currentPlayerColumn > 0) {
                if (map[currentPlayerRow][currentPlayerColumn - 1].hasEnemy()) {
                    Enemy attackThis = getEnemy(currentPlayerRow, currentPlayerColumn - 1);
                    attackEnemy(attackThis);
                    moveEnemies();
                    return;
                }
                // if Tile to left is not tileType 1
                if (map[currentPlayerRow][currentPlayerColumn - 1].getTileType() != 1) {
                    p.setColumn(currentPlayerColumn - 1);
                } else {
                    if (p.getPickAxeDurability() != 0) {
                        map[currentPlayerRow][currentPlayerColumn - 1].setTileType(0);
                        p.usePickAxe();
                    }
                }
            }
        }

        setVisibility();
        if (p.getRow() == key.getRow() && p.getColumn() == key.getColumn()) {
            key.setCollected();
            map[key.getRow()][key.getColumn()].collectItem();
        }

        for (Coin c : coins) {
            if (p.getRow() == c.getRow() && p.getColumn() == c.getColumn() && !c.isCollected()) {
                c.setCollected();
                p.collectGold(c.getValue());
                map[c.getRow()][c.getColumn()].collectItem();
            }
        }

        if (p.getRow() == map.length-1 && map[p.getRow()][p.getColumn()].getTileType() == 2 && key.isCollected()) {
            gameOver = true;
        }

        if (p.getRow() == shop.getRow() && p.getColumn() == shop.getCol()) {
            shop.setVisited(true);
        }
        else {
            shop.setVisited(false);
        }

        map[currentPlayerRow][currentPlayerColumn].setPlayer(false);
        map[p.getRow()][p.getColumn()].setPlayer(true);
        moveEnemies();
    }

    public void moveEnemies() {
        Tile playerTile = getPlayerTile();
        for (Enemy e : enemies) {
            if (e.getCurrentHP() > 0)
                e.moveEnemy(map, p);
            else {
                map[e.getRow()][e.getColumn()].setEnemy(false);
            }
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void attackEnemy(Enemy e) {
        int damage = (int)(Math.random()*p.maxDamage) + p.minDamage;
        e.takeDamage(damage);
        if (e.getCurrentHP() <= 0) {
            p.collectGold((int)(Math.random()*3)+1);
        }
    }

    private int[][] getWorld(String fileName) {
        File f = new File(fileName);
        Scanner s = null;
        try {
            s = new Scanner(f);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }

        ArrayList<String> fileData = new ArrayList<String>();
        while (s.hasNextLine())
            fileData.add(s.nextLine());

        int rows = fileData.size();
        int cols = fileData.get(0).length();

        int[][] worldData = new int[rows][cols];

        for (int i = 0; i < fileData.size(); i++) {
            String d = fileData.get(i);
            for (int j = 0; j < d.length(); j++) {
                if (d.charAt(j) == '#')
                    worldData[i][j] = 1;
                if (d.charAt(j) == '.')
                    worldData[i][j] = 0;
                if (d.charAt(j) == 'S' || d.charAt(j) == 'E') {
                    worldData[i][j] = 2;
                }
                if (d.charAt(j) == 'S') {
                    this.p = new Player(i, j);
                }
            }
        }
        return worldData;

    }

    private void highlightMainPath() {
        boolean check = true;
        while (check) {
            for (int r = 0; r < map.length; r++) {
                for (int c = 0; c < map[0].length; c++) {
                    if (map[r][c].isMainPath()) {
                        ArrayList<Tile> adjacent = getAdjacentTiles(r, c);
                        for (Tile t : adjacent) {
                            t.setMainPath();
                        }
                    }
                }
            }

            check = false;
            for (int r = 0; r < map.length; r++) {
                for (int c = 0; c < map[0].length; c++) {
                    if (map[r][c].isMainPath()) {
                        ArrayList<Tile> adjacent = getAdjacentTiles(r, c);
                        if (adjacent.size() != 0) {
                            check = true;
                        }
                    }
                }
            }
        }

    }

    private void generateWorld() {
        int worldNumber = (int)((Math.random()*3) + 1);
        int[][] mazeData = getWorld("worlds/world" + worldNumber);

        map = new Tile[30][40];

        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                Tile t = new Tile(mazeData[r][c], r, c);
                map[r][c] = t;
            }
        }

        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                if (!map[r][c].isMainPath()) {
                    int change = (int)(Math.random()*10);
                    if (change < 4) {
                        map[r][c].setTileType(0);
                    }
                }
            }
        }

        map[p.getRow()][p.getColumn()].setPlayer(true);

        setVisibility();
        highlightMainPath();
        generateKey();
        generateCoins();
        placeShop();
        generateEnemies();
    }

    public Shop getShop() {
        return shop;
    }

    public Coin[] getCoins() {
        return coins;
    }

    private void generateCoins() {
        coins = new Coin[10];
        ArrayList<Point> availablePoints = new ArrayList<Point>();
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                if (map[r][c].getTileType() == 0) {
                    availablePoints.add(new Point(r, c));
                }
            }
        }
        int coinsGenerated = 0;
        while (coinsGenerated != 10) {
            int randomCoinLocation = (int)(Math.random()*availablePoints.size());
            Point keyLocation = availablePoints.remove(randomCoinLocation);
            int row = (int)keyLocation.getX();
            int column = (int)keyLocation.getY();
            coins[coinsGenerated] = new Coin(row, column);
            map[row][column].setItem();
            coinsGenerated++;
        }
    }

    private void generateEnemies() {
        enemies = new Enemy[AMOUNT_OF_ENEMIES];

        ArrayList<Point> availablePoints = new ArrayList<Point>();
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                if (map[r][c].isMainPath() && !map[r][c].hasItem()) {
                    availablePoints.add(new Point(r, c));
                }
            }
        }
        int enemiesGenerated = 0;
        while (enemiesGenerated != enemies.length) {
            int randomEnemyLocation = (int)(Math.random()*availablePoints.size());
            Point keyLocation = availablePoints.remove(randomEnemyLocation);
            int row = (int)keyLocation.getX();
            int column = (int)keyLocation.getY();
            enemies[enemiesGenerated] = new Enemy(row, column);
            map[row][column].setEnemy(true);
            enemiesGenerated++;
        }
    }

    private void placeShop() {
        ArrayList<Point> availablePoints = new ArrayList<Point>();
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                if (map[r][c].getTileType() == 0 && !map[r][c].hasItem()) {
                    availablePoints.add(new Point(r, c));
                }
            }

            int shopPoint = (int)(Math.random()*availablePoints.size());
            Point shopLocation = availablePoints.remove(shopPoint);
            int row = (int)shopLocation.getX();
            int column = (int)shopLocation.getY();
            shop = new Shop(row, column);
        }
    }

    private void generateKey() {
        ArrayList<Point> availablePoints = new ArrayList<Point>();
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                if (!map[r][c].isMainPath() && map[r][c].getTileType() == 0) {
                    availablePoints.add(new Point(r, c));
                }
            }
        }
        int randomKeyLocationIndex = (int)(Math.random()*availablePoints.size());
        Point keyLocation = availablePoints.get(randomKeyLocationIndex);
        int row = (int)keyLocation.getX();
        int column = (int)keyLocation.getY();
        key = new Key(row, column);
        map[row][column].setItem();
    }

    private void setVisibility() {
        int playerRow = p.getRow();
        int playerColumn = p.getColumn();

        int topLeftRow = playerRow - 4;
        int topLeftColumn = playerColumn - 4;

        for (int i = topLeftRow; i <= topLeftRow+7; i++) {
            for (int j = topLeftColumn; j <= topLeftColumn+9; j++) {
                try {
                    map[i][j].setVisible();
                }
                catch (ArrayIndexOutOfBoundsException e) { }
            }
        }
    }

    public Key getKey() {
        return key;
    }

    private ArrayList<Tile> getAdjacentTiles(int row, int column) {
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
        if (row != 0) {
            Tile up = map[row-1][column];
            if (!up.isMainPath() && up.getTileType() != 1)
                adjacentTiles.add(up);
        }
        if (column != 0) {
            Tile left = map[row][column-1];
            if (!left.isMainPath() && left.getTileType() != 1) {
                adjacentTiles.add(left);
            }
        }
        if (row != map.length-1) {
            Tile down = map[row+1][column];
            if (!down.isMainPath() && down.getTileType() != 1) {
                adjacentTiles.add(down);
            }
        }
        if (column != map[0].length-1) {
            Tile right = map[row][column+1];
            if (!right.isMainPath() && right.getTileType() != 1) {
                adjacentTiles.add(right);
            }
        }
        return adjacentTiles;
    }

    public Tile getPlayerTile() {
        for (Tile[] row : map) {
            for (Tile t : row) {
                if (t.hasPlayer()) {
                    return t;
                }
            }
        }
        return null;
    }

    public void printEnemyState() {
        for (Tile[] row : map) {
            for (Tile t : row) {
                if (t.hasEnemy()) {
                    System.out.println("Tile: " + t.getRow() + " " + t.getColumn() + " has enemy");
                }
            }
        }
    }

    public boolean validPosition(int row, int col) {
        try {
            Tile t = map[row][col];
            return true;
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
