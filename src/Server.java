import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Server extends Thread {
	DatagramSocket s;
	int col, row;
	Inventory inv;

	ArrayList<Player> quePlayers;
	public Server(int port, int col, int row, Inventory inv, ArrayList<Player> quePlayers) {
		try {
			s = new DatagramSocket(port);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		this.col = col;
		this.row = row;
		this.inv = inv;
		this.quePlayers = quePlayers;
	}

	public ArrayList<Item> recievePacket(ArrayList<Player> player) {
		byte[] data = new byte[40500];
		DatagramPacket dpr = new DatagramPacket(data, data.length);
		try {
			s.receive(dpr);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ArrayList<Item> items = new ArrayList<>();
		try (ByteArrayInputStream bais = new ByteArrayInputStream(dpr.getData())) {
			try (ObjectInputStream ois = new ObjectInputStream(bais)) {
				Packet packetRec = (Packet) ois.readObject();
				Item item = packetRec.items.get(0);
				Player p = new Player((InetSocketAddress) dpr.getSocketAddress(), item);
				if (item != null) {

					// add new players
					boolean in = false;
					for (int i=0; i < player.size(); i++) { if (player.get(i).ia.equals(dpr.getSocketAddress())) {
							player.get(i).setDate(new Date());
							in = true;
						}
					}
					if (!in) {
						p.setDate(new Date());
						player.add(p);
					}

					if (item instanceof Snake && item.x.get(0) > col - 1 || item.x.get(0) < 0 || item.y.get(0) > row - 1 || item.y.get(0) < 0) {
						item.die();
					}

					// update items
					inv.addItem(item);
					items = packetRec.items;

					// remove disconnected players
					ArrayList<Integer> remove = new ArrayList<>();
					for (int j = 0; j < player.size(); j++) {
						if (new Date().getTime() - player.get(j).d.getTime() > ((Snake)player.get(j).i).delay + 100) {
							synchronized (inv) {
								inv.items.remove(player.get(j).i);
							}
							remove.add(j);
						}
					}
					for (int j=0; j < remove.size(); j++) {
						ArrayList<Integer> removeP = new ArrayList<>();
						for (int k=0; k < player.size(); k++) {
							if (player.get(remove.get(j)).equals(player.get(k))) {
								removeP.add(k);
							}
						}
						for (int k=0; k < removeP.size(); k++) {
							player.remove((int) removeP.get(k));
						}
					}

					// remove dead items
					inv.removeDeadItems();
				}
				this.quePlayers.add(p);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return items;
	}
	public void sendPacket(Player player, ArrayList<Item> items) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				Packet packetSend = new Packet(items);
				packetSend.setRowCol(row, col);
			synchronized (inv) {
				oos.writeObject(packetSend);
			}
			DatagramPacket dps = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, player.ia.getAddress(), player.ia.getPort());
			s.send(dps);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
