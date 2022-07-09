import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	public Server(int col, int row) throws IOException, ClassNotFoundException {
		ServerSocket ss = new ServerSocket(61529);
		Socket s = ss.accept();
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());
		ArrayList<ArrayList<ArrayList<Item>>> xy = new ArrayList<>();
		for (int i = 0; i < col; i++) {
			xy.add(new ArrayList<>());
			for (int j = 0; j < row; j++) {
				xy.get(i).add(new ArrayList<>());
			}
		}
		while (true) {

			Packet packet = (Packet) is.readObject();

			if (packet != null) {
				System.out.println(packet.item);
				Item item = packet.item;
				int x = packet.x;
				int y = packet.y;
				if (packet.action.equals("+")) {
					ArrayList<Item> coord = xy.get(x).get(y);
					if (!coord.contains(item)) {
						xy.get(x).get(y).add(item);
					}
				} else if (packet.action.equals("-")) {
					xy.get(x).get(y).remove(item);
				} else if (packet.action.equals("get")) {
					os.writeObject(xy.get(x).get(y));
				}
				System.out.println(xy);
			}
		}
	}
}