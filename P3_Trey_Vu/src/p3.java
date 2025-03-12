import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
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
			
			int rowIndex = 0;
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
			
		} catch (FileNotFoundException e) {
			// handle exception
			System.out.println(e);
			System.out.println("gg");
		}
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
