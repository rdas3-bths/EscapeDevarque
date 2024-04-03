import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class World {

    private Tile[][] map;

    public World() {
        map = generateWorld();
    }

    public Tile[][] getTiles() {
        return map;
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
            }
        }
        return worldData;

    }

    public Tile[][] generateWorld() {
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
