/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static ArrayList<Item> orphans = new ArrayList<>();
    static ArrayList<ItemSpawner> spawners = new ArrayList<>();
    static boolean server = false;
    static String name = "";
    static String ip = "127.0.0.1";
    static String port = "61529";
    static int col = 15;
    static int row = 15;
    static int r = 255;
    static int g = 0;
    static int b = 0;


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

            if (args[i].equals("--color")) {
                r = Integer.parseInt(args[i+1]);
                g = Integer.parseInt(args[i+2]);
                b = Integer.parseInt(args[i+3]);
            }
        }

        if (server) {
            ArrayList<ArrayList<Item>> queItems = new ArrayList<>();
            ArrayList<Player> quePlayers = new ArrayList<>();
	        ArrayList<Player> allPlayers = new ArrayList<>();
            Portal p =new Portal(null);
            p.setPosition((int) (col *.8),(int)( row * .1));
            Portal p1 = new Portal(null);
                p1.setPosition((int) (col *.1),(int)( row * .8));
                p.linkPair(p1);
                orphans.add(p);
                orphans.add(p1);
                spawners.add(new ItemSpawner(Apple.class, 0, 0, col, row, 3, 1));
                spawners.add(new ItemSpawner(Banana.class, 0, 0, col, row, 1, 5));
               Inventory inv = new Inventory(orphans, spawners);
               inv.start();
               inv.addOrphans();
               Server s = new Server(Integer.parseInt(port), col, row, inv,  quePlayers);
           s.start();
           while (true) {
               ArrayList<Item> newItems = s.recievePacket(allPlayers);
               for (int i = 0; i < newItems.size(); i++) {
                   inv.addItem(newItems.get(i));
               }
               ArrayList<Item> items = inv.getItems();
               if (quePlayers.size() > 0) {
                   for (int j = 0; j < items.size(); j++) {
                       for (int k = 0; k < items.get(j).x.size(); k++) {
                           if (quePlayers.get(0).i instanceof Snake && !(items.get(j).equals(quePlayers.get(0).i) && k == 0) && quePlayers.get(0).i.x.get(0).equals(items.get(j).x.get(k)) && quePlayers.get(0).i.y.get(0).equals(items.get(j).y.get(k))) {
                               ((Snake) quePlayers.get(0).i).observe(items.get(j));
                           }
                       }
                   }
               }
               if (quePlayers.size() > 0) {
                   for (int j = 0; j < allPlayers.size(); j++) {
                       ArrayList<Item> toSend = new ArrayList<>();
                       for (int k = 0; k < items.size(); k++) {
                           if (items.get(k).equals(allPlayers.get(j).i) && !items.get(k).equals(quePlayers.get(0).i)) {

                           } else {
                               toSend.add(items.get(k));
                           }
                       }
                       s.sendPacket(allPlayers.get(j), toSend);
                       toSend.clear();
                   }
                   quePlayers.remove(0);
               }
           }
        } else {
            System.out.println("trying to connect to " + ip + ":" + port);
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setTitle("snakey");
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            window.setResizable(false);
            Client c = new Client(ip,Integer.parseInt(port));
            c.start();
	    if (name.equals("slowboi")) {
            	orphans.add(new Snake(name, new KeyHandler(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT), 4, 1000 ,(int)(col * .2),(int)(row * .2), null,r,g,b));
	    } else {
            	orphans.add(new Snake(name, new KeyHandler(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT), 4, (int) (Math.log10((col + row))*100) ,(int)(col * .2),(int)(row * .2), null,r,g,b));
	    }
            // sending packet to server will trigger a response containing the panel dimensions
            Snake.c = c;
            c.sendItem(orphans.get(0));
            synchronized (c) {
                try {
                    c.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // >c.getrow()
            // call the dimensions the client has received from server
            GamePanel gamePanel = new GamePanel(c.getCol(), c.getRow(), 16, 2, spawners, orphans, c);
            System.out.printf("connected!");

            if (!name.equals("")) {
                orphans.get(0).setId(name);
            }
            window.addKeyListener((((Snake)orphans.get(0)).getSnakeControls()));
            window.add(gamePanel);
            window.pack();
        }

    }
}
