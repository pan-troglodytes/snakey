/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static ArrayList<Item> orphans = new ArrayList<>();
    static ArrayList<ItemSpawner> spawners = new ArrayList<>();
    static Blueprint map;

    public static void main(String[] args) throws IOException {
        int col = 15;
        int row = 15;
        for (int i=0; i < args.length; i++) {
            if (args[i].equals("--col")) {
                col = Integer.parseInt(args[i+1]);
            } else if (args[i].equals("--row")) {
                row = Integer.parseInt(args[i+1]);
            }
        }
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("snake");
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);

        map = new Blueprint(col, row);
        Item.setBlueprint(map);
        ItemSpawner.setBlueprint(map);
        orphans.add(new Snake(new KeyHandler(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT), 4, (int) (Math.log10((map.getCol() + map.getRow()))*100) ,0,0, null));
        spawners.add(new ItemSpawner(Apple.class, 0, 0, col-1, row-1, 3, 1000));
        spawners.add(new ItemSpawner(Banana.class, 0, 0, col-1, row-1, 1, 3000));
        Portal p = new Portal(null);
        Portal p1 = new Portal(null);
        p.linkPair(p1);
        p.setPosition((int) (col * .1), (int) (row * .1));
        p1.setPosition((int) (col * .9), (int) (row * .9));
        p1.setSprite("sprites/portal-orange.png");
        orphans.add(p);
        orphans.add(p1);
        GamePanel gamePanel = new GamePanel(col, row, 16, 2, map, spawners, orphans);
        window.addKeyListener(((Snake)orphans.get(0)).getSnakeControls());
        window.add(gamePanel);
        window.pack();
    }
}
