import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class p3 {
	public static void main(String[] args) {		
		readMap("src/TEST03");
	} 
	
	public static void readMap(String filename) {
		
		try {
			File file  = new File(filename);
			Scanner scanner = new Scanner(file); 			
			
			int numRows 	= scanner.nextInt();
			int numCols 	= scanner.nextInt();
			int numRooms 	= scanner.nextInt();
			Tile[][][] board = new Tile[numRows][numCols][numRooms];
			boolean[][][] exploredBoard = new boolean[numRows][numCols][numRooms];
			
			int rowIndex = 0;
			int colIndex = 0;
			int roomIndex = 1;
			
			// scan each row
			while (scanner.hasNextLine()) {
				String row = scanner.nextLine();			
				if (row.length() > 0) {
					// scan each column
					for (int i = 0; i < numCols && i < row.length(); i++) {
						char element = row.charAt(i);
						if (findChar(element)) {
							Tile obj = new Tile(rowIndex, i, roomIndex, element);
							board[rowIndex][colIndex][roomIndex] = obj;
							colIndex++;
						}						
					}
					rowIndex++;
				}
				
				// update rooms and rows
				if (rowIndex > numRows) {
					rowIndex = 0;
					roomIndex++;
				}
			}
			
			Queue<Tile> q = new LinkedList<>();
			Tile startingPosition = findStartingPosition(board);
//			exploredBoard[startingPosition.getRow()][startingPosition.getCol()][startingPosition.getRoom()] = true;
			q.add(startingPosition);
			boolean coinFound = false;
			boolean doorFound = false;
			
			
			while (!coinFound) {	
				Tile t = q.remove();
				exploredBoard[t.getRow()][t.getCol()][t.getRoom()] = true;
				
				ArrayList<String> possiblePositions = checkPosition(board, exploredBoard, t);
				for (int i = 0; i < possiblePositions.size(); i++) {
					String str = possiblePositions.get(i);
					if (str.equals("N")) {
						char c = board[t.getRow()-1][t.getCol()][t.getRoom()].getChar();
						Tile N = new Tile(t.getRow()-1, t.getCol(), t.getRoom(), c);
						q.add(N);
					} else if (str.equals("S")) {
						char c = board[t.getRow()+1][t.getCol()][t.getRoom()].getChar();
						Tile S = new Tile(t.getRow()+1, t.getCol(), t.getRoom(), c);
						q.add(S);
					} else if (str.equals("E")) {
						char c = board[t.getRow()][t.getCol()+1][t.getRoom()].getChar();
						Tile E = new Tile(t.getRow(), t.getCol()+1, t.getRoom(), c);
						q.add(E);
					} else if (str.equals("W")) {
						char c = board[t.getRow()][t.getCol()-1][t.getRoom()].getChar();
						Tile W = new Tile(t.getRow(), t.getCol()-1, t.getRoom(), c);
						q.add(W);
					}
					
					if (q.peek().getChar() == '$') {
						coinFound = true;
					} else if (q.peek().getChar() == '|') {
						doorFound = true;
					} 
				}
			}
			
			
		} catch (FileNotFoundException e) {
			// handle exception
			System.out.println(e);
			System.out.println("gg");
		}
	}
	
	public static Tile findStartingPosition(Tile[][][] b) {
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				if (b[i][j][0].getChar() == 'W') {
					return b[i][j][0];
				}
			}
		}
		System.out.println("Something broke with findStartingPosition()");
		return null;
	} 	
	
	public static ArrayList<String> checkPosition(Tile[][][] b, boolean[][][] e, Tile t) {
		int numRows = b.length;
		int numCols = b[0].length;
		int numRooms = b[0][0].length;
		
		int row = t.getRow();
		int col = t.getCol();
		
		ArrayList<String> possiblePositions = new ArrayList<String>();
		for (int k = 0; k < numRooms; k++) {
			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < numCols; j++) {
					Tile position = b[i][j][k];
					char c = position.getChar();
					int pRow = position.getRow();
					int pCol = position.getCol();
					
					if (c == '.' || c == '$' || c == '|' && e[i][j][k] == false) {
						if (col == pCol && row == pRow-1) {
							possiblePositions.add("N");
						} else if (row == pRow && col == pCol+1) {
							possiblePositions.add("E");
						} else if (col == pCol && row == pRow+1) {
							possiblePositions.add("S");
						} else if (row == pRow && col == pCol-1) {
							possiblePositions.add("W");
						}
					}
				}
			}
		}
		return possiblePositions;
	}
	public static boolean findChar(char character) {
		if (character == '@') {
			return true;
		} else if (character == '.') {
			return true;
		} else if (character == '|') {
			return true;
		} else if (character == 'W') {
			return true;
		} else if (character == '$') {
			return true;
		}
		return false;
	}
}
