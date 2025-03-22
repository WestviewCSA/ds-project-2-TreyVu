import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class p3 {
    public static void main(String[] args) {
        Maze maze = new Maze("src/TEST09"); // Load text-based maze        
        MazeSolver solver = new MazeSolver(maze);
        solver.solveWithQueue(); // Run BFS

        // Print the final map to see Wolverine's path
        System.out.println("Final Maze:");
        maze.printMaze(); // Implement a `printMaze()` method in Maze
    }
}
 
