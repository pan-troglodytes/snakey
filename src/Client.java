import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client extends Thread {
	InetAddress ip;
	DatagramSocket s;
	int port;
	ArrayList<Item> items = new ArrayList<>();
	Packet rec;


	public Client(String ip, int port) {
		this.port = port;
		try {
			s = new DatagramSocket();
			this.ip = InetAddress.getByName(ip);
		} catch (SocketException | UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[102400];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				s.receive(packet);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try(ByteArrayInputStream b = new ByteArrayInputStream(packet.getData())){
				try(ObjectInputStream o = new ObjectInputStream(b)){
					Packet packetRec = (Packet) o.readObject();
					rec = packetRec;

				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void sendItem(Item item) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			items.add(item);
			Packet p = new Packet(items);
			oos.writeObject(p);
			DatagramPacket packetSend = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, ip, port);
			s.send(packetSend);
			oos.flush();
			baos.flush();
			items = new ArrayList<>();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<Item> getItems() {
		if (rec == null) {
			return null;
		}
		return rec.items;
	}
}