import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MazeSolver {
    private Maze maze;
    private boolean[][][] visited;
    private Tile[][][] prev;

    public MazeSolver(Maze maze) {
        this.maze = maze;
        visited = new boolean[maze.getNumRooms()][maze.getNumRows()][maze.getNumCols()];
        prev = new Tile[maze.getNumRooms()][maze.getNumRows()][maze.getNumCols()];
    }

    public boolean solveWithQueue(Tile startTile) {
        // 1. Clear visited/prev arrays
        for (int r = 0; r < visited.length; r++) {
            for (int i = 0; i < visited[r].length; i++) {
                for (int j = 0; j < visited[r][i].length; j++) {
                    visited[r][i][j] = false;
                    prev[r][i][j] = null;
                }
            }
        }

        // 2. Initialize a queue-based BFS
        Queue<Tile> q = new LinkedList<>();
        q.add(startTile);
        visited[startTile.getRoom()][startTile.getRow()][startTile.getCol()] = true;

        // 3. Directions (N, S, E, W)
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, 1, -1};

        // DEBUG: Print starting tile info
//        System.out.println("DEBUG (Queue): Starting BFS from W at (room="
//                           + startTile.getRoom() + ", row=" + startTile.getRow()
//                           + ", col=" + startTile.getCol() + ") char=" + startTile.getChar());

        // 4. Standard BFS loop
        while (!q.isEmpty()) {
            Tile t = q.poll();

            // DEBUG: Show which tile we are visiting
//            System.out.println("DEBUG (Queue): Visiting (room=" + t.getRoom()
//                               + ", row=" + t.getRow()
//                               + ", col=" + t.getCol()
//                               + ") char=" + t.getChar());

            // a) If we find `$`, reconstruct full path
            if (t.getChar() == '$') {
                reconstructQueuePath(t);
                System.out.println("Path found!");
                System.out.println();
                return true;  // indicate success
            }

            // b) If we find `|`, reconstruct partial path to the doorway
            if (t.getChar() == '|') {
                reconstructQueuePath(t);
                System.out.println("Reached doorway!");
                System.out.println();
                return false;  // no coin, but we did find the doorway
            }

            // c) Explore neighbors
            for (int i = 0; i < 4; i++) {
                int newRow = t.getRow() + dRow[i];
                int newCol = t.getCol() + dCol[i];
                int room = t.getRoom();

                // DEBUG: Print neighbor checks
//                System.out.println("    Checking neighbor (room=" + room
//                                   + ", row=" + newRow
//                                   + ", col=" + newCol + ")");

                // Validate bounds
                if (newRow >= 0 && newRow < maze.getNumRows() &&
                    newCol >= 0 && newCol < maze.getNumCols()) {

                    char neighborChar = maze.getMap()[room][newRow][newCol];
//                    System.out.println("    Neighbor char=" + neighborChar);

                    // Check if not visited & is walkable
                    if (!visited[room][newRow][newCol] &&
                        maze.isWalkable(room, newRow, newCol)) {

                        visited[room][newRow][newCol] = true;
                        prev[room][newRow][newCol] = t;
                        q.add(new Tile(newRow, newCol, room, neighborChar));

                        // DEBUG: Print enqueuing
//                        System.out.println("    -> ENQUEUEING neighbor ("
//                                           + newRow + "," + newCol
//                                           + ") char=" + neighborChar);
                    } else {
                        // DEBUG: Print reason for skip
                        if (visited[room][newRow][newCol]) {
//                            System.out.println("    -> Already visited, skipping.");
                        } else {
//                            System.out.println("    -> Not walkable, skipping.");
                        }
                    }
                } else {
                    // Out of bounds
//                    System.out.println("    -> Out of bounds, skipping.");
                }
            }
        }

        // 5. If queue empties with no `$` or `|`, no path from this W
        System.out.println("No path found in this room (starting from "
                           + startTile.getRow() + "," + startTile.getCol() + ").");
        System.out.println();
        return false;
    }


    private void reconstructQueuePath(Tile goal) {
        ArrayList<Tile> pathTiles = new ArrayList<>();
        Tile t = goal;
        while (t != null && t.getChar() != 'W') {
//            System.out.println("DEBUG (Queue): Backtracking from (room=" + t.getRoom()
//                               + ", row=" + t.getRow()
//                               + ", col=" + t.getCol()
//                               + ") char=" + t.getChar());
            if (maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] != '$') {
                maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] = '+';
            }
            pathTiles.add(t);
            t = prev[t.getRoom()][t.getRow()][t.getCol()];
        }
        if (t != null && t.getChar() == 'W') {
            pathTiles.add(t);
        }

        System.out.println("Queue-Based Route (start → goal):");
        for (int i = pathTiles.size() - 1; i >= 0; i--) {
            Tile step = pathTiles.get(i);
            System.out.printf("+ %d %d %d\n", step.getRow(), step.getCol(), step.getRoom());
        }
    }

    
    public boolean solveWithStack(Tile startTile) {
        // 1. Clear visited/prev arrays
        for (int r = 0; r < visited.length; r++) {
            for (int i = 0; i < visited[r].length; i++) {
                for (int j = 0; j < visited[r][i].length; j++) {
                    visited[r][i][j] = false;
                    prev[r][i][j] = null;
                }
            }
        }

        // 2. Initialize a stack-based DFS
        Stack<Tile> stack = new Stack<>();
        stack.push(startTile);
        visited[startTile.getRoom()][startTile.getRow()][startTile.getCol()] = true;

        // 3. Directions (N, S, E, W)
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, 1, -1};

        // DEBUG: Print starting tile info
        System.out.println("DEBUG (Stack): Starting DFS from W at (room="
                           + startTile.getRoom() + ", row=" + startTile.getRow()
                           + ", col=" + startTile.getCol() + ") char=" + startTile.getChar());

        // 4. Standard DFS loop (LIFO)
        while (!stack.isEmpty()) {
            Tile t = stack.pop();

            // DEBUG: Show which tile we are visiting
            System.out.println("DEBUG (Stack): Visiting (room=" + t.getRoom()
                               + ", row=" + t.getRow()
                               + ", col=" + t.getCol()
                               + ") char=" + t.getChar());

            // a) If we find `$`, reconstruct full path
            if (t.getChar() == '$') {
                reconstructStackPath(t);
                System.out.println("Path found!");
                System.out.println();
                return true;  // indicate success
            }

            // b) If we find `|`, reconstruct partial path to the doorway
            if (t.getChar() == '|') {
                reconstructStackPath(t);
                System.out.println("Reached doorway!");
                System.out.println();
                return false;  // no coin, but we did find the doorway
            }

            // c) Explore neighbors
            for (int i = 0; i < 4; i++) {
                int newRow = t.getRow() + dRow[i];
                int newCol = t.getCol() + dCol[i];
                int room = t.getRoom();

                // DEBUG: Print neighbor checks
                System.out.println("    Checking neighbor (room=" + room
                                   + ", row=" + newRow
                                   + ", col=" + newCol + ")");

                // Validate bounds
                if (newRow >= 0 && newRow < maze.getNumRows() &&
                    newCol >= 0 && newCol < maze.getNumCols()) {

                    char neighborChar = maze.getMap()[room][newRow][newCol];
                    System.out.println("    Neighbor char=" + neighborChar);

                    // Check if not visited & is walkable
                    if (!visited[room][newRow][newCol] &&
                        maze.isWalkable(room, newRow, newCol)) {

                        visited[room][newRow][newCol] = true;
                        prev[room][newRow][newCol] = t;
                        stack.push(new Tile(newRow, newCol, room, neighborChar));

                        // DEBUG: Print pushing
                        System.out.println("    -> PUSHING neighbor ("
                                           + newRow + "," + newCol
                                           + ") char=" + neighborChar);
                    } else {
                        // DEBUG: Print reason for skip
                        if (visited[room][newRow][newCol]) {
                            System.out.println("    -> Already visited, skipping.");
                        } else {
                            System.out.println("    -> Not walkable, skipping.");
                        }
                    }
                } else {
                    // Out of bounds
                    System.out.println("    -> Out of bounds, skipping.");
                }
            }
        }

        // 5. If stack empties with no `$` or `|`, no path from this W
        System.out.println("No path found in this room (starting from "
                           + startTile.getRow() + "," + startTile.getCol() + ").");
        System.out.println();
        return false;
    }


	private void reconstructStackPath(Tile goal) {
    ArrayList<Tile> pathTiles = new ArrayList<>();
    Tile t = goal;
    while (t != null && t.getChar() != 'W') {
        if (maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] != '$') {
            maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] = '+';
        }
        pathTiles.add(t);
        t = prev[t.getRoom()][t.getRow()][t.getCol()];
    }
    if (t != null && t.getChar() == 'W') {
        pathTiles.add(t);
    }

    System.out.println("Stack-Based Route (start → goal):");
    for (int i = pathTiles.size() - 1; i >= 0; i--) {
        Tile step = pathTiles.get(i);
        System.out.printf("+ %d %d %d\n", step.getRow(), step.getCol(), step.getRoom());
    }
}

 

	public boolean solveWithOpt(Tile startTile) {
	    // Clear visited/prevkjkj
	    for (int r = 0; r < visited.length; r++) {
	        for (int i = 0; i < visited[r].length; i++) {
	            for (int j = 0; j < visited[r][i].length; j++) {
	                visited[r][i][j] = false;
	                prev[r][i][j] = null;
	            }
	        }
	    }
 
	    Queue<Tile> q = new LinkedList<>();
	    q.add(startTile);
	    visited[startTile.getRoom()][startTile.getRow()][startTile.getCol()] = true;

	    int[] dRow = {-1, 1, 0, 0};
	    int[] dCol = {0, 0, 1, -1};

	    while (!q.isEmpty()) {
	        Tile t = q.poll();

	        // If BFS finds the coin, reconstruct final path
	        if (t.getChar() == '$') {
	            reconstructOptPath(t);
	            System.out.println("Path found!");
	            System.out.println();
	            return true;
	        }

	        // NEW: If BFS finds a doorway, reconstruct partial path
	        if (t.getChar() == '|') {
	            // Reconstruct the path from this BFS's W(0) to the doorway
	            reconstructOptPath(t);
	            System.out.println("Reached doorway!");
	            System.out.println();
	            return false;  
	            // Indicate "no coin found," but we did find the doorway
	            // BFS ends here, letting the next BFS start from W(1).
	        }

	        // Explore neighbors
	        for (int i = 0; i < 4; i++) {
	            int newRow = t.getRow() + dRow[i];
	            int newCol = t.getCol() + dCol[i];
	            int room = t.getRoom();

	            if (newRow >= 0 && newRow < maze.getNumRows() &&
	                newCol >= 0 && newCol < maze.getNumCols() &&
	                !visited[room][newRow][newCol] &&
	                maze.isWalkable(room, newRow, newCol)) {

	                visited[room][newRow][newCol] = true;
	                prev[room][newRow][newCol] = t;
	                q.add(new Tile(newRow, newCol, room, 
	                               maze.getMap()[room][newRow][newCol]));
	            }
	        }
	    }

	    // If BFS empties out without finding '$' or '|'
	    System.out.println("No path found in this room (starting from " 
	                       + startTile.getRow() + "," + startTile.getCol() + ").");
	    System.out.println();
	    return false;
	}

	private void reconstructOptPath(Tile goal) {
	    ArrayList<Tile> pathTiles = new ArrayList<>();
	    Tile t = goal;

	    while (t != null && t.getChar() != 'W') {
	    	 System.out.println("DEBUG: Backtracking from (room=" + t.getRoom()
             + ", row=" + t.getRow()
             + ", col=" + t.getCol()
             + ") char=" + t.getChar());
	        if (maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] != '$') {
	            maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] = '+';
	        }
	        pathTiles.add(t);
	        t = prev[t.getRoom()][t.getRow()][t.getCol()];
	    }
	    if (t != null && t.getChar() == 'W') {
	        pathTiles.add(t);
	    }

	    System.out.println("Optimal (Shortest) Route (start → goal):");
	    for (int i = pathTiles.size() - 1; i >= 0; i--) {
	        Tile step = pathTiles.get(i);
	        System.out.printf("+ %d %d %d\n", step.getRow(), step.getCol(), step.getRoom());
	    }
	}



}
