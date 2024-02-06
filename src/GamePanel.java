/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
//import com.sun.jdi.ArrayReference;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    private final int resolution;
    private final int scale;
    private final int tileSize;
    private final int col;
    private final int row;
    private final int width;
    private final int height;
    private ArrayList<ItemSpawner> itemSpawners = new ArrayList<>();
    private ArrayList<Item> itemOrphans = new ArrayList<>();
    private Client c;

    public GamePanel(int col, int row, int resolution, int scale, ArrayList<ItemSpawner> spawners, ArrayList<Item> orphans, Client c) {
        this.c = c;
        this.resolution = resolution;
        this.scale = scale;
        this.tileSize = resolution * scale;
        this.col = col;
        this.row = row;
        this.width = col * tileSize;
        this.height = row * tileSize;
        this.itemOrphans = orphans;
        this.itemSpawners = spawners;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.setFocusable(true);
        new Timer(30,this).start();
        Thread t = new Thread( itemOrphans.get(0));
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint(0,0, width, height);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        itemOrphans.get(0).draw(g, tileSize);
        ArrayList<Drawing> items = new ArrayList<>();
        items.addAll(c.getItems());
        ((Snake) itemOrphans.get(0)).update(items);
        if (items != null) {
            for (Item item : items) {
                if (!item.getId().equals(itemOrphans.get(0).getId())) {
                    item.draw(g, tileSize);
                }
            }
        }
    }
}
