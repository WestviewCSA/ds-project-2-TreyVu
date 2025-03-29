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

    public Maze(String filename) throws IncorrectMapFormatException, FileNotFoundException {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            System.out.println("Map Name: " + filename);
            System.out.println("Using " + (isCoordinateBased ? "coordinate" : "text") + "-based input.");

            
         // read first line
            if (!scanner.hasNextLine()) {
                throw new IncorrectMapFormatException("File is empty; expected M N R");
            }
            String firstLine = scanner.nextLine().trim();
            String[] tokens = firstLine.split("\\s+");
            if (tokens.length < 3) {
                throw new IncorrectMapFormatException("Expected M N R in first line");
            }
            // parse M, N, R
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
            
	        // Initialize map with '.' before filling it
            for (int room = 0; room < numRooms; room++) {
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        map[room][row][col] = '.';  // Default to walkable space
                    }
                }
            }
            
//            scanner.nextLine(); // Move to the next line after dimensions

            // Detect format
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] tokens2 = line.split("\\s+");
                    if (tokens2.length >= 2 && tokens2[1].matches("\\d+")) {
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
                        // If the file doesn't have enough lines, you can break or throw an exception
                        if (!scanner.hasNextLine()) {
                            // Optionally throw IncompleteMapException or just break
                            break;
                        }
                        
                        // Read the line, remove trailing spaces
                        String line = scanner.nextLine().trim();
                        // Expect line to have exactly numCols characters (no spaces in between)
                        
                        for (int col = 0; col < numCols && col < line.length(); col++) {
                            char c = line.charAt(col);
                            
                            // Only accept recognized characters; default to '.'
                            if (isGameChar(c)) {
                                map[room][row][col] = c;
                            } else {
                                map[room][row][col] = '.';
                            }
                            
                            // Track special characters
                            if (c == 'W') {
                            	// Always add this W to the list
                                allWolverines.add(new Tile(row, col, room, c));
                            	
                                // Only set start if we don't have one yet
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


        // Read coordinate-based format
            if (isCoordinateBased) {
                System.out.println("Processing coordinate-based input:");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    System.out.println("DEBUG: Line read: \"" + line + "\"");
                    if (!line.isEmpty()) {
                        String[] tokens3 = line.split("\\s+");
                        if (tokens3.length >= 4) {  // Use >= 4 instead of == 4
                            char type = tokens3[0].charAt(0);
                            int row = Integer.parseInt(tokens3[1]);
                            int col = Integer.parseInt(tokens3[2]);
                            int room = Integer.parseInt(tokens3[3]);

                            map[room][row][col] = type;

                            // Track key positions
                            if (type == 'W') {
                                start = new Tile(row, col, room, type);
                                allWolverines.add(new Tile(row, col, room, type));
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
            }




            scanner.close();

        } catch (FileNotFoundException e) {
        	System.out.println(e);
            System.out.println("Error: File not found.");
        }
    }
    
    public Maze(String filename, boolean inCoordinate) throws IncorrectMapFormatException, FileNotFoundException {
        try {
            // Set format based on the provided flag
            if (inCoordinate) {
                System.out.println("Using coordinate-based input.");
                isCoordinateBased = true;
                isTextBased = false;
            } else {
                System.out.println("Using text-based input.");
                isCoordinateBased = false;
                isTextBased = true;
            }

            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            System.out.println("Map Name: " + filename);
            System.out.println("Using " + (isCoordinateBased ? "coordinate" : "text") + "-based input.");

            // Read header (dimensions)
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

            // Initialize map with '.' before filling it
            for (int room = 0; room < numRooms; room++) {
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        map[room][row][col] = '.';
                    }
                }
            }

            // Skip the rest of the header line (if any)
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Now choose only one branch based on the flag:
            if (isCoordinateBased) {
                System.out.println("Processing coordinate-based input:");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    System.out.println("DEBUG: Line read: \"" + line + "\"");
                    if (!line.isEmpty()) {
                        String[] tokens3 = line.split("\\s+");
                        if (tokens3.length >= 4) {
                            char type = tokens3[0].charAt(0);
                            int row = Integer.parseInt(tokens3[1]);
                            int col = Integer.parseInt(tokens3[2]);
                            int room = Integer.parseInt(tokens3[3]);

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
            } else { // text-based
                for (int room = 0; room < numRooms; room++) {
                    for (int row = 0; row < numRows; row++) {
                        if (!scanner.hasNextLine()) break;
                        String line = scanner.nextLine().trim();
                        for (int col = 0; col < numCols && col < line.length(); col++) {
                            char c = line.charAt(col);
                            if (isGameChar(c)) {
                                map[room][row][col] = c;
                            } else {
                                map[room][row][col] = '.';
                            }
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

        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.out.println("Error: File not found.");
        }
    }

    
    private boolean isGameChar(char c) {
        // Only accept these characters as valid
        return (c == '@' || c == '.' || c == 'W' || c == '$' || c == '|');
    }
    
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
