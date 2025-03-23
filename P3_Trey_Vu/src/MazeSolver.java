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

    public void solveWithQueue() {
        Queue<Tile> q = new LinkedList<>();
        Tile start = maze.getStart();
        q.add(start);
        visited[start.getRoom()][start.getRow()][start.getCol()] = true;

        // Possible movements (North, South, East, West)
        int[] dRow = {-1, 1, 0, 0}; 
        int[] dCol = {0, 0, 1, -1};

        // **Step 1: Move through all doorways until last room**
        while (!q.isEmpty()) {
            Tile t = q.poll();

            // If we're in the last room and found '$', reconstruct the path
            if (t.getChar() == '$') {
            	reconstructQueuePath(t);
                System.out.println("Path found!");
                System.out.println();
                return;
            }

            // Move through the doorway if available
            if (t.getChar() == '|') {
                int nextRoom = t.getRoom() + 1;
                if (nextRoom < maze.getNumRooms()) {
                    for (int row = 0; row < maze.getNumRows(); row++) {
                        for (int col = 0; col < maze.getNumCols(); col++) {
                            if (maze.getMap()[nextRoom][row][col] == '|') {
                                visited[nextRoom][row][col] = true;
                                prev[nextRoom][row][col] = t;
                                q.add(new Tile(row, col, nextRoom, '|'));
                                System.out.println("Wolverine moved to the next maze!");
//                                System.out.println();
                                // **Don't return here**; let BFS keep running
                                break; 
                            }
                        }
                    }
                }
            }


            // **Step 2: Explore movements in the last room**
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
                    q.add(new Tile(newRow, newCol, room, maze.getMap()[room][newRow][newCol]));
                }
            }
        }

        System.out.println("No path found.");
    }

    private void reconstructQueuePath(Tile goal) {
        // We'll store the path in an ArrayList of Tiles
        ArrayList<Tile> pathTiles = new ArrayList<>();
        
        // Backtrack from goal to start
        Tile t = goal;
        while (t != null && t.getChar() != 'W') {
            // Mark path with '+', unless it's the coin
            if (maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] != '$') {
                maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] = '+';
            }
            pathTiles.add(t);
            t = prev[t.getRoom()][t.getRow()][t.getCol()];
        }
        
        // If we stopped because we reached Wolverine's tile
        if (t != null && t.getChar() == 'W') {
            pathTiles.add(t);  // Include Wolverine's position
        }

        // Print the path in coordinate-based style, but we must go backwards
        // because pathTiles is currently goal -> start
        System.out.println("Queue-Based Route (start → goal):");
        for (int i = pathTiles.size() - 1; i >= 0; i--) {
            Tile step = pathTiles.get(i);
            // Example format: "+ row col room"
            System.out.printf("+ %d %d %d\n", step.getRow(), step.getCol(), step.getRoom());
        }
    }

    
    public void solveWithStack() {
        Stack<Tile> stack = new Stack<>();
        Tile start = maze.getStart();
        stack.push(start);
        visited[start.getRoom()][start.getRow()][start.getCol()] = true;
        System.out.println("Wolverine starts at: Room " + start.getRoom() + " Row " + start.getRow() + " Col " + start.getCol());
        
        Tile goal = maze.getCoin();
        System.out.println("Coin is at: Room " + goal.getRoom() + " Row " + goal.getRow() + " Col " + goal.getCol());

        // Possible movements (North, South, East, West)
        int[] dRow = {-1, 1, 0, 0}; // Up, Down, No change, No change
        int[] dCol = {0, 0, 1, -1}; // No change, No change, Right, Left

        while (!stack.isEmpty()) {
        	
            Tile t = stack.peek(); // **DFS: Take last added tile (LIFO)**
//            System.out.println("DFS visiting: Room " + t.getRoom() + " Row " + t.getRow() + " Col " + t.getCol());
            stack.pop();
            
            
            // **Step 1: If we found `$`, reconstruct the path and stop**
            if (t.getChar() == '$') {
            	reconstructStackPath(t);
                System.out.println("Path found!");
                System.out.println();
                return;
            }

            // **Step 2: Explore the four possible directions (N, S, E, W)**
            for (int i = 0; i < 4; i++) {
                int newRow = t.getRow() + dRow[i];
                int newCol = t.getCol() + dCol[i];
                int room = t.getRoom(); // Stay in the same room

                if (newRow >= 0 && newRow < maze.getNumRows() &&
                    newCol >= 0 && newCol < maze.getNumCols() &&
                    !visited[room][newRow][newCol] &&
                    maze.isWalkable(room, newRow, newCol)) {

//                    System.out.println("DFS pushing: Room " + room + " Row " + newRow + " Col " + newCol);
                    visited[room][newRow][newCol] = true;
                    prev[room][newRow][newCol] = t;
                    stack.push(new Tile(newRow, newCol, room, maze.getMap()[room][newRow][newCol]));
                }
            }


            // **Step 3: Move through the doorway `|` (if applicable)**
            if (t.getChar() == '|') {
                int nextRoom = t.getRoom() + 1;
                if (nextRoom < maze.getNumRooms()) {
                    for (int row = 0; row < maze.getNumRows(); row++) {
                        for (int col = 0; col < maze.getNumCols(); col++) {
                            if (maze.getMap()[nextRoom][row][col] == '|') {
                                System.out.println("Position moved to next maze at Room " + nextRoom + " Row " + row + " Col " + col);
                                System.out.println();
                                visited[nextRoom][row][col] = true;
                                prev[nextRoom][row][col] = t;
                                stack.push(new Tile(row, col, nextRoom, '|'));
                                break;
                            }
                        }
                    }
                }
            }


        }

        System.out.println("No path found.");
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
        
        // If we stopped because we reached Wolverine's tile
        if (t != null && t.getChar() == 'W') {
            pathTiles.add(t);  // Include Wolverine's position
        }
        
        // Print the path in correct order (start -> goal)
        System.out.println("Stack-Based Route (start → goal):");
        for (int i = pathTiles.size() - 1; i >= 0; i--) {
            Tile step = pathTiles.get(i);
            System.out.printf("+ %d %d %d\n", step.getRow(), step.getCol(), step.getRoom());
        }
    }

    public void solveWithOpt() {
        // Clear visited array so it doesn't conflict with previous searches
        for (int r = 0; r < visited.length; r++) {
            for (int i = 0; i < visited[r].length; i++) {
                for (int j = 0; j < visited[r][i].length; j++) {
                    visited[r][i][j] = false;
                }
            }
        }

        // Clear prev array as well
        for (int r = 0; r < prev.length; r++) {
            for (int i = 0; i < prev[r].length; i++) {
                for (int j = 0; j < prev[r][i].length; j++) {
                    prev[r][i][j] = null;
                }
            }
        }

        // Standard BFS approach across all mazes
        Queue<Tile> q = new LinkedList<>();
        Tile start = maze.getStart();
        q.add(start);
        visited[start.getRoom()][start.getRow()][start.getCol()] = true;

        // Directions (N, S, E, W)
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, 1, -1};

        while (!q.isEmpty()) {
            Tile t = q.poll();

            // If we find '$', reconstruct the path and stop
            if (t.getChar() == '$') {
                reconstructOptPath(t);
                System.out.println("Path found!");
                System.out.println();
                return;
            }

            // If we find a doorway '|', just enqueue the matching doorway in the next room
            // but DO NOT return immediately. Keep searching for the coin.
            if (t.getChar() == '|') {
                int currentRoom = t.getRoom();
                int nextRoom = currentRoom + 1;
                if (nextRoom < maze.getNumRooms()) {
                    // Find the doorway in the next room
                    for (int row = 0; row < maze.getNumRows(); row++) {
                        for (int col = 0; col < maze.getNumCols(); col++) {
                            if (maze.getMap()[nextRoom][row][col] == '|'
                                && !visited[nextRoom][row][col]) {

                                visited[nextRoom][row][col] = true;
                                prev[nextRoom][row][col] = t;
                                q.add(new Tile(row, col, nextRoom, '|'));
                                System.out.println("Opt BFS: Wolverine moved to next maze (room " 
                                                    + nextRoom + ")!");
                                // We do NOT return here; BFS continues
                            }
                        }
                    }
                }
            }

            // Explore the four neighbors in the current room
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

        // If queue empties out, no coin was found in any room
        System.out.println("No path found.");
        System.out.println();
    }
    
    private void reconstructOptPath(Tile goal) {
        // We'll store the path in an ArrayList of Tiles
        ArrayList<Tile> pathTiles = new ArrayList<>();

        // Backtrack from goal to start
        Tile t = goal;
        while (t != null && t.getChar() != 'W') {
            // Mark path with '+', unless it's the coin
            if (maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] != '$') {
                maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] = '+';
            }
            pathTiles.add(t);
            t = prev[t.getRoom()][t.getRow()][t.getCol()];
        }

        // Add Wolverine's tile if we reached it
        if (t != null && t.getChar() == 'W') {
            pathTiles.add(t);
        }

        // Print the path from W → $
        System.out.println("Optimal (Shortest) Route (start → goal):");
        for (int i = pathTiles.size() - 1; i >= 0; i--) {
            Tile step = pathTiles.get(i);
            System.out.printf("+ %d %d %d\n", step.getRow(), step.getCol(), step.getRoom());
        }
    }



}
