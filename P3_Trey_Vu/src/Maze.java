import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze {
    private int numRows, numCols, numRooms;
    private Tile start, goal, exit;
    private char[][][] map;
    private boolean isTextBased, isCoordinateBased;
    private ArrayList<Tile> allWolverines = new ArrayList<>();

    // Single constructor using the provided flag.
    public Maze(String filename, boolean inCoordinate) throws IncorrectMapFormatException, FileNotFoundException, IllegalMapCharacterException, IncompleteMapException {

        // Set format explicitly based on the flag.
        if (inCoordinate) {
            System.out.println("Using coordinate-based input.");
            isCoordinateBased = true;
            isTextBased = false;
        } else {
            System.out.println("Using text-based input.");
            isCoordinateBased = false;
            isTextBased = true;
        }
        
        // Open the file.
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        System.out.println("Map Name: " + filename);
        System.out.println("Using " + (isCoordinateBased ? "coordinate" : "text") + "-based input.");
        
        // Read the header line for dimensions (M N R)
        if (!scanner.hasNextLine()) {
            throw new IncorrectMapFormatException("File is empty; expected M N R");
        }
        String firstLine = scanner.nextLine().trim();
        String[] tokens = firstLine.split("\\s+");
        if (tokens.length < 3) {
            throw new IncorrectMapFormatException("Expected M N R in first line");
        }
        int M, N, R;
        try {
            M = Integer.parseInt(tokens[0]);
            N = Integer.parseInt(tokens[1]);
            R = Integer.parseInt(tokens[2]);
        } catch (NumberFormatException e) {
            throw new IncorrectMapFormatException("M, N, R not integers");
        }
        numRows = M;
        numCols = N;
        numRooms = R;
        map = new char[numRooms][numRows][numCols];

        // Initialize the map with '.' as default.
//        for (int room = 0; room < numRooms; room++) {
//            for (int row = 0; row < numRows; row++) {
//                for (int col = 0; col < numCols; col++) {
//                    map[room][row][col] = '.';
//                }
//            }
//        }
        
        // Process the remainder of the file exclusively based on the flag.
        if (isCoordinateBased) {
            System.out.println("Processing coordinate-based input:");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                System.out.println("DEBUG: Line read: \"" + line + "\"");
                if (!line.isEmpty()) {
                    String[] tks = line.split("\\s+");
                    if (tks.length >= 4) {
                        char type = tks[0].charAt(0);
                        int row = Integer.parseInt(tks[1]);
                        int col = Integer.parseInt(tks[2]);
                        int room = Integer.parseInt(tks[3]);
                        
                        map[room][row][col] = type;
                        
                        if (type == 'W') {
                            allWolverines.add(new Tile(row, col, room, type));
                            if (start == null) {
                                start = new Tile(row, col, room, type);
                            }
                            System.out.println("DEBUG: Added W at (room=" + room + ", row=" + row + ", col=" + col + ")");
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
        } else { 
        	// Process text-based input.
        	System.out.println("Processing text-based input:");
        	for (int room = 0; room < numRooms; room++) {
        	    for (int row = 0; row < numRows; row++) {
        	        // Check that there is a next line; if not, the map is incomplete.
        	        if (!scanner.hasNextLine()) {
        	            throw new IncompleteMapException("Incomplete map: expected " + numRows +
        	                                             " rows in room " + room + " but reached end-of-file at row " + row);
        	        }
        	        String line = scanner.nextLine().trim();
        	        // If the line is shorter than expected, throw an exception.
        	        if (line.length() < numCols) {
        	            throw new IncompleteMapException("Incomplete map: expected " + numCols +
        	                                             " columns in row " + row + " of room " + room +
        	                                             " but got " + line.length());
        	        }
        	        for (int col = 0; col < numCols; col++) {
        	            char c = line.charAt(col);
        	            if (!isGameChar(c)) {
        	                System.out.println("DEBUG: Illegal character '" + c + "' encountered at room " + room +
        	                                   ", row " + row + ", col " + col);
        	                throw new IllegalMapCharacterException("Illegal character '" + c +
        	                                                       "' encountered at room " + room +
        	                                                       ", row " + row + ", col " + col);
        	            }
        	            // Directly assign the character from the file.
        	            map[room][row][col] = c;
        	            
        	            // Track special positions.
        	            if (c == 'W') {
        	                allWolverines.add(new Tile(row, col, room, c));
        	                if (start == null) {
        	                    start = new Tile(row, col, room, c);
        	                }
        	            } else if (c == '$') {
        	                goal = new Tile(row, col, room, c);
        	            } else if (c == '|') {
        	                exit = new Tile(row, col, room, c);
        	            } 
        	        }
        	    }
        	}



        }
        scanner.close();
    }
    
    // Helper method to validate characters.
    private boolean isGameChar(char c) {
        return (c == '@' || c == '.' || c == 'W' || c == '$' || c == '|');
    }
    
    // Getters for maze data.
    public ArrayList<Tile> getAllWolverines() { 
    	return allWolverines; 
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
    
    // Method to print the maze.
    public void printMaze() {
        for (int room = 0; room < numRooms; room++) {
            System.out.println("Maze Level: " + room);
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    System.out.print(map[room][row][col] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
