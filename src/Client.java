import java.io.*;
import java.net.*;

public class Client extends Thread {
	InetAddress ip;
	DatagramSocket s;
	int port;


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

			// recieve data
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				s.receive(packet);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			String msg = new String(packet.getData());
			System.out.println("server: " + msg);
		}
	}

	public void sendPacket(Packet packet) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(packet);
			sendData(baos.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			s.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}