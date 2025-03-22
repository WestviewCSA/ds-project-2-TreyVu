import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

public class Maze {
	private static int numRows, numCols, numRooms;
	private Tile start, goal, exit;
	char[][][] map;
	
	private static boolean isTextBased, isCoordinateBased;
	
	public Maze(String filename) {
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			
			numRows 	= scanner.nextInt();
			numCols 	= scanner.nextInt();
			numRooms 	= scanner.nextInt();
			map = new char[numRooms][numRows][numCols];
			
			scanner.nextLine();
			if (scanner.hasNextInt()) {
				isCoordinateBased = true;
			} else {
				isTextBased = true;
			}
			
		} catch (FileNotFoundException e) {
			// handle exception
			System.out.println(e);
			System.out.println("gg");
		}	
	}
	
	public Tile getStart() {
		
	}
	
	public Tile getCoin() {
		
	}
	
	public Tile getExit() {
		
	}
	
	public boolean isWalkable(int r, int m, int n) {
		return (map[r][m][n] == '.' || map[r][m][n] == '$' || map[r][m][n] == '|');
	}
	
	public Tile[][][] getMap() {
		
	}
}
