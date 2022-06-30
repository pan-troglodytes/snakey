/*
     This file is part of snakey.

    snakey is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

    snakey is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with snakey. If not, see <https://www.gnu.org/licenses/>.
 */
import com.sun.jdi.ArrayReference;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    final int resolution;
    final int scale;
    final int tileSize;
    final int col;
    final int row;
    final int width;
    final int height;
    Blueprint map;
    ArrayList<ItemSpawner> itemSpawners = new ArrayList<>();
    ArrayList<Item> itemOrphans = new ArrayList<>();

    public GamePanel(int col, int row, int resolution, int scale, Blueprint map, ArrayList<ItemSpawner> spawners, ArrayList<Item> orphans) throws IOException {
        this.resolution = resolution;
        this.scale = scale;
        this.tileSize = resolution * scale;
        this.col = col;
        this.row = row;
        this.width = col * tileSize;
        this.height = row * tileSize;
        this.map = map;
        this.itemOrphans = orphans;
        this.itemSpawners = spawners;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        for (Item item : itemOrphans) {
            if (item.getClass() == Snake.class) {
                this.addKeyListener(((Snake) item).getSnakeControls());
            }
        }
        this.setFocusable(true);
        new Timer(100,this).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (ItemSpawner itemSpawner : itemSpawners) {
            itemSpawner.drawItems(g, tileSize);
        }
        for (Item item : itemOrphans) {
            item.draw(g, tileSize);
        }
    }
}
