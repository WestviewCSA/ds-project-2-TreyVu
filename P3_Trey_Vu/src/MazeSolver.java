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

            // Check if we reached the goal ('$')
            if (t.getChar() == '$') {
                System.out.println("Path found!");
                return; // Stop searching
            } else if (t.getChar() == '|') {
                int currentRoom = t.getRoom();
                int nextRoom = currentRoom + 1;

                // Ensure next room exists
                if (nextRoom < maze.getNumRooms()) {
                    for (int row = 0; row < maze.getNumRows(); row++) {
                        for (int col = 0; col < maze.getNumCols(); col++) {
                            if (maze.getMap()[nextRoom][row][col] == '|') {
                                // Move Wolverine to the new doorway in the next maze
                                Tile nextDoorway = new Tile(row, col, nextRoom, '|');
                                q.add(nextDoorway);
                                visited[nextRoom][row][col] = true;
                                System.out.println("Wolverine moved to the next maze!");
                                break;
                            }
                        }
                    }
                }
            }


            // Explore the four possible directions
            for (int i = 0; i < 4; i++) {
                int newRow = t.getRow() + dRow[i];
                int newCol = t.getCol() + dCol[i];
                int room = t.getRoom(); // Stay in the same room

                // Check if the move is valid
                if (newRow >= 0 && newRow < maze.getNumRows() &&
                    newCol >= 0 && newCol < maze.getNumCols() &&
                    !visited[room][newRow][newCol] &&
                    maze.isWalkable(room, newRow, newCol)) {

                    // Mark as visited and add to queue
                    visited[room][newRow][newCol] = true;
                    q.add(new Tile(newRow, newCol, room, maze.getMap()[room][newRow][newCol]));
                }
            }
        }

        System.out.println("No path found.");
    }
    
    private void reconstructPath(Tile goal) {
        Tile t = goal;
        while (t != null && t.getChar() != 'W') { // Stop when reaching Wolverine
            maze.getMap()[t.getRoom()][t.getRow()][t.getCol()] = '+'; // Mark path
            t = prev[t.getRoom()][t.getRow()][t.getCol()]; // Move to parent tile
        }
    }

}
