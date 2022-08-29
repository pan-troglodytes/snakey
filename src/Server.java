import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Thread {
	DatagramSocket s;
	int col, row;
	Inventory inv;

	HashMap<Item, Integer> connections = new HashMap<>();
	public Server(int port, int col, int row, ArrayList<Item> orphans, ArrayList<ItemSpawner> spawners) {
		try {
			s = new DatagramSocket(port);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		inv = new Inventory(orphans, spawners);
		this.col = col;
		this.row = row;

		Thread t = new Thread(inv);
		t.start();
	}

	public void run () {
		while (true) {
			byte[] data = new byte[40500];
			DatagramPacket dpr = new DatagramPacket(data, data.length);
			try {
				s.receive(dpr);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try(ByteArrayInputStream bais = new ByteArrayInputStream(dpr.getData())) {
				try(ObjectInputStream ois = new ObjectInputStream(bais)) {
					Packet packetRec = (Packet) ois.readObject();
					Item item = packetRec.items.get(0);
					if (item != null) {
						if (item instanceof Snake && item.x.get(0) > row - 1 || item.x.get(0) < 0 || item.y.get(0) > col - 1 || item.y.get(0) < 0) {
							item.die();
						}
						inv.addItem(item);
						if (item instanceof Snake) {

							boolean contains = false;
							Item i = null;
							for (HashMap.Entry<Item, Integer> set : connections.entrySet()) {
								if (set.getKey().toString().equals(item.toString())) {
									i = set.getKey();
									contains = true;
								}
							}
							if (contains) {
								connections.replace(i, 0);
							} else {
								connections.put(item, 0);
							}
						}
					}
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			inv.addOrphans();
			ArrayList<Item> items = inv.getItems();
			if (items != null) {
				for (int i=0; i < items.size(); i++) {
					for (int j=0; j < items.size(); j++) {
						for (int k = 0; k < items.get(j).x.size(); k++) {
							if (items.get(i) instanceof Snake && !(items.get(j).equals(items.get(i)) && k == 0) && items.get(i).x.get(0).equals(items.get(j).x.get(k)) && items.get(i).y.get(0).equals(items.get(j).y.get(k))) {
								((Snake) items.get(i)).observe(items.get(j));
							}
						}
					}
				}
			}
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				Packet packetSend = new Packet(items);
				packetSend.setRowCol(row, col);
				oos.writeObject(packetSend);
				DatagramPacket dps = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, dpr.getAddress(), dpr.getPort());
				s.send(dps);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			Item remove = null;
			for (HashMap.Entry<Item, Integer> set : connections.entrySet()) {
				connections.replace(set.getKey(), set.getValue() + 1);
				if (connections.get(set.getKey()) > 1) {
					items.remove(set.getKey());
					remove = set.getKey();
				}
			}
			if (remove != null) {
				connections.remove(remove);
			}
			inv.removeDeadItems();
		}
	}
}