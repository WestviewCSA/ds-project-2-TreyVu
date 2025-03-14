
public class Map {
	private int numRows;
	private int numCols;
	private int numRooms;
	private Tile[][][] map = new Tile[numRows][numCols][numRooms];
	
	public Map(int numRows, int numCols, int numRooms, Tile[][][] map) {
		super();
		this.numRows = numRows;
		this.numCols = numCols;
		this.numRooms = numRooms;
		this.map = map;
	}
	
	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public int getNumRooms() {
		return numRooms;
	}

	public void setNumRooms(int numRooms) {
		this.numRooms = numRooms;
	}

	public Tile[][][] getMap() {
		return map;
	}

	public void setMap(Tile[][][] map) {
		this.map = map;
	}
}
