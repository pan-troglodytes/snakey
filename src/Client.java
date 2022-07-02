import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
	Socket s;
	PrintStream ps;

	public Client(String ip, int port, int col, int row) throws IOException {
		s = new Socket(ip, port);
		ps = new PrintStream(s.getOutputStream());
		ps.println("create:"+col+":"+row);
		ps.flush();
		ps.println("create:"+col+":"+row);
		ps.flush();
	}

	public void sendCoords(String action, int x, int y) {
		ps.println(action+":"+x+":"+y);
		ps.flush();
	}
}
