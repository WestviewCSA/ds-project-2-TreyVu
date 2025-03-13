
public class Tile {
	private int row, col, room;
	private char character;
	
	public Tile(int row, int col, int room, char character) {
		super();
		this.row = row;
		this.col = col;
		this.room = room;
		this.character = character;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRoom() {
		return room;
	}
	public void setRoom(int room) {
		this.room = room;
	}
	public char getChar() {
		return character;
	}
	public void setChar(char type) {
		this.character = type;
	}
	
	
}
