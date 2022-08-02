/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static ArrayList<Item> orphans = new ArrayList<>();
    static ArrayList<ItemSpawner> spawners = new ArrayList<>();
    static Blueprint map;
    static boolean server = false;
    static String name = "";
    static String ip = "127.0.0.1";
    static String port = "61529";
    static int col = 15;
    static int row = 15;

    public static void main(String[] args) throws IOException {
        for (int i=0; i < args.length; i++) {
            if (args[i].equals("--server")) {
                server = true;
                port = args[i+1];
                row = Integer.parseInt(args[i+2]);
                col = Integer.parseInt(args[i+3]);
            } else if (args[i].equals("--client")) {
                name = args[i+1];
                ip = args[i+2];
                port = args[i+3];
            }
        }

        if (server) {
            Portal p=new Portal(null);
            p.setPosition((int) (row *.1),(int)( col * .1));
            Portal p1 = new Portal(null);
            p1.setPosition((int) (row *.8),(int)( col * .8));
            p.setPair(p1);
            p1.setPair(p);
            orphans.add(p);
            orphans.add(p1);
            Server s = new Server(Integer.parseInt(port), col, row, orphans, spawners);
            s.start();

        } else {
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setTitle("snake");
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            window.setResizable(false);
            Client c = new Client(ip,Integer.parseInt(port));
            c.start();
            map = new Blueprint(col, row, c);
            orphans.add(new Snake(name, new KeyHandler(KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_K), 6, (int) (Math.log10((map.getCol() + map.getRow()))*100) ,(int)(col * .2),(int)(row * .2), null));
            ((Snake)orphans.get(0)).c = c;
            // sending packet to server will trigger a response containing the panel dimensions
            c.sendItem(null);
            synchronized (c) {
                try {
                    c.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // >c.getrow()
            // call the dimensions the client has received from server
            GamePanel gamePanel = new GamePanel(c.getRow(), c.getCol(), 16, 2, map, spawners, orphans, c);
            if (!name.equals("")) {
                orphans.get(0).setId(name);
            }
            window.addKeyListener((((Snake)orphans.get(0)).getSnakeControls()));
            window.add(gamePanel);
            window.pack();
        }

    }
}
