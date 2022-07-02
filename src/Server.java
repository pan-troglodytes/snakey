import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	public Server() throws IOException {
		ServerSocket ss = new ServerSocket(61529);
		Socket s = ss.accept();
		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);
		ArrayList<String> cords = new ArrayList<>(List.of(bf.readLine().split(":")));
		ArrayList<ArrayList<Integer>> xy = new ArrayList<>();

		while (true) {
			String msg = bf.readLine();
			if (msg != null) {
				ArrayList<String> msgA = new ArrayList<>(List.of(msg.split(":")));
				if (msgA.get(0).equals("create")) {
					for (int i=0; i < Integer.parseInt(cords.get(1)); i++) {
						xy.add(new ArrayList<>());
						for (int j=0; j < Integer.parseInt(cords.get(2)); j++) {
							xy.get(i).add(0);
						}
					}
					System.out.println("model created");
				} else if (msgA.get(0).equals("+")) {
					System.out.println(msgA);
					System.out.println(xy);
					xy.get(Integer.parseInt(msgA.get(1))).set(Integer.parseInt(msgA.get(2)), 1);
				} else if (msgA.get(0).equals("-")) {
					xy.get(Integer.parseInt(msgA.get(1))).set(Integer.parseInt(msgA.get(2)), 0);
				}
				System.out.println(xy);
			}
		}

	}
}
