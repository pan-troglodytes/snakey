import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client extends Thread {
	InetAddress ip;
	DatagramSocket s;
	int port;
	ArrayList<Item> items = new ArrayList<>();
	Packet rec;
	private JSONObject received;


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

			String message = new String(packet.getData());

			received = new JSONObject(message);
			synchronized (this) {
				notifyAll();
			}


			ArrayList<Item> items = new ArrayList<>();

			System.out.println(received);
			JSONArray ja = received.getJSONArray("items");
			for (int i=0; i < ja.length(); i++) {
				JSONObject item = ja.getJSONObject(i);

				String[] colors = item.getString("color").split("-");
				Color drawingC = new Color(Integer.parseInt(colors[0]) , Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
				JSONArray jaX =  item.getJSONArray("x");
				ArrayList<Integer> Dx = new ArrayList<>();
				for (int j=0; j < jaX.length(); j++) {
					Dx.add((Integer) jaX.get(j));
				}
				JSONArray jaY =  item.getJSONArray("y");
				ArrayList<Integer> Dy = new ArrayList<>();
				for (int j=0; j < jaY.length(); j++) {
					Dy.add((Integer) jaY.get(j));
				}
				 ArrayList<Character> Dd = null;
				if (item.has("d")) {
					JSONArray jaD = null;
					jaD = item.getJSONArray("d");
				 	Dd= new ArrayList<>();
					for (int j = 0; j < jaD.length(); j++) {
						Dd.add(((String) jaD.get(j)).charAt(0));
					}
				}
				Drawing snake = new Drawing(Dx,Dy,Dd,drawingC,item.getString("id"),item.getInt("value"));
				items.add(snake);



			}
			this.items = items;

		}
	}

	public void sendItem(Item item) {
		JSONObject snake = new JSONObject();
		snake.put("id",item.toString());
		snake.put("x", item.x);
		snake.put("y", item.y);
		snake.put("d", ((Snake)item).d);
		snake.put("value", item.value);
		snake.put("color", item.color.getRed()+"-"+item.color.getGreen()+"-"+item.color.getBlue());
		JSONObject itemsJO = new JSONObject();
		itemsJO.put("snake", snake);


		byte[] data = itemsJO.toString().getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			s.send(packet);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<Item> getItems() {
		if (received == null) {
			return null;
		}
		return items;
	}
	public Integer getRow() {
		if (received == null) {
			return null;
		}
		return received.getInt("row");
	}
	public Integer getCol() {
		if (received == null) {
			return null;
		}
		return received.getInt("col");
	}
}