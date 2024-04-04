import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class World {

    private Tile[][] map;
    private Player p;

    public World() {
        generateWorld();
    }

    public Tile[][] getTiles() {

        return map;
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

        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                if (!map[r][c].isMainPath() && map[r][c].getTileType() == 0) {
                    //System.out.println(r + " " + c);
                }
            }
        }

        setVisibility();
    }

    public void setVisibility() {
        int playerRow = p.getRow();
        int playerColumn = p.getColumn();

        int topLeftRow = playerRow - 1;
        int topLeftColumn = playerColumn - 1;

        for (int i = topLeftRow; i < topLeftRow+3; i++) {
            for (int j = topLeftColumn; j < topLeftColumn+3; j++) {
                try {
                    map[i][j].setVisible();
                }
                catch (ArrayIndexOutOfBoundsException e) { }
            }
        }
    }
}
