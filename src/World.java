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

    public World() {
        generateWorld();
        cheatMode = false;
        gameOver = false;
    }

    public Tile[][] getTiles() {

        return map;
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

    public void movePlayer(String direction) {
        int currentPlayerRow = p.getRow();
        int currentPlayerColumn = p.getColumn();

        if (direction.equals("N")) {
            // if row is greater than 0
            if (currentPlayerRow > 0) {
                // if Tile above is not tileType 1
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
                if (map[currentPlayerRow][currentPlayerColumn + 1].getTileType() != 1) {
                    p.setColumn(currentPlayerColumn + 1);
                } else {
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
        }

        if (p.getRow() == map.length-1 && map[p.getRow()][p.getColumn()].getTileType() == 2 && key.isCollected()) {
            gameOver = true;
        }
    }

    public boolean isGameOver() {
        return gameOver;
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
                Tile t = new Tile(mazeData[r][c]);
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

        setVisibility();
        highlightMainPath();
        generateKey();

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
        key = new Key((int)keyLocation.getX(), (int)keyLocation.getY());
    }

    private void setVisibility() {
        int playerRow = p.getRow();
        int playerColumn = p.getColumn();

        int topLeftRow = playerRow - 2;
        int topLeftColumn = playerColumn - 2;

        for (int i = topLeftRow; i <= topLeftRow+4; i++) {
            for (int j = topLeftColumn; j <= topLeftColumn+4; j++) {
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
}
