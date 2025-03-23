import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeGenerator {

    private int width;
    private int height;
    private int[][] maze;
    private DisjointSet disjointSet;
    private Random random;

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.maze = new int[height * 2 + 1][width * 2 + 1]; // Initialize maze with walls
        this.disjointSet = new DisjointSet(width * height);
        this.random = new Random();
        initMaze();
    }

    private void initMaze() {
        // Initialize maze borders
        for (int i = 0; i < maze.length; i++) {
            maze[i][0] = 1;
            maze[i][maze[0].length - 1] = 1;
        }
        for (int j = 0; j < maze[0].length; j++) {
            maze[0][j] = 1;
            maze[maze.length - 1][j] = 1;
        }

        // Initialize interior walls
        for (int i = 1; i < maze.length - 1; i += 2) {
            for (int j = 1; j < maze[0].length - 1; j += 2) {
                maze[i][j] = 0; // Path
                if (i < maze.length - 2) maze[i + 1][j] = 1; // Vertical wall
                if (j < maze[0].length - 2) maze[i][j + 1] = 1; // Horizontal wall
            }
        }
    }


    public int[][] generateMaze() {
        List<Wall> walls = getAllWalls();
        Collections.shuffle(walls, random);

        for (Wall wall : walls) {
            int cell1 = wall.cell1;
            int cell2 = wall.cell2;

            if (disjointSet.find(cell1) != disjointSet.find(cell2)) {
                removeWall(wall);
                disjointSet.union(cell1, cell2);
            }
        }
        return maze;
    }

     private List<Wall> getAllWalls() {
        List<Wall> walls = new ArrayList<>();
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                walls.add(new Wall(i * width + j - width, i * width + j, 0)); // Vertical walls
            }
        }
         for (int i = 0; i < height; i++) {
            for (int j = 1; j < width; j++) {
                walls.add(new Wall(i * width + j - 1, i * width + j, 1)); // Horizontal walls
            }
        }
        return walls;
    }

    private void removeWall(Wall wall) {
      int x1, y1, x2, y2;
        if (wall.orientation == 0) { // Vertical
            x1 = (wall.cell1 % width) * 2 + 1;
            y1 = (wall.cell1 / width) * 2 + 1;
            maze[y1 + 1][x1] = 0;

        } else { // Horizontal
             x1 = (wall.cell1 % width) * 2 + 1;
             y1 = (wall.cell1 / width) * 2 + 1;
            maze[y1][x1+1] = 0;
        }
    }

     private class Wall {
        int cell1;
        int cell2;
         int orientation; // 0: Vertical, 1: Horizontal

        public Wall(int cell1, int cell2, int orientation) {
            this.cell1 = cell1;
            this.cell2 = cell2;
            this.orientation = orientation;
        }
    }

    // Union-Find data structure
    private class DisjointSet {
        private int[] parent;

        public DisjointSet(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]); // Path compression
        }

        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                parent[rootI] = rootJ;
            }
        }
    }

    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        MazeGenerator generator = new MazeGenerator(width, height);
        int[][] maze = generator.generateMaze();

        // Print the maze
        for (int[] row : maze) {
            for (int cell : row) {
                System.out.print(cell == 1 ? "[]" : "  ");
            }
            System.out.println();
        }
    }
}
