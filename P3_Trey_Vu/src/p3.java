import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class p3 {
    public static void main(String[] args) {
        Maze maze = new Maze("src/test_maze.txt"); // Load text-based maze
        maze.printMaze(); // Print to verify
        
        Maze maze2 = new Maze("src/test_maze_coordinates.txt"); // Load coordinate-based maze
        maze2.printMaze(); // Print to verify
    }
}

