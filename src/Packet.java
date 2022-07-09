import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
	int x;
	int y;
	String action;
	Item item;
	ArrayList<Item> items;

	public Packet(int x, int y, String action, Item item, ArrayList<Item> items) {
		this.x = x;
		this.y = y;
		this.action = action;
		this.item = item;
		this.items = items;
	}
}
