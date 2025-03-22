import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maze {
    private int numRows, numCols, numRooms;
    private Tile start, goal, exit;
    private char[][][] map;
    private boolean isTextBased, isCoordinateBased;

    public Maze(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            numRows = scanner.nextInt();
            numCols = scanner.nextInt();
            numRooms = scanner.nextInt();
            map = new char[numRooms][numRows][numCols];
            
	        // Initialize map with '.' before filling it
            for (int room = 0; room < numRooms; room++) {
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        map[room][row][col] = '.';  // Default to walkable space
                    }
                }
            }
            
            scanner.nextLine(); // Move to the next line after dimensions

            // Detect format
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length >= 2 && tokens[1].matches("\\d+")) {
                        isCoordinateBased = true;
                        System.out.println("Detected: Coordinate-Based");
                    } else {
                        isTextBased = true;
                        System.out.println("Detected: Text-Based");
                    }
                    break;
                }
            }

            // Fix: Reopen the file to avoid skipping data
            scanner.close();
            scanner = new Scanner(file);
            
            // Skip the first three numbers again since we reopened the file
            scanner.nextInt();
            scanner.nextInt();
            scanner.nextInt();
            scanner.nextLine(); // Move past the first line

            // Read text-based format
            if (isTextBased) {
                for (int room = 0; room < numRooms; room++) {
                    for (int row = 0; row < numRows; row++) {
                        if (scanner.hasNextLine()) {
                            String line = scanner.nextLine().trim();
                            for (int col = 0; col < numCols && col < line.length(); col++) {
                                char c = line.charAt(col);
                                map[room][row][col] = c;

                                // Track key positions
                                if (c == 'W') start = new Tile(row, col, room, c);
                                if (c == '$') goal = new Tile(row, col, room, c);
                                if (c == '|') exit = new Tile(row, col, room, c);
                            }
                        }
                    }
                }
            }

            // Read coordinate-based format
            if (isCoordinateBased) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] tokens = line.split("\\s+");
                        if (tokens.length == 4) {
                            char type = tokens[0].charAt(0);
                            int row = Integer.parseInt(tokens[1]);
                            int col = Integer.parseInt(tokens[2]);
                            int room = Integer.parseInt(tokens[3]);

                            map[room][row][col] = type;

                            // Track key positions
                            if (type == 'W') {
                            	start = new Tile(row, col, room, type);
                            }
                            if (type == '$') {
                            	goal = new Tile(row, col, room, type);
                            }
                            if (type == '|') {
                            	exit = new Tile(row, col, room, type);
                            }
                        }
                    }
                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
        	System.out.println(e);
            System.out.println("Error: File not found.");
        }
    }

    public Tile getStart() { 
    	return start; 
    }
    public Tile getCoin() {
    	return goal;
    }
    public Tile getExit() {
    	return exit;
    }
    public boolean isWalkable(int r, int m, int n) {
        return (map[r][m][n] == '.' || map[r][m][n] == '$' || map[r][m][n] == '|');
    }
    public char[][][] getMap() {
    	return map;
    }
    
    public int getNumRooms() {
        return numRooms;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
    
    public void printMaze() {
        for (int room = 0; room < numRooms; room++) {
            System.out.println("Maze Level: " + room);
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    System.out.print(map[room][row][col] + " ");
                }
                System.out.println(); // Newline after each row
            }
            System.out.println(); // Extra space between mazes
        }
    }
}
