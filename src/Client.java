import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client  implements Serializable{
	Socket s;
	ObjectOutputStream os;
	ObjectInputStream is;


	public Client(String ip, int port) throws IOException {
		s = new Socket(ip, port);
		os = new ObjectOutputStream(s.getOutputStream());
		is = new ObjectInputStream(s.getInputStream());
	}

	public ArrayList<Item> sendCoords(String action, int x, int y, Item item) throws IOException, ClassNotFoundException {
		Packet packSend = new Packet(x, y, action, item, null);
		System.out.println(packSend.action);
		os.writeObject(packSend);

		if (action.equals("get")) {
			ArrayList<Item> packRecv = (ArrayList<Item>) is.readObject();
			if (packRecv != null) {
				return packRecv;
			}
		}
		return null;
	}
}
