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

	        numRows = scanner.nextInt();
	        numCols = scanner.nextInt();
	        numRooms = scanner.nextInt();
	        map = new char[numRooms][numRows][numCols];
	        
	        // Move to the next line after reading dimensions
	        scanner.nextLine(); 

	        // Read the first non-empty line of the maze
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine().trim();
	            if (!line.isEmpty()) {  // Ignore empty lines
	                String[] tokens = line.split("\\s+"); // Split by spaces
	                if (tokens.length >= 2 && tokens[1].matches("\\d+")) {
	                    isCoordinateBased = true;
	                    System.out.println("Detected: Coordinate-Based");
	                } else {
	                    isTextBased = true;
	                    System.out.println("Detected: Text-Based");
	                }
	                break; // We only need to check one line
	            }
	        }
	        
	        if (isTextBased) {
	        	while (scanner.hasNextLine()) {
					String row = scanner.nextLine();			
					if (!row.isEmpty()) {
						
					}
				}
	        } else if (isCoordinateBased) {
	        	
	        }

	    } catch (FileNotFoundException e) {
	    	System.out.println(e);
	        System.out.println("Error: File not found.");
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
