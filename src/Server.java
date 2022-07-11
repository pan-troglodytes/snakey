import java.io.*;
import java.net.*;

public class Server extends Thread {
	DatagramSocket s;
	public Server(int col, int row) {
		try {
			s = new DatagramSocket(61529);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
	}

	public void run () {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				s.receive(packet);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try(ByteArrayInputStream b = new ByteArrayInputStream(packet.getData())){
				try(ObjectInputStream o = new ObjectInputStream(b)){
					Packet item = (Packet) o.readObject();
					System.out.println(item);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
	}

	public void sendData(byte[] data, InetAddress ip, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			s.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}