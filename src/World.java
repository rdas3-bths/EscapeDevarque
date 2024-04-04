import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class World {

    private Tile[][] map;
    private Player p;

    public World() {
        map = generateWorld();
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
                if (map[currentPlayerRow-1][currentPlayerColumn].getTileType() != 1) {
                    p.setRow(currentPlayerRow-1);
                }
            }
        }

        if (direction.equals("E")) {
            // if column is less than last column - 1
            if (currentPlayerColumn < map[0].length-1) {
                // if Tile to the right is not tileType 1
                if (map[currentPlayerRow][currentPlayerColumn+1].getTileType() != 1) {
                    p.setColumn(currentPlayerColumn+1);
                }
            }
        }

        if (direction.equals("S")) {
            // if row is less than last row - 1
            if (currentPlayerRow < map.length - 1) {
                // if Tile below is not tileType 1
                if (map[currentPlayerRow+1][currentPlayerColumn].getTileType() != 1) {
                    p.setRow(currentPlayerRow+1);
                }
            }
        }

        if (direction.equals("W")) {
            // if column is greater than 0
            if (currentPlayerColumn > 0) {
                // if Tile to left is not tileType 1
                if (map[currentPlayerRow][currentPlayerColumn-1].getTileType() != 1) {
                    p.setColumn(currentPlayerColumn-1);
                }
            }
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

    private Tile[][] generateWorld() {
        int worldNumber = (int)((Math.random()*3) + 1);
        int[][] mazeData = getWorld("worlds/world" + worldNumber);

        Tile[][] world = new Tile[30][40];

        for (int r = 0; r < world.length; r++) {
            for (int c = 0; c < world[0].length; c++) {
                Tile t = new Tile(mazeData[r][c]);
                world[r][c] = t;
            }
        }

        for (int r = 0; r < world.length; r++) {
            for (int c = 0; c < world[0].length; c++) {
                if (!world[r][c].isMainPath()) {
                    int change = (int)(Math.random()*10);
                    if (change < 3) {
                        world[r][c] = new Tile(0);
                    }
                }
            }
        }
        return world;
    }
}
