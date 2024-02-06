import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class Client extends Thread {
	protected InetAddress ip;
	protected DatagramSocket s;
	protected int port;
	protected ArrayList<Drawing> items = new ArrayList<>();
	protected Packet rec;
	protected JSONObject received;


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



			if (received.has("items")) {
				JSONArray ja = received.getJSONArray("items");
				ArrayList<Integer> itemsFound = new ArrayList<>();

				for (int i=0; i < items.size(); i++) {
					itemsFound.add(0);
				}


			
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
					ArrayList<Integer> Dio = null;
					if (item.has("image-order")) {
						JSONArray jaIo = null;
						jaIo = item.getJSONArray("image-order");
					 	Dio= new ArrayList<>();
						for (int j = 0; j < jaIo.length(); j++) {
							Dio.add((Integer) jaIo.get(j));
						}
					}

					Drawing drawing = new Drawing(Dx,Dy,Dd, Dio, new ArrayList<>(), drawingC,item.getString("id"),item.getInt("value"));

					int newItemInCurrentItemsIndex = -1;
					for (int j=0; j < items.size(); j++) {
						if (item.getString("id").equals(items.get(j).getId())) {
							newItemInCurrentItemsIndex = j;
						}
					}

					if (newItemInCurrentItemsIndex > -1) {
						items.get(newItemInCurrentItemsIndex).updateValues(Dx, Dy, Dd, Dio, item.getInt("value"), item.getString("color"));
						itemsFound.set(newItemInCurrentItemsIndex, 1);
					} else {
						items.add(drawing);
					}
				}
				for (int k = 0; k < itemsFound.size(); k++) {
					if (itemsFound.get(k) == 0) {
						items.remove(k);
						itemsFound.remove(k);
					}
				}
			}
			// share images
			ArrayList<String> Din = null;
			if (received.has("images")) {
				JSONArray jaIn = null;
				JSONObject jo = received.getJSONObject("images");
				Iterator it = jo.keys();
				String id = (String) it.next();
				jaIn = jo.getJSONArray(id);
				Din = new ArrayList<>();
				for (int j = 0; j < jaIn.length(); j++) {
					Din.add((String) jaIn.get(j));
				}

				Drawing drawing = new Drawing(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
				Din, new Color(255, 255, 255), id, 0);
						items.add(drawing);
			}
		}

		
	}
		//System.out.println(items);

	public void sendItem(Item item) {
		JSONObject snake = new JSONObject();
		snake.put("id",item.toString());
		snake.put("x", item.x);
		snake.put("y", item.y);
		snake.put("d", ((Snake)item).d);
		snake.put("value", item.value);
		snake.put("color", item.color.getRed()+"-"+item.color.getGreen()+"-"+item.color.getBlue());
		snake.put("image-order", ((Snake)item).getRenderOrder());
		if (!item.imagesSent) {
			ArrayList<String> imagesNew = new ArrayList<>();
			for (int i=0; i < ((Snake)item).images.size(); i++) {
            		ByteArrayOutputStream baos = new ByteArrayOutputStream();
						try {
							ImageIO.write(item.images.get(i), "png", baos);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					imagesNew.add(Base64.getEncoder().encodeToString(baos.toByteArray()));
			}
			snake.put("image-new", imagesNew);
			item.imagesSent();
		}
		
		JSONObject itemsJO = new JSONObject();
		itemsJO.put("snake", snake);


		byte[] data = itemsJO.toString().getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			s.send(packet);
			System.out.println(itemsJO);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<Drawing> getItems() {
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