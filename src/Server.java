import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class Server extends Thread {
	private DatagramSocket s;
	private int col, row;
	private Inventory inv;
	private ArrayList<Player> quePlayers;
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


		byte[] data = new byte[40000];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			s.receive(packet);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ArrayList<Item> items = new ArrayList<>();
		String message = new String(packet.getData());

		JSONObject jo = new JSONObject(message);
		Iterator keys = jo.keys();
		while (keys.hasNext()) {
			Object key = keys.next();
			if (((String) key).equals("snake")) {
				Snake snake = null;
				try {
					snake = new Snake("", null, 4, 1000, (int) (col * .2), (int) (row * .2), null, 0, 0, 0);
					JSONObject jo2 = jo.getJSONObject((String) key);
					snake.id = jo2.getString("id");

					ArrayList<Integer> x = new ArrayList<>();


					JSONArray jaX = jo2.getJSONArray("x");
					snake.x = new ArrayList<>();
					for (int i = 0; i < jaX.length(); i++) {
						snake.x.add((Integer) jaX.get(i));
					}
					JSONArray jaY = jo2.getJSONArray("y");
					snake.y = new ArrayList<>();
					for (int i = 0; i < jaY.length(); i++) {
						snake.y.add((Integer) jaY.get(i));
					}
					JSONArray jaD = jo2.getJSONArray("d");
					snake.d = new ArrayList<>();
					for (int i = 0; i < jaD.length(); i++) {
						snake.d.add(((String) jaD.get(i)).charAt(0));
					}
					snake.value = jo2.getInt("value");
					String[] colors = jo2.getString("color").split("-");
					snake.setColor(new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2])));


					Player p = new Player((InetSocketAddress) packet.getSocketAddress(), snake);

					// add new players
					boolean in = false;
					for (int i = 0; i < player.size(); i++) {
						if (player.get(i).getInetSockerAddress().equals(packet.getSocketAddress())) {
							player.get(i).setDate(new Date());
							in = true;
						}
					}
					if (!in) {
						p.setDate(new Date());
						player.add(p);
					}

					if (snake.x.get(0) > col - 1 || snake.x.get(0) < 0 || snake.y.get(0) > row - 1 || snake.y.get(0) < 0) {
						snake.die();
					}
					inv.addItem(snake);
					items.add(snake);

					this.quePlayers.add(p);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
			// remove disconnected players
			ArrayList<Integer> remove = new ArrayList<>();
			for (int j = 0; j < player.size(); j++) {
				if (new Date().getTime() - player.get(j).getDate().getTime() > ((Snake) player.get(j).getItem()).delay + 100) {
					synchronized (inv) {
						inv.items.remove(player.get(j).getItem());
					}
					remove.add(j);
				}
			}
			for (int j = 0; j < remove.size(); j++) {
				ArrayList<Integer> removeP = new ArrayList<>();
				for (int k = 0; k < player.size(); k++) {
					if (player.get(remove.get(j)).equals(player.get(k))) {
						removeP.add(k);
					}
				}
				for (int k = 0; k < removeP.size(); k++) {
					player.remove((int) removeP.get(k));
				}
			}

			inv.removeDeadItems();
			return items;

	}
	public void sendPacket(Player player, ArrayList<Item> items) {
			JSONObject itemsJO = new JSONObject();
			JSONArray ja = new JSONArray();
			for (int i=0; i < items.size(); i++) {
				ja.put(items.get(i).jsonify());
			}
			itemsJO.put("items", ja);
			itemsJO.put("row", row);
			itemsJO.put("col", col);
			byte[] data = itemsJO.toString().getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, player.getInetSockerAddress().getAddress(), player.getInetSockerAddress().getPort());
			try {
				s.send(packet);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	}
}
