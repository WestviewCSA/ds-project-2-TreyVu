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
			int numMazes 	= scanner.nextInt();
			
			// process the map!
			int rowIndex = 0;
			while (scanner.hasNextLine()) {
				// grab a line (one row of the map)
				String row = scanner.nextLine();
				if (row.length() > 0) {
					for (int i = 0; i < numCols && i < row.length(); i++) {
						char element = row.charAt(i);
						Tile obj = new Tile(rowIndex, i, element);
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			// handle exception
			System.out.println(e);
			System.out.println("gg");
		}
	}
}
