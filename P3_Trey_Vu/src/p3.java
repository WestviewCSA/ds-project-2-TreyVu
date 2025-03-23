import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class p3 {
    public static void main(String[] args) {
        Maze maze = new Maze("src/T4");       
        MazeSolver solver = new MazeSolver(maze);
//        solver.solveWithQueue();
//        solver.solveWithStack();
        solver.solveWithOpt();
        System.out.println("Final Maze:");
        maze.printMaze();
    }
}
 
