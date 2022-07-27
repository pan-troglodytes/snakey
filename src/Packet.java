import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
	ArrayList<Item> items;

	public Packet(ArrayList<Item> items) {
		this.items = items;
	}
}
