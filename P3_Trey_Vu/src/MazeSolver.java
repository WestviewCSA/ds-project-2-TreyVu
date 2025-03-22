import java.util.LinkedList;
import java.util.Queue;

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
        int[] dRow = {-1, 1, 0, 0}; // Row movement: Up, Down, No change, No change
        int[] dCol = {0, 0, 1, -1}; // Col movement: No change, No change, Right, Left

        while (!q.isEmpty()) {
            Tile t = q.poll(); // Dequeue the current tile

            // Check if we reached the goal ('$') BEFORE moving through a doorway
            if (t.getChar() == '$') {
                reconstructPath(t);
                System.out.println("Path found!");
                return; // Stop searching
            }

            // Explore the four possible directions (N, S, E, W)
            for (int i = 0; i < 4; i++) {
                int newRow = t.getRow() + dRow[i];
                int newCol = t.getCol() + dCol[i];
                int room = t.getRoom(); // Stay in the same room

                if (newRow >= 0 && newRow < maze.getNumRows() &&
                    newCol >= 0 && newCol < maze.getNumCols() &&
                    !visited[room][newRow][newCol] &&
                    maze.isWalkable(room, newRow, newCol)) {

                    // If we find the coin, prioritize it
                    if (maze.getMap()[room][newRow][newCol] == '$') {
                        reconstructPath(new Tile(newRow, newCol, room, '$'));
                        System.out.println("Path found!");
                        return;
                    }

                    // Otherwise, mark as visited and add to queue
                    visited[room][newRow][newCol] = true;
                    prev[room][newRow][newCol] = t;
                    q.add(new Tile(newRow, newCol, room, maze.getMap()[room][newRow][newCol]));
                }
            }

            // Only move through the doorway IF no '$' was found in this room
            if (t.getChar() == '|') {
                int currentRoom = t.getRoom();
                int nextRoom = currentRoom + 1;

                if (nextRoom < maze.getNumRooms()) {
                    for (int row = 0; row < maze.getNumRows(); row++) {
                        for (int col = 0; col < maze.getNumCols(); col++) {
                            if (maze.getMap()[nextRoom][row][col] == '|') {
                                visited[nextRoom][row][col] = true;
                                prev[nextRoom][row][col] = t;
                                q.add(new Tile(row, col, nextRoom, '|'));
                                System.out.println("Wolverine moved to the next maze!");
                                return;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("No path found.");
    }
    
    private void reconstructPath(Tile goal) {
        Tile t = goal;
        while (t != null && t.getChar() != 'W') { // Stop when reaching Wolverine
            if (maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] != '$') { // Don't overwrite $
                maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] = '+'; // Mark path
            }
            t = prev[t.getRoom()][t.getRow()][t.getCol()]; // Move to previous tile
        }
    }



}
