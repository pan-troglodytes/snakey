import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
	ArrayList<Item> items;
	int row;
	int col;

	public Packet(ArrayList<Item> items) {
		this.items = items;
	}
	public void setRowCol(int row, int col) {
		this.row = row;
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
}
