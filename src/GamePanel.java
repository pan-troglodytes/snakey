import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
    ArrayList<Snake> snakes = new ArrayList<>();
    ArrayList<Item> itemOrphans = new ArrayList<>();

    public GamePanel(int col, int row, int resolution, int scale) {
        this.resolution = resolution;
        this.scale = scale;
        this.tileSize = resolution * scale;
        this.col = col;
        this.row = row;
        this.width = col * tileSize;
        this.height = row * tileSize;
        this.map = new Blueprint(col, row);
        ItemSpawner.setBlueprint(map);
        Item.setBlueprint(map);
        this.snakes.add(new Snake(4, 150,3,3, null, map));
        this.itemSpawners.add(new ItemSpawner(Apple.class, 0, 0, col-1, row-1, 3, 1000));
        this.itemSpawners.add(new ItemSpawner(Banana.class, 0, 0, col-1, row-1, 1, 3000));
        Portal p = new Portal(null);
        Portal p1 = new Portal(null);
        p.linkPair(p1);
        p.setPosition(5, 5);
        p1.setPosition(col-5, row-5);
        p1.setColor(new Color(255, 151, 0));
        this.itemOrphans.add(p);
        this.itemOrphans.add(p1);
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        for (Snake snake : snakes) {
            this.addKeyListener(snake.getSnakeControls());
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
        for (Snake snake : snakes) {
            snake.draw(g, tileSize);
        }
        for (ItemSpawner itemSpawner : itemSpawners) {
            itemSpawner.drawItems(g, tileSize);
        }
        for (Item item : itemOrphans) {
            item.draw(g, tileSize);
        }
    }
}
